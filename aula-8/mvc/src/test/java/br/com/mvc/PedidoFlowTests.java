package br.com.mvc;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;
import br.com.mvc.model.entity.Pedido;
import br.com.mvc.model.entity.PedidoStatus;
import br.com.mvc.model.entity.Product;
import br.com.mvc.model.entity.ProductStatus;
import br.com.mvc.model.repository.PedidoRepository;
import br.com.mvc.model.repository.StockRepository;
import br.com.mvc.model.service.FaturaGateway;
import br.com.mvc.model.service.PedidoService;
import br.com.mvc.model.service.ProductService;
import br.com.mvc.model.service.StockService;

@SpringBootTest
@Import(PedidoFlowTests.GatewayConfig.class)
@Transactional
class PedidoFlowTests {
    @Autowired ProductService products;
    @Autowired StockService stocks;
    @Autowired StockRepository stockRepository;
    @Autowired PedidoService pedidos;
    @Autowired PedidoRepository pedidoRepository;
    @Autowired FakeGateway hub;

    @BeforeEach
    void reset() { hub.fail = false; hub.calls = 0; }

    @Test
    void fechaPedidoGuardaIdEBaixaEstoqueUmaVez() {
        Pedido pedido = novoPedido();
        Pedido fechado = pedidos.fechar(pedido.getId());
        assertEquals(PedidoStatus.FECHADO, fechado.getStatus());
        assertEquals(FakeGateway.ID, fechado.getFaturaId());
        assertEquals(FakeGateway.ID, pedidoRepository.findById(pedido.getId()).orElseThrow().getFaturaId());
        assertEquals(2, stockRepository.findById(pedido.getProduct().getId()).orElseThrow().getQuantity());
        assertEquals(1, hub.calls);

        pedidos.fechar(pedido.getId());
        assertEquals(1, hub.calls);
        assertEquals(2, stockRepository.findById(pedido.getProduct().getId()).orElseThrow().getQuantity());
    }

    @Test
    void falhaDoHubMantemPedidoAbertoESaldo() {
        Pedido pedido = novoPedido();
        hub.fail = true;
        assertThrows(IllegalStateException.class, () -> pedidos.fechar(pedido.getId()));
        assertEquals(PedidoStatus.ABERTO, pedidoRepository.findById(pedido.getId()).orElseThrow().getStatus());
        assertNull(pedidoRepository.findById(pedido.getId()).orElseThrow().getFaturaId());
        assertEquals(3, stockRepository.findById(pedido.getProduct().getId()).orElseThrow().getQuantity());
    }

    private Pedido novoPedido() {
        Product product = products.create(Product.builder().name("Papel")
                .preco(new BigDecimal("12.50")).status(ProductStatus.DISPONIVEL).build());
        stocks.move(product.getId(), 3, "entrada");
        Pedido pedido = pedidos.criar(product.getId(), 1, "cliente@exemplo.com");
        assertEquals(new BigDecimal("12.50"), pedido.getTotal());
        return pedido;
    }

    static class FakeGateway implements FaturaGateway {
        static final UUID ID = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee");
        boolean fail;
        int calls;

        @Override
        public UUID faturar(Long pedidoId, BigDecimal valor, String emailCliente) {
            calls++;
            if (fail) throw new IllegalStateException("Hub indisponível");
            assertNotNull(pedidoId);
            assertEquals(new BigDecimal("12.50"), valor);
            assertEquals("cliente@exemplo.com", emailCliente);
            return ID;
        }
    }

    @TestConfiguration
    static class GatewayConfig {
        @Bean @Primary FakeGateway fakeGateway() { return new FakeGateway(); }
    }
}

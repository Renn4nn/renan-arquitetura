package br.com.mvc.model.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.mvc.model.entity.Pedido;
import br.com.mvc.model.entity.PedidoStatus;
import br.com.mvc.model.entity.Product;
import br.com.mvc.model.entity.ProductStatus;
import br.com.mvc.model.entity.Stock;
import br.com.mvc.model.repository.PedidoRepository;
import br.com.mvc.model.repository.ProductRepository;
import br.com.mvc.model.repository.StockRepository;

@Service
public class PedidoService {
    private final PedidoRepository pedidos;
    private final ProductRepository products;
    private final StockRepository stocks;
    private final StockService stockService;
    private final FaturaGateway hub;

    public PedidoService(PedidoRepository pedidos, ProductRepository products,
                         StockRepository stocks, StockService stockService, FaturaGateway hub) {
        this.pedidos = pedidos;
        this.products = products;
        this.stocks = stocks;
        this.stockService = stockService;
        this.hub = hub;
    }

    public List<Pedido> listar() { return pedidos.findAll(); }

    @Transactional
    public Pedido criar(Long productId, int quantity, String emailCliente) {
        if (quantity <= 0) throw new IllegalArgumentException("A quantidade deve ser positiva.");
        if (emailCliente == null || !emailCliente.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
            throw new IllegalArgumentException("Informe o e-mail do cliente.");
        Product product = products.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));
        conferirEstoque(product, quantity);
        return pedidos.save(new Pedido(product, quantity, emailCliente));
    }

    @Transactional
    public Pedido fechar(Long id) {
        Pedido pedido = pedidos.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado."));
        if (pedido.getStatus() == PedidoStatus.FECHADO) return pedido;
        conferirEstoque(pedido.getProduct(), pedido.getQuantity());

        // A chamada REST é síncrona: só marcamos FECHADO após receber o ID.
        var faturaId = hub.faturar(pedido.getId(), pedido.getTotal(), pedido.getEmailCliente());
        stockService.move(pedido.getProduct().getId(), pedido.getQuantity(), "saida");
        pedido.fechar(faturaId);
        return pedidos.save(pedido);
    }

    private void conferirEstoque(Product product, int quantity) {
        if (product.getStatus() != ProductStatus.DISPONIVEL)
            throw new IllegalStateException("Produto indisponível para venda.");
        Stock stock = stocks.findById(product.getId())
                .orElseThrow(() -> new IllegalStateException("Produto sem estoque."));
        if (stock.getQuantity() < quantity)
            throw new IllegalStateException("Estoque insuficiente para o pedido.");
    }
}

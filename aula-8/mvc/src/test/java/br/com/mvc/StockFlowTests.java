package br.com.mvc;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import br.com.mvc.model.entity.Product;
import br.com.mvc.model.entity.ProductStatus;
import br.com.mvc.model.repository.ProductRepository;
import br.com.mvc.model.repository.StockRepository;
import br.com.mvc.model.service.ProductService;
import br.com.mvc.model.service.StockService;

@SpringBootTest
@Transactional
class StockFlowTests {
    @Autowired ProductService products;
    @Autowired ProductRepository productRepository;
    @Autowired StockRepository stockRepository;
    @Autowired StockService stocks;

    @Test
    void entradaSaidaEProtecoes() {
        Product product = products.create(Product.builder()
                .name("Papel").preco(new BigDecimal("10.00"))
                .status(ProductStatus.DISPONIVEL).build());
        Long id = product.getId();
        assertEquals(ProductStatus.INDISPONIVEL, product.getStatus());

        stocks.move(id, 5, "entrada");
        assertEquals(5, stockRepository.findById(id).orElseThrow().getQuantity());
        assertEquals(ProductStatus.DISPONIVEL, productRepository.findById(id).orElseThrow().getStatus());

        assertThrows(IllegalStateException.class, () -> stocks.move(id, 6, "saida"));
        assertEquals(5, stockRepository.findById(id).orElseThrow().getQuantity());
        stocks.move(id, 5, "saida");
        assertEquals(ProductStatus.INDISPONIVEL, productRepository.findById(id).orElseThrow().getStatus());

        products.discontinue(id);
        assertThrows(IllegalStateException.class, () -> stocks.move(id, 1, "entrada"));
        assertEquals(0, stockRepository.findById(id).orElseThrow().getQuantity());
    }
}

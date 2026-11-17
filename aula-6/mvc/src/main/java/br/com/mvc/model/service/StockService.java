package br.com.mvc.model.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.mvc.model.entity.Product;
import br.com.mvc.model.entity.ProductStatus;
import br.com.mvc.model.entity.Stock;
import br.com.mvc.model.repository.ProductRepository;
import br.com.mvc.model.repository.StockRepository;

@Service
public class StockService {
    private final ProductRepository products;
    private final StockRepository stocks;

    public StockService(ProductRepository products, StockRepository stocks) {
        this.products = products;
        this.stocks = stocks;
    }

    public List<Stock> list() { return stocks.findAll(); }

    @Transactional
    public void move(Long productId, int amount, String operation) {
        if (amount <= 0) throw new IllegalArgumentException("A quantidade deve ser positiva.");
        Product product = products.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));
        if (product.getStatus() == ProductStatus.DESCONTINUADO)
            throw new IllegalStateException("Produto descontinuado não pode ter estoque movimentado.");

        Stock stock = stocks.findById(productId).orElseGet(() -> new Stock(product));
        switch (operation) {
            case "entrada" -> stock.add(amount);
            case "saida" -> {
                if (stock.getQuantity() < amount)
                    throw new IllegalStateException("Estoque insuficiente.");
                stock.remove(amount);
            }
            default -> throw new IllegalArgumentException("Operação inválida.");
        }
        product.setStatus(stock.getQuantity() == 0
                ? ProductStatus.INDISPONIVEL : ProductStatus.DISPONIVEL);
        stocks.save(stock);
        products.save(product);
    }
}

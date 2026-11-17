package br.com.mvc.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "stock")
public class Stock {
    @Id
    private Long productId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    @Version
    private Long version;

    protected Stock() {}

    public Stock(Product product) {
        this.product = product;
        this.productId = product.getId();
    }

    public Long getProductId() { return productId; }
    public int getQuantity() { return quantity; }

    public void add(int amount) { quantity = Math.addExact(quantity, amount); }
    public void remove(int amount) { quantity -= amount; }
}

package br.com.mvc.model.entity;

import java.math.BigDecimal;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private String emailCliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PedidoStatus status = PedidoStatus.ABERTO;

    private UUID faturaId;

    @Version
    private Long version;

    protected Pedido() {}

    public Pedido(Product product, int quantity, String emailCliente) {
        this.product = product;
        this.quantity = quantity;
        this.emailCliente = emailCliente;
        this.total = product.getPreco().multiply(BigDecimal.valueOf(quantity));
    }

    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public BigDecimal getTotal() { return total; }
    public String getEmailCliente() { return emailCliente; }
    public PedidoStatus getStatus() { return status; }
    public UUID getFaturaId() { return faturaId; }

    public void fechar(UUID faturaId) {
        if (status != PedidoStatus.ABERTO || faturaId == null)
            throw new IllegalStateException("Pedido não pode ser fechado.");
        this.faturaId = faturaId;
        this.status = PedidoStatus.FECHADO;
    }
}

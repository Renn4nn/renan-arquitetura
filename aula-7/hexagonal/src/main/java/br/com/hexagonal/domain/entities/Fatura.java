package br.com.hexagonal.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Fatura {
  private UUID id;
  private Long pedidoId;
  private BigDecimal valor;
  private String emailCliente;
  private LocalDateTime emitidoEm;
  private String codigoBarras;
  private String chaveAcesso;
  private String hashEmail;

  @Builder 
  private Fatura(
    UUID id,
    Long pedidoId,
    BigDecimal valor,
    String emailCliente,
    LocalDateTime emitidoEm,
    String codigoBarras,
    String chaveAcesso,
    String hashEmail
  ) {

    if (pedidoId == null || pedidoId <= 0) {
      throw new RuntimeException("A fatura precisa de um pedido!");
    }

    if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
      throw new RuntimeException("O valor da fatura deve ser maior que zero!");
    }

    if (emailCliente == null || !emailCliente.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
      throw new IllegalArgumentException("Informe o e-mail do cliente.");
    }

    this.id = id; 
    this.pedidoId = pedidoId; 
    this.valor = valor; 
    this.emailCliente = emailCliente;
    this.emitidoEm = emitidoEm; 
    this.codigoBarras = codigoBarras; 
    this.chaveAcesso = chaveAcesso;
    this.hashEmail = hashEmail;
  }

  public static Fatura criar(Long pedidoId, BigDecimal valor, String emailCliente) {
    return Fatura.builder()
      .id(UUID.randomUUID())
      .pedidoId(pedidoId)
      .valor(valor)
      .emailCliente(emailCliente)
      .emitidoEm(LocalDateTime.now())
      .build();
  }

  public void inserirBoleto(String codigoBarras) {
    if (codigoBarras == null || !codigoBarras.matches("\\d{44}")) {
      throw new IllegalArgumentException("O boleto deve ter código de barras de 44 dígitos.");
    }
    this.codigoBarras = codigoBarras;
  }

  public void inserirNota(String chaveAcesso) {
    if (chaveAcesso == null || !chaveAcesso.matches("\\d{44}")) {
      throw new IllegalArgumentException("A chave de acesso deve ter 44 dígitos.");
    }
    this.chaveAcesso = chaveAcesso;
  }

  public void inserirHashEmail(String hashEmail) {
    if (hashEmail == null || !hashEmail.matches("[0-9a-f]{64}")) {
      throw new IllegalArgumentException("Hash de e-mail inválido.");
    }
    this.hashEmail = hashEmail;
  }

}

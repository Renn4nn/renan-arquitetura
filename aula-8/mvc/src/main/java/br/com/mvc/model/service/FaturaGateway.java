package br.com.mvc.model.service;

import java.math.BigDecimal;
import java.util.UUID;

public interface FaturaGateway {
    UUID faturar(Long pedidoId, BigDecimal valor, String emailCliente);
}

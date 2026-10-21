package br.com.hexagonal.domain.in;

import java.math.BigDecimal;
import java.util.UUID;

public interface FaturaService {
  UUID fatura(Long pedidoId, BigDecimal valor);
}

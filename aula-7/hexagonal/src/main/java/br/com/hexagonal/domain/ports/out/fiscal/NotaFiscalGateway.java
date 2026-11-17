package br.com.hexagonal.domain.ports.out.fiscal;

import br.com.hexagonal.domain.entities.Fatura;

public interface NotaFiscalGateway {
  String emitirNota(Fatura fatura);
}

package br.com.hexagonal.infra.adapters.out.fiscal;

import org.springframework.stereotype.Component;
import br.com.hexagonal.domain.entities.Fatura;
import br.com.hexagonal.domain.ports.out.fiscal.NotaFiscalGateway;

@Component
public class SoapNotaFiscalSimulado implements NotaFiscalGateway {
  @Override
  public String emitirNota(Fatura fatura) {
    // Resposta simulada do emissor SOAP: chave numérica com 44 dígitos.
    return String.format("%044d", fatura.getId().getMostSignificantBits() & Long.MAX_VALUE);
  }
}

package br.com.hexagonal.application;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.hexagonal.domain.entities.Fatura;
import br.com.hexagonal.domain.ports.in.FaturaService;
import br.com.hexagonal.domain.ports.out.messaging.BoletoMessaging;
import br.com.hexagonal.domain.ports.out.fiscal.NotaFiscalGateway;
import br.com.hexagonal.domain.ports.out.notification.EmailNotifier;
import br.com.hexagonal.domain.ports.out.persistence.FaturaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class FaturaServiceImp implements FaturaService{

  private final FaturaRepository faturaRepository;
  private final BoletoMessaging boletoMessaging;
  private final NotaFiscalGateway notaFiscalGateway;
  private final EmailNotifier emailNotifier;

  @Override
  @org.springframework.transaction.annotation.Transactional
  public UUID faturarPedido(Long pedidoId, BigDecimal valor, String emailCliente) {
    Fatura fatura = Fatura.criar(pedidoId, valor, emailCliente);
    String codigoBarras = boletoMessaging.emitirBoleto(fatura);
    fatura.inserirBoleto(codigoBarras);
    String chaveAcesso = notaFiscalGateway.emitirNota(fatura);
    fatura.inserirNota(chaveAcesso);
    String hashEmail = emailNotifier.notificarCliente(fatura);
    fatura.inserirHashEmail(hashEmail);
    faturaRepository.salvar(fatura);
    return fatura.getId();
  }
  
}

package br.com.hexagonal.application;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.hexagonal.domain.entities.Fatura;
import br.com.hexagonal.domain.ports.in.FaturaService;
import br.com.hexagonal.domain.ports.out.messaging.BoletoMessaging;
import br.com.hexagonal.domain.ports.out.persistence.FaturaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class FaturaServiceImp implements FaturaService{

  private final FaturaRepository faturaRepository;
  // private final BoletoMessaging boletoMessaging;

  @Override
  public UUID faturarPedido(Long pedidoId, BigDecimal valor) {
    Fatura fatura = Fatura.criar(pedidoId, valor);
    // String codigoBarras = boletoMessaging.emitirBoleto(fatura);
    // fatura.inserirBoleto(codigoBarras);
    //TODO: EMITIR NOTA
    //TODO: DISPARAR EMAIL COM BOLETO E NOTA
    faturaRepository.salvar(fatura);
    return fatura.getId();
  }
  
}

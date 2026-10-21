package br.com.hexagonal.application;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.hexagonal.domain.entities.Fatura;
import br.com.hexagonal.domain.in.FaturaService;
import br.com.hexagonal.domain.out.persistence.FaturaRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FaturaServiceImp implements FaturaService {

  private final FaturaRepository faturaRepository;
  private final BoletoMessaging boletoMessaging;

  @Override
  public UUID faturaPedido(Long pedidoId, BigDecimal valor) {
    Fatura fatura = Fatura.criar(pedidoId, valor);
    String codigoBarras = boletoMessaging.emitirBoleto(fatura);
    fatura.inserirBoleto(codigoBarras);

    // TODO: GERAR BOLETO
    // TODO: EMITIR NOTA
    // TODO DISPARAR EMAIL COM BOLETO E NOTA
    faturaRepository.salvar(fatura);
    return fatura.getId();
  }
}

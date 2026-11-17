package br.com.hexagonal.infra.adapters.out.messaging;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.springframework.stereotype.Component;
import br.com.hexagonal.domain.entities.Fatura;
import br.com.hexagonal.domain.ports.out.messaging.BoletoMessaging;

@Component
public class FilaBoletoSimulada implements BoletoMessaging {
  private final Queue<Long> pedidos = new ConcurrentLinkedQueue<>();

  @Override
  public synchronized String emitirBoleto(Fatura fatura) {
    pedidos.add(fatura.getPedidoId());
    Long pedido = pedidos.poll(); // consumidor simulado; não há broker externo
    return String.format("%044d", pedido);
  }
}

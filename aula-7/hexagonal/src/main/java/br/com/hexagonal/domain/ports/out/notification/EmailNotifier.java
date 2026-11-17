package br.com.hexagonal.domain.ports.out.notification;

import br.com.hexagonal.domain.entities.Fatura;

public interface EmailNotifier {
  String notificarCliente(Fatura fatura);
}

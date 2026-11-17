package br.com.hexagonal.infra.adapters.out.notification;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import org.springframework.stereotype.Component;
import br.com.hexagonal.domain.entities.Fatura;
import br.com.hexagonal.domain.ports.out.notification.EmailNotifier;

@Component
public class SmtpEmailSimulado implements EmailNotifier {
  @Override
  public String notificarCliente(Fatura fatura) {
    String conteudo = "Para: " + fatura.getEmailCliente() + "; fatura " + fatura.getId()
        + ", pedido " + fatura.getPedidoId() + ", boleto " + fatura.getCodigoBarras()
        + ", nota " + fatura.getChaveAcesso();
    try {
      return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
          .digest(conteudo.getBytes(StandardCharsets.UTF_8)));
    } catch (NoSuchAlgorithmException ex) {
      throw new IllegalStateException("SHA-256 indisponível", ex);
    }
  }
}

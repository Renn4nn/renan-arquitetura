package br.com.mvc.integration;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import br.com.mvc.model.service.FaturaGateway;

@Component
public class HubFaturaClient implements FaturaGateway {
    private final RestClient client;

    public HubFaturaClient(@Value("${hub.base-url}") String baseUrl) {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(3000);
        this.client = RestClient.builder().baseUrl(baseUrl).requestFactory(factory).build();
    }

    @Override
    public UUID faturar(Long pedidoId, BigDecimal valor, String emailCliente) {
        try {
            FaturaResponse response = client.post().uri("/fatura")
                    .body(new FaturaRequest(pedidoId, valor, emailCliente))
                    .retrieve().body(FaturaResponse.class);
            if (response == null || response.faturaId() == null)
                throw new IllegalStateException("Hub não devolveu o ID da fatura.");
            return response.faturaId();
        } catch (RestClientException ex) {
            throw new IllegalStateException("Falha ao faturar o pedido no hub; o pedido permanece aberto.", ex);
        }
    }

    public record FaturaRequest(Long pedidoId, BigDecimal valor, String emailCliente) {}
    public record FaturaResponse(UUID faturaId) {}
}

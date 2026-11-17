package br.com.mvc;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import com.sun.net.httpserver.HttpServer;
import br.com.mvc.integration.HubFaturaClient;

class HubFaturaClientTests {
    @Test
    void enviaPedidoPorRestELeIdDaResposta() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        AtomicReference<String> received = new AtomicReference<>();
        server.createContext("/fatura", exchange -> {
            received.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            byte[] body = "{\"faturaId\":\"aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee\"}"
                    .getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(201, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
        server.start();
        try {
            HubFaturaClient client = new HubFaturaClient(
                    "http://127.0.0.1:" + server.getAddress().getPort());
            assertEquals("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee",
                    client.faturar(123L, new BigDecimal("50.00"), "cliente@exemplo.com").toString());
            assertTrue(received.get().contains("\"pedidoId\":123"));
            assertTrue(received.get().contains("\"valor\":50.00"));
            assertTrue(received.get().contains("\"emailCliente\":\"cliente@exemplo.com\""));
        } finally {
            server.stop(0);
        }
    }
}

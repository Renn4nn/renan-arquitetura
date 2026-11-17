package br.com.hexagonal;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import br.com.hexagonal.domain.ports.in.FaturaService;
import br.com.hexagonal.infra.adapters.out.persistence.jpa.FaturaEntityJpa;
import br.com.hexagonal.infra.adapters.out.persistence.jpa.FaturaRepositoryJpa;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:fatura-test;DB_CLOSE_DELAY=-1")
class FaturaFlowTests {
    @Autowired FaturaService service;
    @Autowired FaturaRepositoryJpa repository;

    @Test
    void criaFaturaComBoletoNotaEHash() {
        UUID id = service.faturarPedido(123L, new BigDecimal("50.00"), "cliente@exemplo.com");
        FaturaEntityJpa fatura = repository.findById(id).orElseThrow();
        assertEquals(123L, fatura.getPedidoId());
        assertEquals("cliente@exemplo.com", fatura.getEmailCliente());
        assertNotNull(fatura.getEmitidoEm());
        assertTrue(fatura.getCodigoBarras().matches("\\d{44}"));
        assertTrue(fatura.getChaveAcesso().matches("\\d{44}"));
        assertTrue(fatura.getHashEmail().matches("[0-9a-f]{64}"));
    }
}

package br.com.ccs.desafiojpadepositarsacar.components;

import br.com.ccs.desafiojpadepositarsacar.entities.UsuarioVersion;
import br.com.ccs.desafiojpadepositarsacar.factories.EntityTestFactory;
import br.com.ccs.desafiojpadepositarsacar.repostitories.UsuarioVersionRepository;
import br.com.ccs.desafiojpadepositarsacar.services.UsuarioVersionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
//@ActiveProfiles("test")
class DepositoComponentDepositoOtimistaTest {

    @Autowired
    private DepositoComponent depositoComponent;
    @Autowired
    private UsuarioVersionService usuarioVersionService;
    @Autowired
    private UsuarioVersionRepository repository;
    private UsuarioVersion usuario;

    @BeforeEach
    void setUp() {
        repository.deleteAllInBatch();
        usuario = EntityTestFactory.getUsuarioVersion();
    }

    @Test
    void testDepositarPessimista() {
        usuarioVersionService.save(usuario);
        depositoComponent.depositarOtimista(usuario, new BigDecimal(100));
        assertEquals(new BigDecimal(1100).setScale(2, RoundingMode.HALF_UP), usuarioVersionService.findById(usuario.getId()).getSaldo());
    }

    @Test
    void testDepositarPessimistaVariosDepositos() {
        usuarioVersionService.save(usuario);
        depositoComponent.depositarOtimista(usuario, new BigDecimal(100));
        depositoComponent.depositarOtimista(usuario, new BigDecimal(200));
        depositoComponent.depositarOtimista(usuario, new BigDecimal(300));
        assertEquals(new BigDecimal(1600).setScale(2, RoundingMode.HALF_UP), usuarioVersionService.findById(usuario.getId()).getSaldo());
    }
}
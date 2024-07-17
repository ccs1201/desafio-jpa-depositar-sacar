package br.com.ccs.desafiojpadepositarsacar.components;

import br.com.ccs.desafiojpadepositarsacar.entities.Usuario;
import br.com.ccs.desafiojpadepositarsacar.factories.EntityTestFactory;
import br.com.ccs.desafiojpadepositarsacar.repostitories.UsuarioRepository;
import br.com.ccs.desafiojpadepositarsacar.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
//@ActiveProfiles("test")
class DepositoComponentDepositoPessimistaTest {

    @Autowired
    private DepositoComponent depositoComponent;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository repository;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        repository.deleteAllInBatch();
        usuario = EntityTestFactory.getUsuario();
    }

    @Test
    void testDepositarPessimista() {
        usuarioService.save(usuario);
        depositoComponent.depositarPessimista(usuario, new BigDecimal(100));
        assertEquals(new BigDecimal(1100).setScale(2, RoundingMode.HALF_UP), usuarioService.findById(usuario.getId()).getSaldo());
    }
    @Test
    void testDepositarPessimistaVariosDepositos() {
        usuarioService.save(usuario);
        depositoComponent.depositarPessimista(usuario, new BigDecimal(100));
        depositoComponent.depositarPessimista(usuario, new BigDecimal(200));
        depositoComponent.depositarPessimista(usuario, new BigDecimal(300));
        assertEquals(new BigDecimal(1600).setScale(2, RoundingMode.HALF_UP), usuarioService.findById(usuario.getId()).getSaldo());
    }
}
package br.com.ccs.desafiojpadepositarsacar.components;

import br.com.ccs.desafiojpadepositarsacar.entities.Usuario;
import br.com.ccs.desafiojpadepositarsacar.entities.UsuarioVersion;
import br.com.ccs.desafiojpadepositarsacar.exceptions.DepositoException;
import br.com.ccs.desafiojpadepositarsacar.factories.EntityTestFactory;
import br.com.ccs.desafiojpadepositarsacar.repostitories.UsuarioRepository;
import br.com.ccs.desafiojpadepositarsacar.repostitories.UsuarioVersionRepository;
import br.com.ccs.desafiojpadepositarsacar.services.UsuarioService;
import br.com.ccs.desafiojpadepositarsacar.services.UsuarioVersionService;
import br.com.ccs.desafiojpadepositarsacar.utils.RunnableFutureTestHelper;
import org.flywaydb.core.internal.util.ExceptionUtils;
import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DespositoConcorrenteTest {

    @Autowired
    private DepositoComponent depositoComponent;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioVersionService usuarioVersionService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioVersionRepository usuarioVersionRepository;
    private Usuario usuario;
    private UsuarioVersion usuarioVersion;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAllInBatch();
        usuarioVersionRepository.deleteAllInBatch();
        usuario = EntityTestFactory.getUsuario();
        usuarioVersion = EntityTestFactory.getUsuarioVersion();
    }

    @Test
    void testeDepositarPessimistaConcorrenteSucesso() {
        usuario = usuarioService.save(usuario);
        assertDoesNotThrow(() -> RunnableFutureTestHelper.run(50, 5, () ->
                depositoComponent.depositarPessimista(usuario, BigDecimal.valueOf(10.00))));

        assertEquals(BigDecimal.valueOf(1500.00).setScale(2, RoundingMode.HALF_UP), usuarioService.findById(usuario.getId()).getSaldo());
    }


    @Test
    void testDepositarOtimistaDeveriaLancarSaqueException() {
        usuarioVersion = usuarioVersionService.save(usuarioVersion);
        var ex = assertThrows(CompletionException.class, () ->
                RunnableFutureTestHelper.run(2, 5, () -> depositoComponent.depositarOtimista(usuarioVersion, BigDecimal.valueOf(100.00))));

        assertEquals(BigDecimal.valueOf(1100).setScale(2, RoundingMode.HALF_UP), usuarioVersionService.findById(usuarioVersion.getId()).getSaldo());
        assertEquals(DepositoException.class, ex.getCause().getClass());
        assertEquals(StaleObjectStateException.class, ExceptionUtils.getRootCause(ex).getClass());
    }


    @Test
    @DisplayName("Teste concorrente de Deposito otimista, saldo inicial 1.000, realizamos 50 depositos de 10, total de 500, saldo final deveria ser 1500")
    void testeDepositarOtimistaConcorrenteSucesso() {
        usuarioVersion = usuarioVersionService.save(usuarioVersion);
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 50; i++) {
                depositarOtimista(i, BigDecimal.TEN);
            }
        });

        assertEquals(BigDecimal.valueOf(1500).setScale(2, RoundingMode.HALF_UP), usuarioVersionService.findById(usuarioVersion.getId()).getSaldo());
    }

    void depositarOtimista(int delay, BigDecimal valor) {
        RunnableFutureTestHelper.run(1, delay, () ->
                depositoComponent.depositarOtimista(usuarioVersion, valor));
    }
}

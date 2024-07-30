package br.com.ccs.desafiojpadepositarsacar.components;

import br.com.ccs.desafiojpadepositarsacar.entities.Usuario;
import br.com.ccs.desafiojpadepositarsacar.entities.UsuarioVersion;
import br.com.ccs.desafiojpadepositarsacar.exceptions.SaqueException;
import br.com.ccs.desafiojpadepositarsacar.factories.EntityTestFactory;
import br.com.ccs.desafiojpadepositarsacar.repostitories.UsuarioRepository;
import br.com.ccs.desafiojpadepositarsacar.repostitories.UsuarioVersionRepository;
import br.com.ccs.desafiojpadepositarsacar.services.UsuarioService;
import br.com.ccs.desafiojpadepositarsacar.services.UsuarioVersionService;
import br.com.ccs.desafiojpadepositarsacar.utils.TestContainerBase;
import br.com.ccs.desafiojpadepositarsacar.utils.RunnableFutureTestHelper;
import org.flywaydb.core.internal.util.ExceptionUtils;
import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.*;


class SaqueConcorrenteTest extends TestContainerBase {

    @Autowired
    private SaqueComponent saqueComponent;
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
    void testeSacarPessimistaConcorrenteSucesso() {
        usuario = usuarioService.save(usuario);
        assertDoesNotThrow(() -> RunnableFutureTestHelper.run(50, 5, () ->
                saqueComponent.sacarPessimista(usuario, BigDecimal.valueOf(10.00))));

        assertEquals(BigDecimal.valueOf(500.00).setScale(2, RoundingMode.HALF_UP), usuarioService.findById(usuario.getId()).getSaldo());
    }


    @Test
    void testSacarOtimistaDeveriaLancarSaqueException() {
        usuarioVersion = usuarioVersionService.save(usuarioVersion);
        var ex = assertThrows(CompletionException.class, () ->
                RunnableFutureTestHelper.run(2, 5, () -> saqueComponent.sacarOtimista(usuarioVersion, BigDecimal.valueOf(100.00))));

        assertEquals(BigDecimal.valueOf(900).setScale(2, RoundingMode.HALF_UP), usuarioVersionService.findById(usuarioVersion.getId()).getSaldo());
        assertEquals(SaqueException.class, ex.getCause().getClass());
        assertEquals(StaleObjectStateException.class, ExceptionUtils.getRootCause(ex).getClass());
    }


    @Test
    @DisplayName("Teste concorrente de saque otimista, saldo inicial 1.000, realizamos 50 saques de 10, total de 500, saldo final deveria ser 500")
    void testeSacarOtimistaConcorrenteSucesso() {
        usuarioVersion = usuarioVersionService.save(usuarioVersion);
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 50; i++) {
                sacarOtimista(i, BigDecimal.TEN);
            }
        });

        assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP), usuarioVersionService.findById(usuarioVersion.getId()).getSaldo());
    }

    @Test
    void testSacarOtimistaDeveriaLancarSaqueExceptionQuandoSaldoForInsuficiente() {
        usuarioVersion = usuarioVersionService.save(usuarioVersion);
        var ex = assertThrows(CompletionException.class, () -> {
            for (int i = 0; i < 101; i++) {
                sacarOtimista(i, BigDecimal.TEN);
            }
        });

        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP), usuarioVersionService.findById(usuarioVersion.getId()).getSaldo());
        assertEquals(SaqueException.class, ex.getCause().getClass());
        assertTrue(ex.getCause().getMessage().contains("Saldo insuficiente"));
    }


    void sacarOtimista(int delay, BigDecimal valor) {
        RunnableFutureTestHelper.run(1, delay, () ->
                saqueComponent.sacarOtimista(usuarioVersion, valor));
    }
}

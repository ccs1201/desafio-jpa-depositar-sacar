package br.com.ccs.desafiojpadepositarsacar.components;

import br.com.ccs.desafiojpadepositarsacar.constants.DesafioJpaConstants;
import br.com.ccs.desafiojpadepositarsacar.entities.Usuario;
import br.com.ccs.desafiojpadepositarsacar.exceptions.TransacaoFinanceiraException;
import br.com.ccs.desafiojpadepositarsacar.factories.EntityTestFactory;
import br.com.ccs.desafiojpadepositarsacar.repostitories.UsuarioRepository;
import br.com.ccs.desafiojpadepositarsacar.utils.RunnableFutureTestHelper;
import br.com.ccs.desafiojpadepositarsacar.utils.messageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class SaqueComponentTest {

    @Autowired
    private SaqueComponent saqueComponent;
    @Autowired
    private UsuarioRepository repository;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        repository.deleteAllInBatch();
        usuario = EntityTestFactory.getUsuario();
    }

    @Test
    void testSacarValorSaqueMaiorQueSaldoDeveriaLancarTransacaoFinanceiraException() {
        usuario = repository.save(usuario);
        var ex = assertThrows(TransacaoFinanceiraException.class, () -> saqueComponent.sacarPessimista(usuario, BigDecimal.valueOf(1000.01)));
        assertEquals(messageUtil.getMessage(DesafioJpaConstants.ERRO_SALDO_INSUFICIENTE), ex.getMessage());

        assertEquals(usuario.getSaldo().setScale(2), repository.findById(usuario.getId()).get().getSaldo());
    }

    @Test
    void testSacarValorSaqueMenorQueZeroDeveriaLancarTransacaoFinanceiraException() {
        usuario = repository.save(usuario);
        var ex = assertThrows(TransacaoFinanceiraException.class, () -> saqueComponent.sacarPessimista(usuario, BigDecimal.valueOf(-1000.01)));
        assertEquals(messageUtil.getMessage(DesafioJpaConstants.ERRO_VALOR_TRANSACAO_INVALIDO), ex.getMessage());

        assertEquals(usuario.getSaldo().setScale(2), repository.findById(usuario.getId()).get().getSaldo());
    }

    @Test
    void testSacarValorSaqueIgualValorSaldoDeveriaPassar() {
        repository.save(usuario);
        assertDoesNotThrow(() -> saqueComponent.sacarPessimista(usuario, BigDecimal.valueOf(1000.00)));
        assertEquals(BigDecimal.valueOf(0.0), usuario.getSaldo());
    }

    @Test
    void testSacarValorSaqueMenorQueValorSaldoDeveriaPassar() {
        repository.save(usuario);
        assertDoesNotThrow(() -> saqueComponent.sacarPessimista(usuario, BigDecimal.valueOf(999.99)));
        assertEquals(BigDecimal.valueOf(0.01), usuario.getSaldo());
    }

    @Test
    void testSacarValorSaqueMaiorQueZeroDeveriaPassar() {
        repository.save(usuario);
        assertDoesNotThrow(() -> saqueComponent.sacarPessimista(usuario, BigDecimal.valueOf(0.01)));
        assertEquals(BigDecimal.valueOf(999.99), usuario.getSaldo());
    }


    @Test
    void testSacarValorSaqueIgualZeroDeveriaLancarTransacaoFinanceiraException() {
        repository.save(usuario);
        var ex = assertThrows(TransacaoFinanceiraException.class, () -> saqueComponent.sacarPessimista(usuario, BigDecimal.ZERO));

        assertEquals(messageUtil.getMessage(DesafioJpaConstants.ERRO_VALOR_TRANSACAO_INVALIDO), ex.getMessage());
        assertEquals(BigDecimal.valueOf(1000), usuario.getSaldo());
    }

    @Test
    void testeMultiplosSacarConcorrenteDeveriaPassar() {
        repository.save(usuario);
        assertDoesNotThrow(() -> saqueComponent.sacarPessimista(usuario, BigDecimal.valueOf(500)));
        assertDoesNotThrow(() -> saqueComponent.sacarPessimista(usuario, BigDecimal.valueOf(500.00)));
        assertEquals(BigDecimal.ZERO.setScale(2), usuario.getSaldo().setScale(2));
    }

    @Test
    void testeSacarPessimistaConcorrenteSucesso() {
        usuario = repository.save(usuario);
        assertDoesNotThrow(() -> RunnableFutureTestHelper.run(10, 15, () -> saqueComponent.sacarPessimista(usuario, BigDecimal.valueOf(10.00))));

        usuario = repository.findById(usuario.getId()).get();

        assertEquals(BigDecimal.valueOf(900.00).setScale(2), usuario.getSaldo().setScale(2));
    }

    @Test
    void testeSacarOtimistaConcorrenteSucesso() {
        usuario = repository.save(usuario);
        assertDoesNotThrow(() -> RunnableFutureTestHelper.run(10, 15, () -> saqueComponent.sacarOtimista(usuario, BigDecimal.valueOf(10.00))));

        usuario = repository.findById(usuario.getId()).get();

        assertEquals(BigDecimal.valueOf(900.00).setScale(2), usuario.getSaldo().setScale(2));
    }
}
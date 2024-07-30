package br.com.ccs.desafiojpadepositarsacar.components;

import br.com.ccs.desafiojpadepositarsacar.constants.MessageConstants;
import br.com.ccs.desafiojpadepositarsacar.entities.Usuario;
import br.com.ccs.desafiojpadepositarsacar.exceptions.SaqueException;
import br.com.ccs.desafiojpadepositarsacar.factories.EntityTestFactory;
import br.com.ccs.desafiojpadepositarsacar.repostitories.UsuarioRepository;
import br.com.ccs.desafiojpadepositarsacar.services.UsuarioService;
import br.com.ccs.desafiojpadepositarsacar.utils.TestContainerBase;
import br.com.ccs.desafiojpadepositarsacar.utils.TranslationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class SaqueComponentSaquePessimistaTest extends TestContainerBase {

    @Autowired
    private SaqueComponent saqueComponent;
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
    void testSacarValorSaqueMaiorQueSaldoDeveriaLancarTransacaoFinanceiraException() {
        usuario = usuarioService.save(usuario);
        var ex = assertThrows(SaqueException.class, () -> saqueComponent.sacarPessimista(usuario, BigDecimal.valueOf(1000.01)));
        assertEquals(TranslationUtil.getMessage(MessageConstants.ERRO_SALDO_INSUFICIENTE), ex.getMessage());

        assertEquals(usuario.getSaldo().setScale(2, RoundingMode.HALF_UP), usuarioService.findById(usuario.getId()).getSaldo());
    }

    @Test
    void testSacarValorSaqueMenorQueZeroDeveriaLancarTransacaoFinanceiraException() {
        usuario = usuarioService.save(usuario);
        var ex = assertThrows(SaqueException.class, () -> saqueComponent.sacarPessimista(usuario, BigDecimal.valueOf(-1000.01)));
        assertEquals(TranslationUtil.getMessage(MessageConstants.ERRO_VALOR_TRANSACAO_INVALIDO), ex.getMessage());

        assertEquals(usuario.getSaldo().setScale(2, RoundingMode.HALF_UP), usuarioService.findById(usuario.getId()).getSaldo());
    }

    @Test
    void testSacarValorSaqueIgualValorSaldoDeveriaPassar() {
        usuario = usuarioService.save(usuario);
        assertDoesNotThrow(() -> saqueComponent.sacarPessimista(usuario, BigDecimal.valueOf(1000.00)));

        assertEquals(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP), usuarioService.findById(usuario.getId()).getSaldo());
    }

    @Test
    void testSacarValorSaqueMenorQueValorSaldoDeveriaPassar() {
        usuario = usuarioService.save(usuario);
        assertDoesNotThrow(() -> saqueComponent.sacarPessimista(usuario, BigDecimal.valueOf(999.99)));
        assertEquals(BigDecimal.valueOf(0.01), usuarioService.findById(usuario.getId()).getSaldo());
    }

    @Test
    void testSacarValorSaqueMaiorQueZeroDeveriaPassar() {
        usuario = usuarioService.save(usuario);
        assertDoesNotThrow(() -> saqueComponent.sacarPessimista(usuario, BigDecimal.valueOf(0.01)));
        assertEquals(BigDecimal.valueOf(999.99), usuarioService.findById(usuario.getId()).getSaldo());
    }


    @Test
    void testSacarValorSaqueIgualZeroDeveriaLancarTransacaoFinanceiraException() {
        usuario = usuarioService.save(usuario);
        var ex = assertThrows(SaqueException.class, () -> saqueComponent.sacarPessimista(usuario, BigDecimal.ZERO));

        assertEquals(TranslationUtil.getMessage(MessageConstants.ERRO_VALOR_TRANSACAO_INVALIDO), ex.getMessage());
        assertEquals(BigDecimal.valueOf(1000.00).setScale(2, RoundingMode.HALF_UP), usuarioService.findById(usuario.getId()).getSaldo());
    }

    @Test
    void testeMultiplosSacarPessimistaDeveriaPassar() {
        usuario = usuarioService.save(usuario);
        assertDoesNotThrow(() -> saqueComponent.sacarPessimista(usuario, BigDecimal.valueOf(500)));
        assertDoesNotThrow(() -> saqueComponent.sacarPessimista(usuario, BigDecimal.valueOf(500.00)));
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), usuarioService.findById(usuario.getId()).getSaldo().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void testeMultiplosSacarOtmistaDeveriaPassar() {
        usuario = usuarioService.save(usuario);
        assertDoesNotThrow(() -> saqueComponent.sacarPessimista(usuario, BigDecimal.valueOf(500)));
        assertDoesNotThrow(() -> saqueComponent.sacarPessimista(usuario, BigDecimal.valueOf(500.00)));
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), usuarioService.findById(usuario.getId()).getSaldo());
    }

}
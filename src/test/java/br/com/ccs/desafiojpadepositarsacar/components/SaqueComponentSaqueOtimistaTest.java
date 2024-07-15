package br.com.ccs.desafiojpadepositarsacar.components;

import br.com.ccs.desafiojpadepositarsacar.constants.MessageConstants;
import br.com.ccs.desafiojpadepositarsacar.entities.UsuarioVersion;
import br.com.ccs.desafiojpadepositarsacar.exceptions.SaqueException;
import br.com.ccs.desafiojpadepositarsacar.factories.EntityTestFactory;
import br.com.ccs.desafiojpadepositarsacar.repostitories.UsuarioVersionRepository;
import br.com.ccs.desafiojpadepositarsacar.services.UsuarioVersionService;
import br.com.ccs.desafiojpadepositarsacar.utils.TranslationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SaqueComponentSaqueOtimistaTest {

    @Autowired
    private SaqueComponent saqueComponent;
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
    void testSacarValorSaqueMaiorQueSaldoDeveriaLancarTransacaoFinanceiraException() {
        usuario = usuarioVersionService.save(usuario);
        var ex = assertThrows(SaqueException.class, () -> saqueComponent.sacarOtimista(usuario, BigDecimal.valueOf(1000.01)));
        assertEquals(TranslationUtil.getMessage(MessageConstants.ERRO_SALDO_INSUFICIENTE), ex.getMessage());

        assertEquals(usuario.getSaldo().setScale(2), usuarioVersionService.findById(usuario.getId()).getSaldo());
    }

    @Test
    void testSacarValorSaqueMenorQueZeroDeveriaLancarTransacaoFinanceiraException() {
        usuario = usuarioVersionService.save(usuario);
        var ex = assertThrows(SaqueException.class, () -> saqueComponent.sacarOtimista(usuario, BigDecimal.valueOf(-1000.01)));
        assertEquals(TranslationUtil.getMessage(MessageConstants.ERRO_VALOR_TRANSACAO_INVALIDO), ex.getMessage());

        assertEquals(usuario.getSaldo().setScale(2, RoundingMode.HALF_UP), usuarioVersionService.findById(usuario.getId()).getSaldo());
    }

    @Test
    void testSacarValorSaqueIgualValorSaldoDeveriaPassar() {
        usuario = usuarioVersionService.save(usuario);
        assertDoesNotThrow(() -> saqueComponent.sacarOtimista(usuario, BigDecimal.valueOf(1000.00)));
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.HALF_UP), usuarioVersionService.findById(usuario.getId()).getSaldo());
    }

    @Test
    void testSacarValorSaqueMenorQueValorSaldoDeveriaPassar() {
        usuario = usuarioVersionService.save(usuario);
        assertDoesNotThrow(() -> saqueComponent.sacarOtimista(usuario, BigDecimal.valueOf(999.99)));
        assertEquals(BigDecimal.valueOf(0.01), usuarioVersionService.findById(usuario.getId()).getSaldo());
    }

    @Test
    void testSacarValorSaqueMaiorQueZeroDeveriaPassar() {
        usuario = usuarioVersionService.save(usuario);
        assertDoesNotThrow(() -> saqueComponent.sacarOtimista(usuario, BigDecimal.valueOf(0.01)));
        assertEquals(BigDecimal.valueOf(999.99), usuarioVersionService.findById(usuario.getId()).getSaldo());
    }


    @Test
    void testSacarValorSaqueIgualZeroDeveriaLancarTransacaoFinanceiraException() {
        usuario = usuarioVersionService.save(usuario);
        var ex = assertThrows(SaqueException.class, () -> saqueComponent.sacarOtimista(usuario, BigDecimal.ZERO));

        assertEquals(TranslationUtil.getMessage(MessageConstants.ERRO_VALOR_TRANSACAO_INVALIDO), ex.getMessage());
        assertEquals(BigDecimal.valueOf(1000).setScale(2, RoundingMode.HALF_UP), usuarioVersionService.findById(usuario.getId()).getSaldo());
    }

    @Test
    void testeMultiplosSaquesDeveriaPassar() {
        usuario = usuarioVersionService.save(usuario);
        assertDoesNotThrow(() -> saqueComponent.sacarOtimista(usuario, BigDecimal.valueOf(500)));
        assertDoesNotThrow(() -> saqueComponent.sacarOtimista(usuario, BigDecimal.valueOf(500.00)));

        usuario = usuarioVersionService.findById(usuario.getId());

        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), usuarioVersionService.findById(usuario.getId()).getSaldo());
    }
}
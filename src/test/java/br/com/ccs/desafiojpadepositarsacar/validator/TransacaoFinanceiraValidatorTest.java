package br.com.ccs.desafiojpadepositarsacar.validator;

import br.com.ccs.desafiojpadepositarsacar.constants.MessageConstants;
import br.com.ccs.desafiojpadepositarsacar.exceptions.TransacaoFinanceiraException;
import br.com.ccs.desafiojpadepositarsacar.utils.TranslationUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransacaoFinanceiraValidatorTest {

    @Test
    void testValidarSaldoQuandoValorTransacaoMenorQueValorDebito() {
        var ex = assertThrows(TransacaoFinanceiraException.class, () ->
                TransacaoFinanceiraValidator.validarValorTransacao(BigDecimal.ZERO, BigDecimal.valueOf(1000)));

        assertEquals(TranslationUtil.getMessage(MessageConstants.ERRO_SALDO_INSUFICIENTE), ex.getMessage());
    }

    @Test
    void testValidarSaldoQuandoValorTransacaoMaiorQueValorDebito() {
        assertDoesNotThrow(() ->
                TransacaoFinanceiraValidator.validarValorTransacao(BigDecimal.valueOf(1000), BigDecimal.TEN));
    }

    @Test
    void testValidarValorTransacaoQuandoValorDebitoIgualAZero() {
        var ex = assertThrows(TransacaoFinanceiraException.class, () ->
                TransacaoFinanceiraValidator.validarValorTransacao(BigDecimal.TEN, BigDecimal.ZERO));

        assertEquals(TranslationUtil.getMessage(MessageConstants.ERRO_VALOR_TRANSACAO_INVALIDO), ex.getMessage());
    }

    @Test
    void testValidarValorTransacaoQuandoValorDebitoMenorQueZero() {
        var ex = assertThrows(TransacaoFinanceiraException.class, () ->
                TransacaoFinanceiraValidator.validarValorTransacao(BigDecimal.TEN, BigDecimal.valueOf(-0.01)));

        assertEquals(TranslationUtil.getMessage(MessageConstants.ERRO_VALOR_TRANSACAO_INVALIDO), ex.getMessage());
    }
}
package br.com.ccs.desafiojpadepositarsacar.validator;

import br.com.ccs.desafiojpadepositarsacar.constants.DesafioJpaConstants;
import br.com.ccs.desafiojpadepositarsacar.exceptions.TransacaoFinanceiraException;
import br.com.ccs.desafiojpadepositarsacar.utils.messageUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SaldoValidatorTest {

    @Test
    void testValidarSaldoQuandoValorTransacaoMenorQueValorDebito() {
        var ex = assertThrows(TransacaoFinanceiraException.class, () ->
                SaldoValidator.validarValorTransacao(BigDecimal.ZERO, BigDecimal.valueOf(1000)));

        assertEquals(messageUtil.getMessage(DesafioJpaConstants.ERRO_SALDO_INSUFICIENTE), ex.getMessage());
    }

    @Test
    void testValidarSaldoQuandoValorTransacaoMaiorQueValorDebito() {
        assertDoesNotThrow(() ->
                SaldoValidator.validarValorTransacao(BigDecimal.valueOf(1000), BigDecimal.TEN));
    }

    @Test
    void testValidarValorTransacaoQuandoValorDebitoIgualAZero() {
        var ex = assertThrows(TransacaoFinanceiraException.class, () ->
                SaldoValidator.validarValorTransacao(BigDecimal.TEN, BigDecimal.ZERO));

        assertEquals(messageUtil.getMessage(DesafioJpaConstants.ERRO_VALOR_TRANSACAO_INVALIDO), ex.getMessage());
    }

    @Test
    void testValidarValorTransacaoQuandoValorDebitoMenorQueZero() {
        var ex = assertThrows(TransacaoFinanceiraException.class, () ->
                SaldoValidator.validarValorTransacao(BigDecimal.TEN, BigDecimal.valueOf(-0.01)));

        assertEquals(messageUtil.getMessage(DesafioJpaConstants.ERRO_VALOR_TRANSACAO_INVALIDO), ex.getMessage());
    }
}
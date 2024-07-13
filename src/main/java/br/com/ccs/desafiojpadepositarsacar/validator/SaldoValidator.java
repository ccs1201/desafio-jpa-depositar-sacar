package br.com.ccs.desafiojpadepositarsacar.validator;

import br.com.ccs.desafiojpadepositarsacar.constants.DesafioJpaConstants;
import br.com.ccs.desafiojpadepositarsacar.exceptions.TransacaoFinanceiraException;
import br.com.ccs.desafiojpadepositarsacar.utils.messageUtil;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class SaldoValidator {

    public static void validarValorTransacao(BigDecimal saldo, BigDecimal valorDebito) {
        if (saldo.compareTo(valorDebito) < 0) {
            throw new TransacaoFinanceiraException(
                    messageUtil.getMessage(DesafioJpaConstants.ERRO_SALDO_INSUFICIENTE));
        }

        if (valorDebito.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransacaoFinanceiraException(
                    messageUtil.getMessage(DesafioJpaConstants.ERRO_VALOR_TRANSACAO_INVALIDO));
        }
    }
}

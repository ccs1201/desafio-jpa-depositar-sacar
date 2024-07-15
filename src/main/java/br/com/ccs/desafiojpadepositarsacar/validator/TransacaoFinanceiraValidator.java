package br.com.ccs.desafiojpadepositarsacar.validator;

import br.com.ccs.desafiojpadepositarsacar.constants.MessageConstants;
import br.com.ccs.desafiojpadepositarsacar.exceptions.TransacaoFinanceiraException;
import br.com.ccs.desafiojpadepositarsacar.utils.TranslationUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@UtilityClass
@Slf4j
public class TransacaoFinanceiraValidator {

    public static void validarValorTransacao(BigDecimal saldo, BigDecimal valorDebito) {
        log.debug("Validando valor da transação. Saldo {}, Valor Debito {}", saldo, valorDebito);
        if (saldo.compareTo(valorDebito) < 0) {
            throw new TransacaoFinanceiraException(
                    TranslationUtil.getMessage(MessageConstants.ERRO_SALDO_INSUFICIENTE));
        }

        if (valorDebito.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransacaoFinanceiraException(
                    TranslationUtil.getMessage(MessageConstants.ERRO_VALOR_TRANSACAO_INVALIDO));
        }
    }
}

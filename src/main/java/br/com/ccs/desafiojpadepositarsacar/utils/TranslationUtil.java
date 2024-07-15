package br.com.ccs.desafiojpadepositarsacar.utils;

import br.com.ccs.desafiojpadepositarsacar.constants.MessageConstants;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TranslationUtil {

    public static String getMessage(MessageConstants constante) {
        return constante.getMensagem();
    }

    public static String getFormattedMessage(MessageConstants constante, Object... args) {
        return String.format(constante.getMensagem(), args);
    }
}
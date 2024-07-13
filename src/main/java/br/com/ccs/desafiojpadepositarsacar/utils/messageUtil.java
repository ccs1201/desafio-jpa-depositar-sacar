package br.com.ccs.desafiojpadepositarsacar.utils;

import br.com.ccs.desafiojpadepositarsacar.constants.DesafioJpaConstants;

public class messageUtil {

    public static String getMessage(DesafioJpaConstants constante) {
        return constante.getMensagem();
    }

    public static String getFormattedMessage(DesafioJpaConstants constante, Object... args) {
        return String.format(constante.getMensagem(), args);
    }
}
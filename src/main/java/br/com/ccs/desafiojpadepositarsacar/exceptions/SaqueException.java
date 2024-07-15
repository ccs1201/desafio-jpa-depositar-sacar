package br.com.ccs.desafiojpadepositarsacar.exceptions;

public class SaqueException extends DesafioJpaBaseException {
    public SaqueException(String message) {
        super(message);
    }

    public SaqueException(String message, Throwable cause) {
        super(message, cause);
    }
}

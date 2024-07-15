package br.com.ccs.desafiojpadepositarsacar.exceptions;

public class DesafioJpaBaseException extends RuntimeException {

    public DesafioJpaBaseException(String message) {
        super(message);
    }

    public DesafioJpaBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

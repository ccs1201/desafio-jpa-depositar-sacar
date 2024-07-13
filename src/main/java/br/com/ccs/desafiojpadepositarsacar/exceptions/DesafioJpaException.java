package br.com.ccs.desafiojpadepositarsacar.exceptions;

public class DesafioJpaException extends RuntimeException {

    public DesafioJpaException(String message) {
        super(message);
    }

    public DesafioJpaException(String message, Throwable cause) {
        super(message, cause);
    }
}

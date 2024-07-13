package br.com.ccs.desafiojpadepositarsacar.exceptions;

public class DesafioJpaServiceException extends DesafioJpaException {
    public DesafioJpaServiceException(String message) {
        super(message);
    }

    public DesafioJpaServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

package br.com.ccs.desafiojpadepositarsacar.exceptions;

public class DesafioJpaSaqueException extends DesafioJpaException {
    public DesafioJpaSaqueException(String message) {
        super(message);
    }

    public DesafioJpaSaqueException(String message, Throwable cause) {
        super(message, cause);
    }
}

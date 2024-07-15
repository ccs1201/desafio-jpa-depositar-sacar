package br.com.ccs.desafiojpadepositarsacar.exceptions;

public class DepositoException extends DesafioJpaBaseException {

    public DepositoException(String message) {
        super(message);
    }

    public DepositoException(String message, Throwable cause) {
        super(message, cause);
    }
}

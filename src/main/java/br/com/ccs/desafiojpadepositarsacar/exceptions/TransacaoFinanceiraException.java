package br.com.ccs.desafiojpadepositarsacar.exceptions;

public class TransacaoFinanceiraException extends DesafioJpaBaseException {
    public TransacaoFinanceiraException(String message) {
        super(message);
    }

    public TransacaoFinanceiraException(String message, Throwable cause) {
        super(message, cause);
    }
}

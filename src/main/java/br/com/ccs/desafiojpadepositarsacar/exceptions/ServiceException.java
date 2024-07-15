package br.com.ccs.desafiojpadepositarsacar.exceptions;

public class ServiceException extends DesafioJpaBaseException {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

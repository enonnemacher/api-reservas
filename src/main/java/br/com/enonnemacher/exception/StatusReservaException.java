package br.com.enonnemacher.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StatusReservaException extends Exception {
    public StatusReservaException(String tipo, String status) {
        super(String.format("Não é possível realizar o %s para esta reserva, pois ela não está no status %s.", tipo, status));
    }
}

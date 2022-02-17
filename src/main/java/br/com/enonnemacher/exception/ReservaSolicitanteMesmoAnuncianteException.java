package br.com.enonnemacher.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReservaSolicitanteMesmoAnuncianteException extends Exception {
    public ReservaSolicitanteMesmoAnuncianteException() {
        super("O solicitante de uma reserva não pode ser o próprio anunciante.");
    }
}

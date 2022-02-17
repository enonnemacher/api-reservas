package br.com.enonnemacher.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReservaMinimaException extends Exception {
    public ReservaMinimaException(Integer valor, String tipo, String campo) {
        super(String.format("Não é possivel realizar uma reserva com menos de %s %s para imóveis do tipo %s", valor, tipo, campo));
    }
}

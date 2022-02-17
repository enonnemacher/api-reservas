package br.com.enonnemacher.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImovelComReservaAtivaException extends Exception {
    public ImovelComReservaAtivaException() {
        super("Este anuncio já esta reservado para o período informado.");
    }
}

package br.com.enonnemacher.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CampoDuplicadoStringException extends Exception {
    public CampoDuplicadoStringException(String tipo, String campo, String valor) {
        super(String.format("Já existe um recurso do tipo %s com %s com o valor '%s'.", tipo, campo, valor));
    }
}

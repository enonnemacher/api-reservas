package br.com.enonnemacher.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NaoEncontradoException extends Exception {
    public NaoEncontradoException(String tipo, String campo, String valor) {
        super(String.format("Nenhum(a) %s com %s com o valor '%s' foi encontrado.", tipo, campo, valor));
    }
}

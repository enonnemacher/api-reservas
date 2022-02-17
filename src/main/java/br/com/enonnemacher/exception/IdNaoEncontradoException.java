package br.com.enonnemacher.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IdNaoEncontradoException extends Exception {
    public IdNaoEncontradoException(String tipo, String campo, Long valor) {
        super(String.format("Nenhum(a) %s com %s com o valor '%d' foi encontrado.", tipo, campo, valor));
    }
}

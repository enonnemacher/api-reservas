package br.com.enonnemacher.exception;

import br.com.enonnemacher.domain.FormaPagamento;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FormaPagamentoNaoAceitaException extends Exception {
    public FormaPagamentoNaoAceitaException(FormaPagamento formaPagamento, String formasAceitas) {
        super(String.format("O anúncio não aceita %s como forma de pagamento. As formas aceitas são %s.", formaPagamento, formasAceitas));
    }
}

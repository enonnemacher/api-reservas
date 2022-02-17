package br.com.enonnemacher.response;

import br.com.enonnemacher.domain.FormaPagamento;
import br.com.enonnemacher.domain.Imovel;
import br.com.enonnemacher.domain.Usuario;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DadosAnuncioResponse {

    private Long idAnuncio;
    private Imovel imovel;
    private Usuario anunciante;
    private List<FormaPagamento> formasAceitas;
    private String descricao;
}

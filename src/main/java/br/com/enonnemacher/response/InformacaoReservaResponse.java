package br.com.enonnemacher.response;

import br.com.enonnemacher.domain.Pagamento;
import br.com.enonnemacher.domain.Periodo;
import lombok.*;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InformacaoReservaResponse {

    private Long idReserva;
    private DadosSolicitanteResponse solicitante;
    private Integer quantidadePessoas;
    private DadosAnuncioResponse anuncio;
    private Periodo periodo;
    private Pagamento pagamento;
}

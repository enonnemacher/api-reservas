package br.com.enonnemacher.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DadosSolicitanteResponse {

    private Long idSolicitante;
    private String nomeSolicitante;
}

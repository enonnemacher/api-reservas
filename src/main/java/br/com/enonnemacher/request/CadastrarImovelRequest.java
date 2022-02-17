package br.com.enonnemacher.request;

import br.com.enonnemacher.domain.CaracteristicaImovel;
import br.com.enonnemacher.domain.Endereco;
import br.com.enonnemacher.domain.TipoImovel;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarImovelRequest {

    @NotNull(message = "Campo obrigatório não informado. Favor informar o campo tipoImovel.")
    private TipoImovel tipoImovel;

    @Valid
    @NotNull(message = "Campo obrigatório não informado. Favor informar o campo endereco.")
    private Endereco endereco;

    @NotBlank(message = "Campo obrigatório não informado. Favor informar o campo identificacao.")
    private String identificacao;

    @NotNull(message = "Campo obrigatório não informado. Favor informar o campo id_proprietario.")
    private Long idProprietario;

    private List<CaracteristicaImovel> caracteristicas;
}

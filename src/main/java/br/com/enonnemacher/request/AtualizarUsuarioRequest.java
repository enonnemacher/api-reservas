package br.com.enonnemacher.request;

import br.com.enonnemacher.domain.Endereco;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarUsuarioRequest {

    @NotBlank
    private String nome;

    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    @NotNull
    private LocalDate dataNascimento;

    @Valid
    private Endereco endereco;
}

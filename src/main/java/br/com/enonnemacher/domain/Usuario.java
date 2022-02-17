package br.com.enonnemacher.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario {

    public static final String REGEX_CPF = "[0-9]{11}";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String email;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // ocultar o campo senha na resposta Json
    private String senha;

    @NotBlank
    @Pattern(regexp = REGEX_CPF, message = "O CPF deve ser informado no formato 99999999999.")
    private String cpf;

    @NotNull
    private LocalDate dataNascimento;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_endereco")
    @Valid
    private Endereco endereco;

//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    private String imagemAvatar;
}

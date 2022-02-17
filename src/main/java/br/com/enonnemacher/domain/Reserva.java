package br.com.enonnemacher.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Service
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_solicitante")
    @NotNull(message = "Campo obrigatório não informado. Favor informar o campo solicitante.")
    private Usuario solicitante;

    @ManyToOne
    @JoinColumn(name = "id_anuncio")
    @NotNull(message = "Campo obrigatório não informado. Favor informar o campo anuncio.")
    private Anuncio anuncio;

    @NotNull(message = "Campo obrigatório não informado. Favor informar o campo periodo.")
    @Embedded
    private Periodo periodo;

    @NotNull(message = "Campo obrigatório não informado. Favor informar o campo quantidadePessoas.")
    private Integer quantidadePessoas;

    @NotNull(message = "Campo obrigatório não informado. Favor informar o campo dataHoraReserva.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraReserva;

    @Embedded
    private Pagamento pagamento;
}

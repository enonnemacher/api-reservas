package br.com.enonnemacher.service.reserva;

import br.com.enonnemacher.domain.Periodo;
import br.com.enonnemacher.domain.Reserva;
import br.com.enonnemacher.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class ListarReservaPorIdSolicitanteService {

    @Autowired
    private ReservaRepository reservaRepository;

    public Page<Reserva> listarReservasPorIdSolicitante(Pageable pageable, Long idSolicitante, Periodo periodo) {
        if (periodo != null) {
            if (periodo.getDataHoraInicial() != null && periodo.getDataHoraFinal() != null) {
                alterarPeriodoParaPadrao(periodo);
                return reservaRepository.findAllBySolicitanteIdEqualsAndPeriodoDataHoraInicialGreaterThanEqualAndPeriodoDataHoraFinalLessThanEqual(idSolicitante, periodo.getDataHoraInicial(), periodo.getDataHoraFinal(), pageable);
            }
        }
        return reservaRepository.findAllBySolicitanteIdEquals(idSolicitante, pageable);
    }

    private void alterarPeriodoParaPadrao(Periodo periodo) {
        LocalDate dataInicial = LocalDate.from(periodo.getDataHoraInicial());
        LocalDate dataFinal = LocalDate.from(periodo.getDataHoraFinal());
        LocalTime horaInicial = LocalTime.of(14, 00, 00);
        LocalTime horaFinal = LocalTime.of(12, 00, 00);
        periodo.setDataHoraInicial(LocalDateTime.of(dataInicial, horaInicial));
        periodo.setDataHoraFinal(LocalDateTime.of(dataFinal, horaFinal));
    }
}

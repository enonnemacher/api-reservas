package br.com.enonnemacher.repository;

import br.com.enonnemacher.domain.Reserva;
import br.com.enonnemacher.domain.StatusPagamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends PagingAndSortingRepository<Reserva, Long> {
    // 4.1
    List<Reserva> findAllByAnuncioIdAndPeriodoDataHoraInicialLessThanEqualAndPeriodoDataHoraFinalGreaterThanEqualAndPagamentoStatusOrAnuncioIdAndPeriodoDataHoraInicialLessThanEqualAndPeriodoDataHoraFinalGreaterThanEqualAndPagamentoStatus(Long idAnuncio, LocalDateTime dataHoraFinal, LocalDateTime dataHoraInicial, StatusPagamento pendente, Long idAnuncio1, LocalDateTime dataHoraFinal1, LocalDateTime dataHoraInicial1, StatusPagamento pago);

    // 4.2
    Page<Reserva> findAllBySolicitanteIdEqualsAndPeriodoDataHoraInicialGreaterThanEqualAndPeriodoDataHoraFinalLessThanEqual(Long idSolicitante, LocalDateTime dataHoraInicial, LocalDateTime dataHoraFinal, Pageable pageable);

    Page<Reserva> findAllBySolicitanteIdEquals(Long idSolicitante, Pageable pageable);

    //4.3
    Page<Reserva> findAllByAnuncioAnuncianteIdEquals(Pageable pageable, Long idAnunciante);

    // 4.4
    Optional<Reserva> findById(Long idReserva);
}

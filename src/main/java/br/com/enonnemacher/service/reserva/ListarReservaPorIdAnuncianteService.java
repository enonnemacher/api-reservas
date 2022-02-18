package br.com.enonnemacher.service.reserva;

import br.com.enonnemacher.domain.Reserva;
import br.com.enonnemacher.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ListarReservaPorIdAnuncianteService {

    @Autowired
    private ReservaRepository reservaRepository;

    public Page<Reserva> listarReservasPorIdAnunciante(Pageable pageable, Long idAnunciante) {
        Page<Reserva> listaReservas = reservaRepository.findAllByAnuncioAnuncianteIdEquals(pageable, idAnunciante);
        return listaReservas;
    }
}

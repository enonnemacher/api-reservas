package br.com.enonnemacher.service.reserva;

import br.com.enonnemacher.domain.Reserva;
import br.com.enonnemacher.exception.NaoEncontradoLongException;
import br.com.enonnemacher.exception.TipoDominioException;
import br.com.enonnemacher.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListarReservaPorIdService {

    @Autowired
    private ReservaRepository reservaRepository;

    public Reserva consultarReservaId(Long idReserva) throws NaoEncontradoLongException {
        return reservaRepository.findById(idReserva).orElseThrow(()
                -> new NaoEncontradoLongException(TipoDominioException.Reserva.getTipo(), TipoDominioException.Id.getTipo(), idReserva));
    }
}

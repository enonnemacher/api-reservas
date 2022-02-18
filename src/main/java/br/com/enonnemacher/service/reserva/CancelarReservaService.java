package br.com.enonnemacher.service.reserva;

import br.com.enonnemacher.domain.Pagamento;
import br.com.enonnemacher.domain.Reserva;
import br.com.enonnemacher.domain.StatusPagamento;
import br.com.enonnemacher.exception.NaoEncontradoLongException;
import br.com.enonnemacher.exception.StatusReservaException;
import br.com.enonnemacher.exception.TipoDominioException;
import br.com.enonnemacher.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CancelarReservaService {

    @Autowired
    private ListarReservaPorIdService listarReservaPorIdService;

    @Autowired
    private ReservaRepository reservaRepository;

    public void cancelarReserva(Long idReserva) throws NaoEncontradoLongException, StatusReservaException {
        Reserva reserva = listarReservaPorIdService.consultarReservaId(idReserva);

        if (reserva.getPagamento().getStatus().equals(StatusPagamento.PAGO) ||
                reserva.getPagamento().getStatus().equals(StatusPagamento.ESTORNADO) ||
                reserva.getPagamento().getStatus().equals(StatusPagamento.CANCELADO)) {
            throw new StatusReservaException(TipoDominioException.Cancelamento.getTipo(), TipoDominioException.Pendente.getTipo());
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setValorTotal(reserva.getPagamento().getValorTotal());
        pagamento.setStatus(StatusPagamento.CANCELADO);
        pagamento.setFormaEscolhida(reserva.getPagamento().getFormaEscolhida());

        reserva.setPagamento(pagamento);

        reservaRepository.save(reserva);
    }
}

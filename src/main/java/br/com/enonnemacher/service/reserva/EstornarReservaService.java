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
public class EstornarReservaService {

    @Autowired
    private ListarReservaPorIdService listarReservaPorIdService;

    @Autowired
    private ReservaRepository reservaRepository;

    public void estornarReserva(Long idReserva) throws NaoEncontradoLongException, StatusReservaException {
        Reserva reserva = listarReservaPorIdService.consultarReservaId(idReserva);

        if (reserva.getPagamento().getStatus().equals(StatusPagamento.PENDENTE) ||
                reserva.getPagamento().getStatus().equals(StatusPagamento.ESTORNADO) ||
                reserva.getPagamento().getStatus().equals(StatusPagamento.CANCELADO)) {
            throw new StatusReservaException(TipoDominioException.Estorno.getTipo(), TipoDominioException.Pago.getTipo());
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setValorTotal(reserva.getPagamento().getValorTotal());
        pagamento.setStatus(StatusPagamento.ESTORNADO);
        pagamento.setFormaEscolhida(null);

        reserva.setPagamento(pagamento);

        reservaRepository.save(reserva);
    }
}

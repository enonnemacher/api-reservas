package br.com.enonnemacher.service.reserva;

import br.com.enonnemacher.domain.FormaPagamento;
import br.com.enonnemacher.domain.Pagamento;
import br.com.enonnemacher.domain.Reserva;
import br.com.enonnemacher.domain.StatusPagamento;
import br.com.enonnemacher.exception.FormaPagamentoNaoAceitaException;
import br.com.enonnemacher.exception.NaoEncontradoLongException;
import br.com.enonnemacher.exception.StatusReservaException;
import br.com.enonnemacher.exception.TipoDominioException;
import br.com.enonnemacher.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagarReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ListarReservaPorIdService listarReservaPorIdService;

    public void pagarReserva(Long idReserva, FormaPagamento formaPagamento) throws
            NaoEncontradoLongException, StatusReservaException, FormaPagamentoNaoAceitaException {
        Reserva reserva = listarReservaPorIdService.consultarReservaId(idReserva);

        List<FormaPagamento> formas = reserva.getAnuncio().getFormasAceitas();
        for (int i = 0; i < formas.size(); i++) {
            if (!formas.contains(formaPagamento)) {
                throw new FormaPagamentoNaoAceitaException(formaPagamento,
                        reserva.getAnuncio().getFormasAceitas().toString().replaceAll("\\[", "").replaceAll("\\]", ""));
            }
        }

        if (reserva.getPagamento().getStatus().equals(StatusPagamento.PAGO) ||
                reserva.getPagamento().getStatus().equals(StatusPagamento.ESTORNADO) ||
                reserva.getPagamento().getStatus().equals(StatusPagamento.CANCELADO)) {
            throw new StatusReservaException(TipoDominioException.Pagamento.getTipo(), TipoDominioException.Pendente.getTipo());
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setValorTotal(reserva.getPagamento().getValorTotal());
        pagamento.setStatus(StatusPagamento.PAGO);
        pagamento.setFormaEscolhida(formaPagamento);

        reserva.setPagamento(pagamento);

        reservaRepository.save(reserva);
    }
}

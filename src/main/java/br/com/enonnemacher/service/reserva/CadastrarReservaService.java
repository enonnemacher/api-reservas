package br.com.enonnemacher.service.reserva;

import br.com.enonnemacher.domain.*;
import br.com.enonnemacher.exception.*;
import br.com.enonnemacher.repository.ReservaRepository;
import br.com.enonnemacher.request.CadastrarReservaRequest;
import br.com.enonnemacher.response.DadosAnuncioResponse;
import br.com.enonnemacher.response.DadosSolicitanteResponse;
import br.com.enonnemacher.response.InformacaoReservaResponse;
import br.com.enonnemacher.service.anuncio.ListarAnuncioPorIdService;
import br.com.enonnemacher.service.usuario.ListarUsuarioPorIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class CadastrarReservaService {

    @Autowired
    private ListarAnuncioPorIdService listarAnuncioPorIdService;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ListarUsuarioPorIdService listarUsuarioPorIdService;

    public InformacaoReservaResponse salvar(CadastrarReservaRequest cadastrarReservaRequest) throws IdNaoEncontradoException, ReservaSolicitanteMesmoAnuncianteException, ReservaMenorQueUmDiaException, DataFimReservaMaiorInicialException, ReservaMinimaException, ImovelComReservaAtivaException {
        if (cadastrarReservaRequest.getIdSolicitante()
                .equals(listarAnuncioPorIdService.consultarAnuncioID(cadastrarReservaRequest.getIdAnuncio()).getAnunciante().getId())) {
            throw new ReservaSolicitanteMesmoAnuncianteException();
        }

        LocalDate dataInicial = LocalDate.from(cadastrarReservaRequest.getPeriodo().getDataHoraInicial());
        LocalDate dataFinal = LocalDate.from(cadastrarReservaRequest.getPeriodo().getDataHoraFinal());
        LocalTime horaInicial = LocalTime.of(14, 00, 00);
        LocalTime horaFinal = LocalTime.of(12, 00, 00);
        BigDecimal diarias = BigDecimal.valueOf(dataFinal.getDayOfYear() - dataInicial.getDayOfYear());
        Integer numeroDiarias = dataFinal.getDayOfYear() - dataInicial.getDayOfYear();

        Periodo periodo = new Periodo();
        periodo.setDataHoraInicial(LocalDateTime.of(dataInicial, horaInicial));
        periodo.setDataHoraFinal(LocalDateTime.of(dataFinal, horaFinal));

        List<Reserva> listaReservas = reservaRepository.findAllByAnuncioIdAndPeriodoDataHoraInicialLessThanEqualAndPeriodoDataHoraFinalGreaterThanEqualAndPagamentoStatusOrAnuncioIdAndPeriodoDataHoraInicialLessThanEqualAndPeriodoDataHoraFinalGreaterThanEqualAndPagamentoStatus(
                cadastrarReservaRequest.getIdAnuncio(),
                periodo.getDataHoraFinal(),
                periodo.getDataHoraInicial(),
                StatusPagamento.PENDENTE,
                cadastrarReservaRequest.getIdAnuncio(),
                periodo.getDataHoraFinal(),
                periodo.getDataHoraInicial(),
                StatusPagamento.PAGO);
        if (listaReservas.size() != 0) {
            throw new ImovelComReservaAtivaException();
        }

        if (dataFinal.getDayOfYear() < dataInicial.getDayOfYear()) {
            throw new DataFimReservaMaiorInicialException();
        }

        if ((dataFinal.getDayOfYear() - dataInicial.getDayOfYear()) < 1) {
            throw new ReservaMenorQueUmDiaException();
        }

        if (listarAnuncioPorIdService.consultarAnuncioID(cadastrarReservaRequest.getIdAnuncio()).getImovel()
                .getTipoImovel().equals(TipoImovel.HOTEL) && cadastrarReservaRequest.getQuantidadePessoas() < 2) {
            throw new ReservaMinimaException(2, TipoDominioException.Pessoa.getTipo(), TipoDominioException.Hotel.getTipo());
        }

        if (listarAnuncioPorIdService.consultarAnuncioID(cadastrarReservaRequest.getIdAnuncio()).getImovel()
                .getTipoImovel().equals(TipoImovel.POUSADA) && numeroDiarias < 5) {
            throw new ReservaMinimaException(5, TipoDominioException.Diaria.getTipo(), TipoDominioException.Pousada.getTipo());
        }

        BigDecimal valorTotal = diarias.multiply(listarAnuncioPorIdService.consultarAnuncioID(cadastrarReservaRequest.getIdAnuncio()).getValorDiaria());

        LocalDateTime dataHoraReserva = LocalDateTime.now();

        Pagamento pagamento = new Pagamento();
        pagamento.setValorTotal(valorTotal);
        pagamento.setStatus(StatusPagamento.PENDENTE);

        Reserva reserva = new Reserva();
        reserva.setSolicitante(listarUsuarioPorIdService.consultarUsuarioID(cadastrarReservaRequest.getIdSolicitante()));
        reserva.setAnuncio(listarAnuncioPorIdService.consultarAnuncioID(cadastrarReservaRequest.getIdAnuncio()));
        reserva.setPeriodo(periodo);
        reserva.setQuantidadePessoas(cadastrarReservaRequest.getQuantidadePessoas());
        reserva.setDataHoraReserva(dataHoraReserva);
        reserva.setPagamento(pagamento);

        reservaRepository.save(reserva);

        InformacaoReservaResponse informacaoReservaResponse = new InformacaoReservaResponse();
        informacaoReservaResponse.setIdReserva(reserva.getId());
        informacaoReservaResponse.setSolicitante(solicitanteResponse(reserva));
        informacaoReservaResponse.setQuantidadePessoas(reserva.getQuantidadePessoas());
        informacaoReservaResponse.setAnuncio(anuncioResponse(reserva));
        informacaoReservaResponse.setPeriodo(reserva.getPeriodo());
        informacaoReservaResponse.setPagamento(pagamento);
        return informacaoReservaResponse;
    }

    private DadosSolicitanteResponse solicitanteResponse(Reserva reserva) {
        DadosSolicitanteResponse dadosSolicitanteResponse = new DadosSolicitanteResponse();
        dadosSolicitanteResponse.setIdSolicitante(reserva.getSolicitante().getId());
        dadosSolicitanteResponse.setNomeSolicitante(reserva.getSolicitante().getNome());
        return dadosSolicitanteResponse;
    }

    private DadosAnuncioResponse anuncioResponse(Reserva reserva) {
        DadosAnuncioResponse dadosAnuncioResponse = new DadosAnuncioResponse();
        dadosAnuncioResponse.setIdAnuncio(reserva.getAnuncio().getId());
        dadosAnuncioResponse.setImovel(reserva.getAnuncio().getImovel());
        dadosAnuncioResponse.setAnunciante(reserva.getSolicitante());
        dadosAnuncioResponse.setFormasAceitas(reserva.getAnuncio().getFormasAceitas());
        dadosAnuncioResponse.setDescricao(reserva.getAnuncio().getDescricao());
        return dadosAnuncioResponse;
    }
}

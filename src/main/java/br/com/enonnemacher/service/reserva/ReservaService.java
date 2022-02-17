package br.com.enonnemacher.service.reserva;

import br.com.enonnemacher.domain.*;
import br.com.enonnemacher.exception.*;
import br.com.enonnemacher.repository.ReservaRepository;
import br.com.enonnemacher.request.CadastrarReservaRequest;
import br.com.enonnemacher.response.DadosAnuncioResponse;
import br.com.enonnemacher.response.DadosSolicitanteResponse;
import br.com.enonnemacher.response.InformacaoReservaResponse;
import br.com.enonnemacher.service.anuncio.AnuncioService;
import br.com.enonnemacher.service.usuario.ListarUsuarioPorIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private ListarUsuarioPorIdService listarUsuarioPorIdService;
    @Autowired
    private AnuncioService anuncioService;

    // 4.1 - Realizar uma reserva
    public InformacaoReservaResponse salvar(CadastrarReservaRequest cadastrarReservaRequest) throws IdNaoEncontradoException, ReservaSolicitanteMesmoAnuncianteException, ReservaMenorQueUmDiaException, DataFimReservaMaiorInicialException, ReservaMinimaException, ImovelComReservaAtivaException {
        if (cadastrarReservaRequest.getIdSolicitante()
                .equals(anuncioService.consultarAnuncioID(cadastrarReservaRequest.getIdAnuncio()).getAnunciante().getId())) {
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

        if (anuncioService.consultarAnuncioID(cadastrarReservaRequest.getIdAnuncio()).getImovel()
                .getTipoImovel().equals(TipoImovel.HOTEL) && cadastrarReservaRequest.getQuantidadePessoas() < 2) {
            throw new ReservaMinimaException(2, TipoDominioException.Pessoa.getTipo(), TipoDominioException.Hotel.getTipo());
        }

        if (anuncioService.consultarAnuncioID(cadastrarReservaRequest.getIdAnuncio()).getImovel()
                .getTipoImovel().equals(TipoImovel.POUSADA) && numeroDiarias < 5) {
            throw new ReservaMinimaException(5, TipoDominioException.Diaria.getTipo(), TipoDominioException.Pousada.getTipo());
        }

        BigDecimal valorTotal = diarias.multiply(anuncioService.consultarAnuncioID(cadastrarReservaRequest.getIdAnuncio()).getValorDiaria());

        LocalDateTime dataHoraReserva = LocalDateTime.now();

        Pagamento pagamento = new Pagamento();
        pagamento.setValorTotal(valorTotal);
        pagamento.setStatus(StatusPagamento.PENDENTE);

        Reserva reserva = new Reserva();
        reserva.setSolicitante(listarUsuarioPorIdService.consultarUsuarioID(cadastrarReservaRequest.getIdSolicitante()));
        reserva.setAnuncio(anuncioService.consultarAnuncioID(cadastrarReservaRequest.getIdAnuncio()));
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

    // 4.2 - Listar reservas de um solicitante especifico
    public Page<Reserva> listarReservasPorIdSolicitante(Pageable pageable, Long idSolicitante, Periodo periodo) {
        if (periodo != null) {
            if (periodo.getDataHoraInicial() != null && periodo.getDataHoraFinal() != null) {
                alterarPeriodoParaPadrao(periodo);
                return reservaRepository.findAllBySolicitanteIdEqualsAndPeriodoDataHoraInicialGreaterThanEqualAndPeriodoDataHoraFinalLessThanEqual(idSolicitante, periodo.getDataHoraInicial(), periodo.getDataHoraFinal(), pageable);
            }
        }
        return reservaRepository.findAllBySolicitanteIdEquals(idSolicitante, pageable);
    }

    // 4.3 - Listar reservas de um anunciante especifico
    public Page<Reserva> listarReservasPorIdAnunciante(Pageable pageable, Long idAnunciante) {
        Page<Reserva> listaReservas = reservaRepository.findAllByAnuncioAnuncianteIdEquals(pageable, idAnunciante);
        return listaReservas;
    }

    // 4.4 - Pagar Reserva
    public void pagarReserva(Long idReserva, FormaPagamento formaPagamento) throws
            NaoEncontradoLongException, StatusReservaException, FormaPagamentoNaoAceitaException {
        Reserva reserva = consultarReservaId(idReserva);

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

    // 4.5 - Cancelar uma reserva
    public void cancelarReserva(Long idReserva) throws NaoEncontradoLongException, StatusReservaException {
        Reserva reserva = consultarReservaId(idReserva);

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

    // 4.6 - Estornar reserva
    public void estornarReserva(Long idReserva) throws NaoEncontradoLongException, StatusReservaException {
        Reserva reserva = consultarReservaId(idReserva);

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

    public Reserva consultarReservaId(Long idReserva) throws NaoEncontradoLongException {
        return reservaRepository.findById(idReserva).orElseThrow(()
                -> new NaoEncontradoLongException(TipoDominioException.Reserva.getTipo(), TipoDominioException.Id.getTipo(), idReserva));
    }

    private void alterarPeriodoParaPadrao(Periodo periodo) {
        LocalDate dataInicial = LocalDate.from(periodo.getDataHoraInicial());
        LocalDate dataFinal = LocalDate.from(periodo.getDataHoraFinal());
        LocalTime horaInicial = LocalTime.of(14, 00, 00);
        LocalTime horaFinal = LocalTime.of(12, 00, 00);
        periodo.setDataHoraInicial(LocalDateTime.of(dataInicial, horaInicial));
        periodo.setDataHoraFinal(LocalDateTime.of(dataFinal, horaFinal));
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

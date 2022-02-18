package br.com.enonnemacher.controller;

import br.com.enonnemacher.domain.FormaPagamento;
import br.com.enonnemacher.domain.Periodo;
import br.com.enonnemacher.domain.Reserva;
import br.com.enonnemacher.exception.*;
import br.com.enonnemacher.request.CadastrarReservaRequest;
import br.com.enonnemacher.response.InformacaoReservaResponse;
import br.com.enonnemacher.service.reserva.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private CadastrarReservaService cadastrarReservaService;

    @Autowired
    private ListarReservaPorIdSolicitanteService listarReservaPorIdSolicitanteService;

    @Autowired
    private ListarReservaPorIdAnuncianteService listarReservaPorIdAnuncianteService;

    @Autowired
    private PagarReservaService pagarReservaService;

    @Autowired
    private CancelarReservaService cancelarReservaService;

    @Autowired
    private EstornarReservaService estornarReservaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InformacaoReservaResponse salvar(@Valid @RequestBody CadastrarReservaRequest cadastrarReservaRequest) throws ReservaSolicitanteMesmoAnuncianteException, ReservaMenorQueUmDiaException, ReservaMinimaException, IdNaoEncontradoException, DataFimReservaMaiorInicialException, ImovelComReservaAtivaException, ReservaSolicitanteMesmoAnuncianteException, ReservaMenorQueUmDiaException, ReservaMinimaException, IdNaoEncontradoException, DataFimReservaMaiorInicialException {
        return cadastrarReservaService.salvar(cadastrarReservaRequest);
    }

    @GetMapping(path = "/solicitantes/{idSolicitante}")
    public Page<Reserva> listarReservasPorIdSolicitante(@ApiIgnore @PageableDefault(sort = "periodo.dataHoraFinal", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Long idSolicitante, Periodo periodo) {
        Page<Reserva> listaReservas = listarReservaPorIdSolicitanteService.listarReservasPorIdSolicitante(pageable, idSolicitante, periodo);
        return listaReservas;
    }

    @GetMapping(path = "/anuncios/anunciantes/{idAnunciante}")
    public Page<Reserva> listarReservasPorIdAnunciante(@ApiIgnore @PageableDefault(sort = "periodo.dataHoraFinal", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Long idAnunciante) {
        return listarReservaPorIdAnuncianteService.listarReservasPorIdAnunciante(pageable, idAnunciante);
    }

    @PutMapping(path = "/{idReserva}/pagamentos")
    public void pagarReserva(@PathVariable Long idReserva, @RequestBody FormaPagamento formaPagamento) throws NaoEncontradoLongException, StatusReservaException, FormaPagamentoNaoAceitaException {
        pagarReservaService.pagarReserva(idReserva, formaPagamento);
    }

    @PutMapping(path = "/{idReserva}/pagamentos/cancelar")
    public void cancelarReserva(@PathVariable Long idReserva) throws NaoEncontradoLongException, StatusReservaException {
        cancelarReservaService.cancelarReserva(idReserva);
    }

    @PutMapping(path = "/{idReserva}/pagamentos/estornar")
    public void estornarReserva(@PathVariable Long idReserva) throws NaoEncontradoLongException, StatusReservaException {
        estornarReservaService.estornarReserva(idReserva);
    }
}

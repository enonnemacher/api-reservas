package br.com.enonnemacher.controller;

import br.com.enonnemacher.domain.Anuncio;
import br.com.enonnemacher.exception.CampoDuplicadoLongException;
import br.com.enonnemacher.exception.IdNaoEncontradoException;
import br.com.enonnemacher.request.CadastrarAnuncioRequest;
import br.com.enonnemacher.service.anuncio.CadastrarAnuncioService;
import br.com.enonnemacher.service.anuncio.ListarAnuncioPorAnuncianteService;
import br.com.enonnemacher.service.anuncio.ListarAnuncioService;
import br.com.enonnemacher.service.anuncio.RemoverAnuncioService;
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
@RequestMapping("/anuncios")
public class AnuncioController {

    @Autowired
    private CadastrarAnuncioService cadastrarAnuncioService;

    @Autowired
    private ListarAnuncioService listarAnuncioService;

    @Autowired
    private ListarAnuncioPorAnuncianteService listarAnuncioPorAnuncianteService;

    @Autowired
    private RemoverAnuncioService removerAnuncioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Anuncio salvar(@Valid @RequestBody CadastrarAnuncioRequest cadastrarAnuncioRequest) throws IdNaoEncontradoException, CampoDuplicadoLongException, CampoDuplicadoLongException {
        return cadastrarAnuncioService.salvar(cadastrarAnuncioRequest);
    }

    @GetMapping
    public Page<Anuncio> consultarAnuncios(@ApiIgnore @PageableDefault(sort = "valorDiaria", direction = Sort.Direction.ASC) Pageable pageable) {
        return listarAnuncioService.consultarAnuncios(pageable);
    }

    @GetMapping(path = "/anunciantes/{idAnunciante}")
    public Page<Anuncio> consultarAnuncioAnunciante(@ApiIgnore @PageableDefault(sort = "valorDiaria", direction = Sort.Direction.ASC) Pageable pageable, @PathVariable Long idAnunciante) throws IdNaoEncontradoException {
        return listarAnuncioPorAnuncianteService.consultarAnuncioAnunciante(pageable, idAnunciante);
    }

    @DeleteMapping("/{id}")
    public void removerAnuncio(@PathVariable Long id) throws IdNaoEncontradoException {
        removerAnuncioService.removerAnuncio(id);
    }
}

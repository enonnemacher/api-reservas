package br.com.enonnemacher.controller;

import br.com.enonnemacher.domain.Imovel;
import br.com.enonnemacher.exception.IdNaoEncontradoException;
import br.com.enonnemacher.request.CadastrarImovelRequest;
import br.com.enonnemacher.service.ImovelService;
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
@RequestMapping("/imoveis")
public class ImovelController {

    @Autowired
    private ImovelService imovelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Imovel salvar(@Valid @RequestBody CadastrarImovelRequest cadastrarImovelRequest) throws IdNaoEncontradoException {
        return imovelService.salvar(cadastrarImovelRequest);
    }

    @GetMapping
    public Page<Imovel> consultarImoveis(@ApiIgnore @PageableDefault(sort = "identificacao", direction = Sort.Direction.ASC) Pageable pageable) {
        return imovelService.consultarImoveis(pageable);
    }

    @GetMapping(path = "/proprietarios/{idProprietario}")
    public Page<Imovel> consultarImovelProprietario(@ApiIgnore @PageableDefault(sort = "identificacao", direction = Sort.Direction.ASC) Pageable pageable, @PathVariable Long idProprietario) throws IdNaoEncontradoException {
        return imovelService.consultarImovelProprietario(pageable, idProprietario);
    }

    @GetMapping(path = "/{idImovel}")
    public Imovel consultarImovelID(@PathVariable Long idImovel) throws IdNaoEncontradoException {
        return imovelService.consultarImovelID(idImovel);
    }

    @DeleteMapping("/{id}")
    public void removerImovel(@PathVariable Long id) throws Exception {
        imovelService.removerImovel(id);
    }
}

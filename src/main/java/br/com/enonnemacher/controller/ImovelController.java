package br.com.enonnemacher.controller;

import br.com.enonnemacher.domain.Imovel;
import br.com.enonnemacher.exception.IdNaoEncontradoException;
import br.com.enonnemacher.request.CadastrarImovelRequest;
import br.com.enonnemacher.service.imovel.*;
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
    private CadastrarImovelService cadastrarImovelService;

    @Autowired
    private ListarImovelService listarImovelService;

    @Autowired
    private ListarImovelPorProprietarioService listarImovelPorProprietarioService;

    @Autowired
    private ListarImovelPorIdService listarImovelPorIdService;

    @Autowired
    private RemoverImovelService removerImovelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Imovel salvar(@Valid @RequestBody CadastrarImovelRequest cadastrarImovelRequest) throws IdNaoEncontradoException {
        return cadastrarImovelService.salvar(cadastrarImovelRequest);
    }

    @GetMapping
    public Page<Imovel> consultarImoveis(@ApiIgnore @PageableDefault(sort = "identificacao", direction = Sort.Direction.ASC) Pageable pageable) {
        return listarImovelService.consultarImoveis(pageable);
    }

    @GetMapping(path = "/proprietarios/{idProprietario}")
    public Page<Imovel> consultarImovelProprietario(@ApiIgnore @PageableDefault(sort = "identificacao", direction = Sort.Direction.ASC) Pageable pageable, @PathVariable Long idProprietario) throws IdNaoEncontradoException {
        return listarImovelPorProprietarioService.consultarImovelProprietario(pageable, idProprietario);
    }

    @GetMapping(path = "/{idImovel}")
    public Imovel consultarImovelID(@PathVariable Long idImovel) throws IdNaoEncontradoException {
        return listarImovelPorIdService.consultarImovelID(idImovel);
    }

    @DeleteMapping("/{id}")
    public void removerImovel(@PathVariable Long id) throws Exception {
        removerImovelService.removerImovel(id);
    }
}

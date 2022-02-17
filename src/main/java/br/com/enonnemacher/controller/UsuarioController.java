package br.com.enonnemacher.controller;

import br.com.enonnemacher.domain.Usuario;
import br.com.enonnemacher.request.AtualizarUsuarioRequest;
import br.com.enonnemacher.service.UsuarioService;
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
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar(@Valid @RequestBody Usuario usuario) throws Exception {
        return usuarioService.salvar(usuario);
    }

    @GetMapping
    public Page<Usuario> consultarUsuarios(@ApiIgnore @PageableDefault(sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return usuarioService.consultarUsuarios(pageable);
    }

    @GetMapping(path = "/{id}")
    public Usuario consultarUsuario(@PathVariable Long id) throws Exception {
        return usuarioService.consultarUsuarioID(id);
    }

    @GetMapping(path = "/cpf/{cpf}")
    public Usuario consultarUsuarioCPF(@PathVariable String cpf) throws Exception {
        return usuarioService.consultarUsuarioCPF(cpf);
    }

    @PutMapping(path = "/{id}")
    public Usuario atualizarUsuario(@PathVariable Long id, @RequestBody @Valid AtualizarUsuarioRequest atualizarUsuarioRequest) throws Exception {
        return usuarioService.atualizarUsuario(id, atualizarUsuarioRequest);
    }
}

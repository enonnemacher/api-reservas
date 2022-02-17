package br.com.enonnemacher.service.usuario;

import br.com.enonnemacher.domain.Usuario;
import br.com.enonnemacher.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ListarUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Page<Usuario> consultarUsuarios(Pageable pageable) {
        Page<Usuario> listaUsuarios = usuarioRepository.findAll(pageable);
        return listaUsuarios;
    }
}

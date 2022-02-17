package br.com.enonnemacher.service.usuario;

import br.com.enonnemacher.domain.Usuario;
import br.com.enonnemacher.exception.IdNaoEncontradoException;
import br.com.enonnemacher.exception.TipoDominioException;
import br.com.enonnemacher.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListarUsuarioPorIdService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario consultarUsuarioID(Long id) throws IdNaoEncontradoException {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException(TipoDominioException.USUARIO.getTipo(),
                        TipoDominioException.Id.getTipo(), id));
    }
}

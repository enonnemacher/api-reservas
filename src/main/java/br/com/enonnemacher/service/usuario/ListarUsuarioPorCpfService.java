package br.com.enonnemacher.service.usuario;

import br.com.enonnemacher.domain.Usuario;
import br.com.enonnemacher.exception.NaoEncontradoException;
import br.com.enonnemacher.exception.TipoDominioException;
import br.com.enonnemacher.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListarUsuarioPorCpfService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario consultarUsuarioCPF(String cpf) throws NaoEncontradoException {
        if (!usuarioRepository.existsByCpf(cpf)) {
            throw new NaoEncontradoException(TipoDominioException.USUARIO.getTipo(), TipoDominioException.CPF.getTipo(),
                    cpf);
        }
        return usuarioRepository.findByCpf(cpf);
    }
}

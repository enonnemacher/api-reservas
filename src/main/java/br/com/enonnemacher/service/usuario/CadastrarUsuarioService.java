package br.com.enonnemacher.service.usuario;

import br.com.enonnemacher.domain.Usuario;
import br.com.enonnemacher.exception.CampoDuplicadoStringException;
import br.com.enonnemacher.exception.TipoDominioException;
import br.com.enonnemacher.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadastrarUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario salvar(Usuario usuario) throws Exception {
        boolean emailDuplicado = usuarioRepository.existsByEmail(usuario.getEmail());
        if (emailDuplicado) {
            throw new CampoDuplicadoStringException(TipoDominioException.USUARIO.getTipo(),
                    TipoDominioException.EMAIL.getTipo(), usuario.getEmail());
        }

        boolean cpfDuplicado = usuarioRepository.existsByCpf(usuario.getCpf());
        if (cpfDuplicado) {
            throw new CampoDuplicadoStringException(TipoDominioException.USUARIO.getTipo(),
                    TipoDominioException.CPF.getTipo(), usuario.getCpf());
        }

        return usuarioRepository.save(usuario);
    }
}

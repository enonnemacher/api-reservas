package br.com.enonnemacher.service.usuario;

import br.com.enonnemacher.domain.Usuario;
import br.com.enonnemacher.exception.CampoDuplicadoStringException;
import br.com.enonnemacher.exception.IdNaoEncontradoException;
import br.com.enonnemacher.exception.TipoDominioException;
import br.com.enonnemacher.repository.UsuarioRepository;
import br.com.enonnemacher.request.AtualizarUsuarioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AtualizarUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ListarUsuarioPorIdService listarUsuarioPorIdService;

    public Usuario atualizarUsuario(Long id, AtualizarUsuarioRequest atualizarUsuarioRequest) throws IdNaoEncontradoException, CampoDuplicadoStringException {
        boolean emailDuplicado = usuarioRepository.existsByEmailAndIdNot(atualizarUsuarioRequest.getEmail(), id);
        if (emailDuplicado) {
            throw new CampoDuplicadoStringException(TipoDominioException.USUARIO.getTipo(),
                    TipoDominioException.EMAIL.getTipo(), atualizarUsuarioRequest.getEmail());
        }

        Usuario usuario = listarUsuarioPorIdService.consultarUsuarioID(id);

        usuario.setEmail(atualizarUsuarioRequest.getEmail());
        usuario.setNome(atualizarUsuarioRequest.getNome());
        usuario.setSenha(atualizarUsuarioRequest.getSenha());
        usuario.setDataNascimento(atualizarUsuarioRequest.getDataNascimento());

        if (atualizarUsuarioRequest.getEndereco() != null) {
            usuario.getEndereco().setBairro(atualizarUsuarioRequest.getEndereco().getBairro());
            usuario.getEndereco().setCep(atualizarUsuarioRequest.getEndereco().getCep());
            usuario.getEndereco().setComplemento(atualizarUsuarioRequest.getEndereco().getComplemento());
            usuario.getEndereco().setCidade(atualizarUsuarioRequest.getEndereco().getCidade());
            usuario.getEndereco().setEstado(atualizarUsuarioRequest.getEndereco().getEstado());
            usuario.getEndereco().setLogradouro(atualizarUsuarioRequest.getEndereco().getLogradouro());
            usuario.getEndereco().setNumero(atualizarUsuarioRequest.getEndereco().getNumero());
        } else {
            usuario.setEndereco(null);
        }

        return usuarioRepository.save(usuario);
    }
}

package br.com.enonnemacher.service;

import br.com.enonnemacher.domain.Usuario;
import br.com.enonnemacher.exception.CampoDuplicadoStringException;
import br.com.enonnemacher.exception.IdNaoEncontradoException;
import br.com.enonnemacher.exception.NaoEncontradoException;
import br.com.enonnemacher.exception.TipoDominioException;
import br.com.enonnemacher.repository.UsuarioRepository;
import br.com.enonnemacher.request.AtualizarUsuarioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 1.1 - Cadastro de Usuário
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

    // 1.2 - Listar usuários
    public Page<Usuario> consultarUsuarios(Pageable pageable) {
        Page<Usuario> listaUsuarios = usuarioRepository.findAll(pageable);
        return listaUsuarios;
    }

    // 1.3 - Buscar um usuário por id
    public Usuario consultarUsuarioID(Long id) throws IdNaoEncontradoException {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException(TipoDominioException.USUARIO.getTipo(),
                        TipoDominioException.Id.getTipo(), id));
    }

    // 1.4 - Buscar um usuário por cpf
    public Usuario consultarUsuarioCPF(String cpf) throws NaoEncontradoException {
        if (!usuarioRepository.existsByCpf(cpf)) {
            throw new NaoEncontradoException(TipoDominioException.USUARIO.getTipo(), TipoDominioException.CPF.getTipo(),
                    cpf);
        }
        return usuarioRepository.findByCpf(cpf);
    }

    // 1.5 - Alterar um usuário
    public Usuario atualizarUsuario(Long id, AtualizarUsuarioRequest atualizarUsuarioRequest) throws IdNaoEncontradoException, CampoDuplicadoStringException {
        boolean emailDuplicado = usuarioRepository.existsByEmailAndIdNot(atualizarUsuarioRequest.getEmail(), id);
        if (emailDuplicado) {
            throw new CampoDuplicadoStringException(TipoDominioException.USUARIO.getTipo(),
                    TipoDominioException.EMAIL.getTipo(), atualizarUsuarioRequest.getEmail());
        }

        Usuario usuario = consultarUsuarioID(id);

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

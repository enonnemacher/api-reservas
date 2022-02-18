package br.com.enonnemacher.service.imovel;

import br.com.enonnemacher.domain.Imovel;
import br.com.enonnemacher.domain.Usuario;
import br.com.enonnemacher.exception.IdNaoEncontradoException;
import br.com.enonnemacher.repository.ImovelRepository;
import br.com.enonnemacher.service.usuario.ListarUsuarioPorIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ListarImovelPorProprietarioService {

    @Autowired
    private ImovelRepository imovelRepository;

    @Autowired
    private ListarUsuarioPorIdService listarUsuarioPorIdService;

    public Page<Imovel> consultarImovelProprietario(Pageable pageable, Long id) throws IdNaoEncontradoException {
        Usuario usuario = listarUsuarioPorIdService.consultarUsuarioID(id);
        Page<Imovel> listaImoveisDoProprietario = imovelRepository.findAllByProprietarioEqualsAndExcluidoFalse(pageable, usuario);
        return listaImoveisDoProprietario;
    }
}

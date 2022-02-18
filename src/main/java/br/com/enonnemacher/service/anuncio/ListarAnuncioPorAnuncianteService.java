package br.com.enonnemacher.service.anuncio;

import br.com.enonnemacher.domain.Anuncio;
import br.com.enonnemacher.domain.Usuario;
import br.com.enonnemacher.exception.IdNaoEncontradoException;
import br.com.enonnemacher.repository.AnuncioRepository;
import br.com.enonnemacher.service.usuario.ListarUsuarioPorIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ListarAnuncioPorAnuncianteService {

    @Autowired
    private ListarUsuarioPorIdService listarUsuarioPorIdService;

    @Autowired
    private AnuncioRepository anuncioRepository;

    public Page<Anuncio> consultarAnuncioAnunciante(Pageable pageable, Long idAnunciante) throws IdNaoEncontradoException {
        Usuario usuario = listarUsuarioPorIdService.consultarUsuarioID(idAnunciante);
        Page<Anuncio> listaAnuncioAnunciante = anuncioRepository.findAllByAnuncianteEqualsAndExcluidoFalse(pageable, usuario);
        return listaAnuncioAnunciante;
    }
}

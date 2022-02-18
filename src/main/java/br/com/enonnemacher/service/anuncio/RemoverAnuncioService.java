package br.com.enonnemacher.service.anuncio;

import br.com.enonnemacher.domain.Anuncio;
import br.com.enonnemacher.exception.IdNaoEncontradoException;
import br.com.enonnemacher.repository.AnuncioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoverAnuncioService {

    @Autowired
    private ListarAnuncioPorIdService listarAnuncioPorIdService;

    @Autowired
    private AnuncioRepository anuncioRepository;

    public void removerAnuncio(Long id) throws IdNaoEncontradoException {
        Anuncio anuncio = listarAnuncioPorIdService.consultarAnuncioID(id);
        anuncio.setExcluido(true);
        anuncioRepository.save(anuncio);
    }
}

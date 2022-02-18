package br.com.enonnemacher.service.anuncio;

import br.com.enonnemacher.domain.Anuncio;
import br.com.enonnemacher.repository.AnuncioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ListarAnuncioService {

    @Autowired
    private AnuncioRepository anuncioRepository;

    public Page<Anuncio> consultarAnuncios(Pageable pageable) {
        Page<Anuncio> listaAnuncios = anuncioRepository.findAllByExcluidoFalse(pageable);
        return listaAnuncios;
    }
}

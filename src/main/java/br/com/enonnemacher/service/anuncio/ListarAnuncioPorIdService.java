package br.com.enonnemacher.service.anuncio;

import br.com.enonnemacher.domain.Anuncio;
import br.com.enonnemacher.exception.IdNaoEncontradoException;
import br.com.enonnemacher.exception.TipoDominioException;
import br.com.enonnemacher.repository.AnuncioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Optional.ofNullable;

@Service
public class ListarAnuncioPorIdService {

    @Autowired
    private AnuncioRepository anuncioRepository;

    public Anuncio consultarAnuncioID(Long id) throws IdNaoEncontradoException {
        return ofNullable(anuncioRepository.findByIdAndExcluidoFalse(id))
                .orElseThrow(() -> new IdNaoEncontradoException(TipoDominioException.Anuncio.getTipo(),
                        TipoDominioException.Id.getTipo(), id));
    }
}
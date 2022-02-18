package br.com.enonnemacher.service.imovel;

import br.com.enonnemacher.domain.Imovel;
import br.com.enonnemacher.exception.IdNaoEncontradoException;
import br.com.enonnemacher.exception.TipoDominioException;
import br.com.enonnemacher.repository.ImovelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Optional.ofNullable;

@Service
public class ListarImovelPorIdService {

    @Autowired
    private ImovelRepository imovelRepository;

    public Imovel consultarImovelID(Long id) throws IdNaoEncontradoException {
        return ofNullable(imovelRepository.findByIdAndExcluidoFalse(id))
                .orElseThrow(() -> new IdNaoEncontradoException(TipoDominioException.Imovel.getTipo(),
                        TipoDominioException.Id.getTipo(), id));
    }
}

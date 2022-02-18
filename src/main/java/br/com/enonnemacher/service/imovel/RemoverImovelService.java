package br.com.enonnemacher.service.imovel;

import br.com.enonnemacher.domain.Imovel;
import br.com.enonnemacher.exception.IdNaoEncontradoException;
import br.com.enonnemacher.exception.ImovelComAnuncioException;
import br.com.enonnemacher.repository.AnuncioRepository;
import br.com.enonnemacher.repository.ImovelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoverImovelService {

    @Autowired
    private ListarImovelPorIdService listarImovelPorIdService;

    @Autowired
    private AnuncioRepository anuncioRepository;

    @Autowired
    private ImovelRepository imovelRepository;

    public void removerImovel(Long id) throws IdNaoEncontradoException, ImovelComAnuncioException {
        Imovel imovel = listarImovelPorIdService.consultarImovelID(id);
        if(anuncioRepository.existsByImovelAndExcluidoFalse(imovel)){
            throw new ImovelComAnuncioException();
        }
        imovel.setExcluido(true);
        imovelRepository.save(imovel);
    }
}

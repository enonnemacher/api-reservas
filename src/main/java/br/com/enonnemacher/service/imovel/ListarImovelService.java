package br.com.enonnemacher.service.imovel;

import br.com.enonnemacher.domain.Imovel;
import br.com.enonnemacher.repository.ImovelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ListarImovelService {

    @Autowired
    private ImovelRepository imovelRepository;

    public Page<Imovel> consultarImoveis(Pageable pageable) {
        Page<Imovel> listaImoveis = imovelRepository.findAllByExcluidoFalse(pageable);
        return listaImoveis;
    }
}

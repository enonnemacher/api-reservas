package br.com.enonnemacher.service;

import br.com.enonnemacher.domain.CaracteristicaImovel;
import br.com.enonnemacher.domain.Imovel;
import br.com.enonnemacher.domain.Usuario;
import br.com.enonnemacher.exception.IdNaoEncontradoException;
import br.com.enonnemacher.exception.ImovelComAnuncioException;
import br.com.enonnemacher.exception.TipoDominioException;
import br.com.enonnemacher.repository.ImovelRepository;
import br.com.enonnemacher.request.CadastrarImovelRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Optional.ofNullable;

@Service
public class ImovelService {

    @Autowired
    private ImovelRepository imovelRepository;
//    @Autowired
//    private AnuncioRepository anuncioRepository;
    @Autowired
    private UsuarioService usuarioService;

    // 2.1 - Cadastro de Imóvel
    public Imovel salvar(CadastrarImovelRequest cadastrarImovelRequest) throws IdNaoEncontradoException {
        if (cadastrarImovelRequest.getIdProprietario() == null) {
            throw new IdNaoEncontradoException(TipoDominioException.USUARIO.getTipo(), TipoDominioException.Id.getTipo(),
                    cadastrarImovelRequest.getIdProprietario());
        }

        List<CaracteristicaImovel> caracteristicas = cadastrarImovelRequest.getCaracteristicas();

        return imovelRepository.save(new Imovel(null, cadastrarImovelRequest.getIdentificacao(),
                cadastrarImovelRequest.getTipoImovel(),
                cadastrarImovelRequest.getEndereco(),
                usuarioService.consultarUsuarioID(cadastrarImovelRequest.getIdProprietario()),
                caracteristicas, false));
    }

    // 2.2 - Listar imóveis
    public Page<Imovel> consultarImoveis(Pageable pageable) {
        Page<Imovel> listaImoveis = imovelRepository.findAllByExcluidoFalse(pageable);
        return listaImoveis;
    }

    // 2.3 - Listar imóveis de um proprietário específico
    public Page<Imovel> consultarImovelProprietario(Pageable pageable, Long id) throws IdNaoEncontradoException {
        Usuario usuario = usuarioService.consultarUsuarioID(id);
        Page<Imovel> listaImoveisDoProprietario = imovelRepository.findAllByProprietarioEqualsAndExcluidoFalse(pageable, usuario);
        return listaImoveisDoProprietario;
    }

    // 2.4 - Buscar um imóvel por id
    public Imovel consultarImovelID(Long id) throws IdNaoEncontradoException {
        return ofNullable(imovelRepository.findByIdAndExcluidoFalse(id))
                .orElseThrow(() -> new IdNaoEncontradoException(TipoDominioException.Imovel.getTipo(),
                        TipoDominioException.Id.getTipo(), id));
    }

    // 2.5 - Excluir um imóvel
    public void removerImovel(Long id) throws IdNaoEncontradoException, ImovelComAnuncioException {
        Imovel imovel = consultarImovelID(id);
//        if(anuncioRepository.existsByImovelAndExcluidoFalse(imovel)){
//            throw new ImovelComAnuncioException();
//        }
        imovel.setExcluido(true);
        imovelRepository.save(imovel);
    }
}

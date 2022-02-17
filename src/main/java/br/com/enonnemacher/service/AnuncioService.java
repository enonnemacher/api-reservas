package br.com.enonnemacher.service;

import br.com.enonnemacher.domain.Anuncio;
import br.com.enonnemacher.domain.FormaPagamento;
import br.com.enonnemacher.domain.Imovel;
import br.com.enonnemacher.domain.Usuario;
import br.com.enonnemacher.exception.CampoDuplicadoLongException;
import br.com.enonnemacher.exception.IdNaoEncontradoException;
import br.com.enonnemacher.exception.TipoDominioException;
import br.com.enonnemacher.repository.AnuncioRepository;
import br.com.enonnemacher.request.CadastrarAnuncioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Optional.ofNullable;

@Service
public class AnuncioService {

    @Autowired
    private AnuncioRepository anuncioRepository;
    @Autowired
    private ImovelService imovelService;
    @Autowired
    private UsuarioService usuarioService;

    // 3.1 - Anunciar imóvel
    public Anuncio salvar(CadastrarAnuncioRequest cadastrarAnuncioRequest) throws IdNaoEncontradoException, CampoDuplicadoLongException {
        if (cadastrarAnuncioRequest.getIdImovel() == null) {
            throw new IdNaoEncontradoException(TipoDominioException.Imovel.getTipo(),
                    TipoDominioException.Id.getTipo(),
                    cadastrarAnuncioRequest.getIdImovel());
        }

        if (cadastrarAnuncioRequest.getIdAnunciante() == null) {
            throw new IdNaoEncontradoException(TipoDominioException.USUARIO.getTipo(),
                    TipoDominioException.Id.getTipo(),
                    cadastrarAnuncioRequest.getIdAnunciante());
        }

        Imovel imovel = imovelService.consultarImovelID(cadastrarAnuncioRequest.getIdImovel());
        if (anuncioRepository.existsByImovelAndExcluidoFalse(imovel)) {
            throw new CampoDuplicadoLongException(TipoDominioException.Anuncio.getTipo(),
                    TipoDominioException.IdImovel.getTipo(), cadastrarAnuncioRequest.getIdImovel());
        }

        List<FormaPagamento> formasPagamento = cadastrarAnuncioRequest.getFormasAceitas();

        return anuncioRepository.save(new Anuncio(null,
                cadastrarAnuncioRequest.getTipoAnuncio(),
                imovelService.consultarImovelID(cadastrarAnuncioRequest.getIdImovel()),
                usuarioService.consultarUsuarioID(cadastrarAnuncioRequest.getIdAnunciante()),
                cadastrarAnuncioRequest.getValorDiaria(),
                formasPagamento,
                cadastrarAnuncioRequest.getDescricao(),
                false));
    }

    // 3.2 - Listar anúncios
    public Page<Anuncio> consultarAnuncios(Pageable pageable) {
        Page<Anuncio> listaAnuncios = anuncioRepository.findAllByExcluidoFalse(pageable);
        return listaAnuncios;
    }

    // 3.3 - Listar anúncios de um anunciante específico
    public Page<Anuncio> consultarAnuncioAnunciante(Pageable pageable, Long idAnunciante) throws IdNaoEncontradoException {
        Usuario usuario = usuarioService.consultarUsuarioID(idAnunciante);
        Page<Anuncio> listaAnuncioAnunciante = anuncioRepository.findAllByAnuncianteEqualsAndExcluidoFalse(pageable, usuario);
        return listaAnuncioAnunciante;
    }

    // 3.4 - Excluir um anúncio
    public void removerAnuncio(Long id) throws IdNaoEncontradoException {
        Anuncio anuncio = consultarAnuncioID(id);
        anuncio.setExcluido(true);
        anuncioRepository.save(anuncio);
    }

    public Anuncio consultarAnuncioID(Long id) throws IdNaoEncontradoException {
        return ofNullable(anuncioRepository.findByIdAndExcluidoFalse(id))
                .orElseThrow(() -> new IdNaoEncontradoException(TipoDominioException.Anuncio.getTipo(),
                        TipoDominioException.Id.getTipo(), id));
    }
}
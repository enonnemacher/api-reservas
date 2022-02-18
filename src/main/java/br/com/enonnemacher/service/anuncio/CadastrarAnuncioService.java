package br.com.enonnemacher.service.anuncio;

import br.com.enonnemacher.domain.Anuncio;
import br.com.enonnemacher.domain.FormaPagamento;
import br.com.enonnemacher.domain.Imovel;
import br.com.enonnemacher.exception.CampoDuplicadoLongException;
import br.com.enonnemacher.exception.IdNaoEncontradoException;
import br.com.enonnemacher.exception.TipoDominioException;
import br.com.enonnemacher.repository.AnuncioRepository;
import br.com.enonnemacher.request.CadastrarAnuncioRequest;
import br.com.enonnemacher.service.imovel.ListarImovelPorIdService;
import br.com.enonnemacher.service.usuario.ListarUsuarioPorIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CadastrarAnuncioService {

    @Autowired
    private ListarImovelPorIdService listarImovelPorIdService;

    @Autowired
    private AnuncioRepository anuncioRepository;

    @Autowired
    private ListarUsuarioPorIdService listarUsuarioPorIdService;

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

        Imovel imovel = listarImovelPorIdService.consultarImovelID(cadastrarAnuncioRequest.getIdImovel());
        if (anuncioRepository.existsByImovelAndExcluidoFalse(imovel)) {
            throw new CampoDuplicadoLongException(TipoDominioException.Anuncio.getTipo(),
                    TipoDominioException.IdImovel.getTipo(), cadastrarAnuncioRequest.getIdImovel());
        }

        List<FormaPagamento> formasPagamento = cadastrarAnuncioRequest.getFormasAceitas();

        return anuncioRepository.save(new Anuncio(null,
                cadastrarAnuncioRequest.getTipoAnuncio(),
                listarImovelPorIdService.consultarImovelID(cadastrarAnuncioRequest.getIdImovel()),
                listarUsuarioPorIdService.consultarUsuarioID(cadastrarAnuncioRequest.getIdAnunciante()),
                cadastrarAnuncioRequest.getValorDiaria(),
                formasPagamento,
                cadastrarAnuncioRequest.getDescricao(),
                false));
    }
}

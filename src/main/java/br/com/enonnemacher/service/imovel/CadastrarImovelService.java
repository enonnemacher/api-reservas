package br.com.enonnemacher.service.imovel;

import br.com.enonnemacher.domain.CaracteristicaImovel;
import br.com.enonnemacher.domain.Imovel;
import br.com.enonnemacher.exception.IdNaoEncontradoException;
import br.com.enonnemacher.exception.TipoDominioException;
import br.com.enonnemacher.repository.ImovelRepository;
import br.com.enonnemacher.request.CadastrarImovelRequest;
import br.com.enonnemacher.service.usuario.ListarUsuarioPorIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CadastrarImovelService {

    @Autowired
    private ImovelRepository imovelRepository;

    @Autowired
    private ListarUsuarioPorIdService listarUsuarioPorIdService;

    public Imovel salvar(CadastrarImovelRequest cadastrarImovelRequest) throws IdNaoEncontradoException {
        if (cadastrarImovelRequest.getIdProprietario() == null) {
            throw new IdNaoEncontradoException(TipoDominioException.USUARIO.getTipo(), TipoDominioException.Id.getTipo(),
                    cadastrarImovelRequest.getIdProprietario());
        }

        List<CaracteristicaImovel> caracteristicas = cadastrarImovelRequest.getCaracteristicas();

        return imovelRepository.save(new Imovel(null, cadastrarImovelRequest.getIdentificacao(),
                cadastrarImovelRequest.getTipoImovel(),
                cadastrarImovelRequest.getEndereco(),
                listarUsuarioPorIdService.consultarUsuarioID(cadastrarImovelRequest.getIdProprietario()),
                caracteristicas, false));
    }
}

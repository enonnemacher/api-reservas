package br.com.enonnemacher.repository;

import br.com.enonnemacher.domain.Anuncio;
import br.com.enonnemacher.domain.Imovel;
import br.com.enonnemacher.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnuncioRepository extends PagingAndSortingRepository<Anuncio, Long> {

    List<Anuncio> findAll();

    Anuncio findByIdAndExcluidoFalse(Long id);

    Page<Anuncio> findAllByExcluidoFalse(Pageable pageable);

    Page<Anuncio> findAllByAnuncianteEqualsAndExcluidoFalse(Pageable pageable, Usuario usuario);

    boolean existsByImovelAndExcluidoFalse(Imovel imovel);
}

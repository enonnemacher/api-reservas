package br.com.enonnemacher.repository;

import br.com.enonnemacher.domain.Imovel;
import br.com.enonnemacher.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImovelRepository extends PagingAndSortingRepository<Imovel, Long> {
    List<Imovel> findAllByProprietarioEquals(Usuario usuario);

    List<Imovel> findAll();

    Page<Imovel> findAllByExcluidoFalse(Pageable pageable);

    Page<Imovel> findAllByProprietarioEqualsAndExcluidoFalse(Pageable pageable, Usuario usuario);

    Imovel findByIdAndExcluidoFalse(Long id);
}

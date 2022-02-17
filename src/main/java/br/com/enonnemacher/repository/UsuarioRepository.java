package br.com.enonnemacher.repository;

import br.com.enonnemacher.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Long> {

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    Usuario findByCpf(String cpf);

    boolean existsByEmailAndIdNot(String email, Long id);

    Page<Usuario> findAll(Pageable pageable);
}

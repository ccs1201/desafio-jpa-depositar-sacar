package br.com.ccs.desafiojpadepositarsacar.repostitories;

import br.com.ccs.desafiojpadepositarsacar.entities.Usuario;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>, CustomRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select u from Usuario u where id = :id")
    void findByIdLockPessimista(Integer id);
}
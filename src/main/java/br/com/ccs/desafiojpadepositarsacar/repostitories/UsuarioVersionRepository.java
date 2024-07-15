package br.com.ccs.desafiojpadepositarsacar.repostitories;

import br.com.ccs.desafiojpadepositarsacar.entities.UsuarioVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioVersionRepository extends JpaRepository<UsuarioVersion, Integer>, CustomRepository {
}
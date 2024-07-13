package br.com.ccs.desafiojpadepositarsacar.repostitories;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomRepository {
    EntityManager getEntityManager();
}

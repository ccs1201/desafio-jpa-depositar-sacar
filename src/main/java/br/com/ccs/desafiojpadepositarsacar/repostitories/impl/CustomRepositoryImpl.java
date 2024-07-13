package br.com.ccs.desafiojpadepositarsacar.repostitories.impl;

import br.com.ccs.desafiojpadepositarsacar.repostitories.CustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


public class CustomRepositoryImpl implements CustomRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}

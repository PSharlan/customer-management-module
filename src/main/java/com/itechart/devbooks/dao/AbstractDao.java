package com.itechart.devbooks.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public abstract class AbstractDao<T> {

    private Class<T> persistentClass;

    @PersistenceContext
    private EntityManager em;

    AbstractDao(Class<T> persistentClass){
        this.persistentClass = persistentClass;
    }

    protected final EntityManager getEntityManager() {
        return em;
    }

    public T findOneById(long id) {
        T foundEntity = em.find(persistentClass, id);
        return foundEntity;
    }

    public List<T> getAll() {
        List<T> foundEntities = em.createQuery("SELECT e FROM " + persistentClass.getSimpleName() + " e", persistentClass).getResultList();
        return foundEntities;
    }

    public T create(T entity) {
        em.persist(entity);
        return entity;
    }

    public T merge(T entity) {
        T en = em.merge(entity);
        return en;
    }


    public void remove(T entity) {
        em.remove(em.merge(entity));
    }

}

package com.example.mybookshopapp.data;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;

@Repository
public abstract class AbstractHibernateDAO<T> {

    @Autowired
    EntityManagerFactory entityManagerFactory;

    private Class<T> clazz;

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T findOne(Long id) {
        return getSession().find(clazz, id);
    }

    public Session getSession() {
        return entityManagerFactory.createEntityManager().unwrap(Session.class);
    }
}

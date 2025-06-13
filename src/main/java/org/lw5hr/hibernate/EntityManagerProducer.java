package org.lw5hr.hibernate;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
public class EntityManagerProducer
{
    @PersistenceUnit
    private EntityManagerFactory emf;

    @Produces // you can also make this @RequestScoped
    public EntityManager create()
    {
        return emf.createEntityManager();
    }

    public void close(@Disposes EntityManager em)
    {
        if (em.isOpen())
        {
            em.close();
        }
    }
}
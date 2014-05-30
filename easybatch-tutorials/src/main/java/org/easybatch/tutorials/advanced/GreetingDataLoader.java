package org.easybatch.tutorials.advanced;

import org.easybatch.core.api.AbstractRecordProcessor;
import org.hibernate.HibernateException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GreetingDataLoader extends AbstractRecordProcessor<Greeting> {

    private Logger logger = Logger.getLogger(GreetingDataLoader.class.getName());

    public void processRecord(final Greeting greeting) throws Exception {
        DatabaseUtil.getCurrentSession().beginTransaction();
        try {
            DatabaseUtil.getCurrentSession().saveOrUpdate(greeting);
            DatabaseUtil.getCurrentSession().getTransaction().commit();
            logger.log(Level.INFO, "Greeting {0} successfully persisted in the database", greeting);
        } catch (HibernateException e) {
            DatabaseUtil.getCurrentSession().getTransaction().rollback();
            throw new Exception("A database exception occurred during greeting persisting.", e);
        }
    }

}
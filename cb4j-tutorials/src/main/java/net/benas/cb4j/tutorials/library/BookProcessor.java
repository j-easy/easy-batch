package net.benas.cb4j.tutorials.library;

import net.benas.cb4j.core.api.RecordProcessingException;
import net.benas.cb4j.core.api.RecordProcessor;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

/**
 * A book processor that inserts Book objects in database.
 * @author benas (md.benhassine@gmail.com)
 */
public class BookProcessor implements RecordProcessor<Book> {

    public void preProcessRecord(Book book) throws RecordProcessingException {
        DatabaseUtil.getCurrentSession().beginTransaction();
    }

    public void processRecord(Book book) throws RecordProcessingException {
        try {
            DatabaseUtil.getCurrentSession().saveOrUpdate(book);
        } catch (HibernateException e) {
            DatabaseUtil.getCurrentSession().getTransaction().rollback();
            throw new RecordProcessingException(e.getMessage());
        }
    }

    public void postProcessRecord(Book book) throws RecordProcessingException {
        Transaction transaction = DatabaseUtil.getCurrentSession().getTransaction();
        if (transaction.isActive() && !transaction.wasRolledBack()) {
            transaction.commit();
        }
    }

}

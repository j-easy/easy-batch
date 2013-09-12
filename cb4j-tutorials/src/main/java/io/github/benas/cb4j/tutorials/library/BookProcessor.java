package io.github.benas.cb4j.tutorials.library;

import io.github.benas.cb4j.core.api.RecordProcessingException;
import io.github.benas.cb4j.core.impl.DefaultRecordProcessorImpl;
import org.hibernate.HibernateException;

/**
 * A book processor that inserts Book objects in database.<br/>
 *
 * <strong>This implementation is kept simple for demonstration purpose. In production environment, one may define a
 * commit interval to avoid performance issues of committing the transaction after each record insertion.</strong>
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class BookProcessor extends DefaultRecordProcessorImpl<Book> {

    public void preProcessRecord(Book book) throws RecordProcessingException {
        DatabaseUtil.getCurrentSession().beginTransaction();
    }

    public void processRecord(Book book) throws RecordProcessingException {
        try {
            DatabaseUtil.getCurrentSession().saveOrUpdate(book);
            DatabaseUtil.getCurrentSession().getTransaction().commit();
        } catch (HibernateException e) {
            DatabaseUtil.getCurrentSession().getTransaction().rollback();
            throw new RecordProcessingException(e.getMessage());
        }
    }

}

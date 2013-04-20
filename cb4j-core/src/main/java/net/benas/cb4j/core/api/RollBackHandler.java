package net.benas.cb4j.core.api;

/**
 * A callback interface to rollback a record processing when an unexpected exception occurs.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public interface RollbackHandler<T> {

    public void rollback(T typedRecord) throws Exception;

}

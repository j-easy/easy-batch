package io.github.benas.easybatch.core.api;

/**
 * Abstract record processor implementation to extend by clients that do not need to implement
 * {@link RecordProcessor#getEasyBatchResult()}.
 *
 * @param <T> The target domain object type.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public abstract class AbstractRecordProcessor<T> implements RecordProcessor<T, Object> {

    @Override
    public Object getEasyBatchResult() {
        return null;
    }

}

package org.easybatch.extensions.stream;

import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;

import java.util.Date;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Reader that reads records form a {@link Stream}.
 *
 * This reader produces {@link GenericRecord} instances.
 *
 * @param <T> Type of elements in the stream.
 * @author Charles Fleury
 * @since 5.0
 */
public class StreamRecordReader<T> implements RecordReader {

    protected Stream<T> dataSource;
    protected Iterator<T> iterator;
    protected long currentRecordNumber;

    /**
     * Create a {@link StreamRecordReader} to read records from a {@link Stream}.
     *
     * @param dataSource to read records from
     */
    public StreamRecordReader(final Stream<T> dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Open the reader.
     */
    @Override
    public void open() throws Exception {
        if (dataSource == null) {
            throw new IllegalArgumentException("The stream must not be null");
        }
        currentRecordNumber = 0;
        iterator = dataSource.iterator();
    }

    /**
     * Read next record from the data source.
     *
     * @return the next record from the data source.
     */
    @Override
    public Record<T> readRecord() throws Exception {
        if (iterator.hasNext()) {
            Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
            return new GenericRecord<>(header, iterator.next());
        } else {
            return null;
        }
    }

    private String getDataSourceName() {
        return "In-Memory Stream";
    }

    /**
     * Close the reader.
     */
    @Override
    public void close() throws Exception {
        dataSource.close();
    }
}

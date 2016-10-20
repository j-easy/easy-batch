package org.easybatch.extensions.stream;

import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;

import java.util.Date;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * @author Charles Fleury
 * @since 17/10/16.
 */
public class StreamRecordReader<T> implements RecordReader {

  private static final String DEFAULT_DATASOURCE_NAME = "DATASOURCE";

  protected String datasource;
  protected Stream<T> stream;
  protected Iterator<T> iterator;
  protected long currentRecordNumber;

  /**
   * Create a {@link StreamRecordReader} to read record from java 8 stream.
   *
   * @param stream to read record from
   *  @param datasource name (default to DEFAULT_DATASOURCE_NAME)
   */
  public StreamRecordReader(final Stream<T> stream, final String datasource) {
    this.stream = stream;
    this.datasource = datasource;
  }

  /**
   * Open the reader.
   */
  @Override
  public void open() throws Exception {
    if(stream == null) {
      throw new IllegalArgumentException("stream must not be null");
    }

    if(datasource == null || datasource.isEmpty()) {
      datasource = DEFAULT_DATASOURCE_NAME;
    }

    currentRecordNumber = 0;
    iterator = stream.iterator();
  }

  /**
   * Read next record from the data source.
   *
   * @return the next record from the data source.
   */
  @Override
  public Record<T> readRecord() throws Exception {
    if(iterator.hasNext()) {
      Header header = new Header(++currentRecordNumber, datasource, new Date());
      return new GenericRecord<>(header, iterator.next());
    } else {
      return null;
    }
  }

  /**
   * Close the reader.
   */
  @Override
  public void close() throws Exception {
    // no op
  }
}

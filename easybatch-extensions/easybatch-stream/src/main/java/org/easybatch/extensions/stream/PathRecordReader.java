package org.easybatch.extensions.stream;

import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.Record;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.nio.file.Files.list;
import static java.nio.file.Files.walk;

/**
 * @author Charles Fleury
 * @since 17/10/16.
 */
public class PathRecordReader implements RecordReader {

  private Path directory;
  private boolean recursive;
  private StreamRecordReader<Path> delegate;

  /**
   * Create a {@link PathRecordReader} to read record from java 8 stream.
   *
   * @param directory to read files from
   */
  public PathRecordReader(final Path directory) {
    this(directory, false);
  }

  /**
   * Create a {@link PathRecordReader} to read record from java 8 stream.
   *
   * @param directory to read files from
   * @param recursive if the reader should be recursive
   */
  public PathRecordReader(final Path directory, final boolean recursive) {
    this.directory = directory;
    this.recursive = recursive;
  }

  /**
   * Open the reader.
   */
  @Override
  public void open() throws Exception {
    try {
      Stream<Path> stream;
      if (recursive) {
        stream = walk(directory);
      } else {
        stream = list(directory);
      }
      delegate = new StreamRecordReader<>(stream.filter(Files::isRegularFile), directory.toAbsolutePath().toString());
      delegate.open();
    } catch (IOException e) {
      throw new IllegalArgumentException("cannot read directory", e);
    }
  }

  /**
   * Read next record from the data source.
   *
   * @return the next record from the data source.
   */
  @Override
  public Record<Path> readRecord() throws Exception {
    return delegate.readRecord();
  }

  /**
   * Close the reader.
   */
  @Override
  public void close() throws Exception {
    if(delegate != null) {
      delegate.close();
    }
  }
}

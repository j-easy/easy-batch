package org.easybatch.extensions.stream;

import org.easybatch.core.record.Record;
import org.junit.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Charles Fleury
 * @since 17/10/16.
 */
public class StreamRecordReaderTest {

  @Test
  public void whenStreamIsNotEmpty_thenThereShouldBeANextRecordToRead() throws Exception {
    Stream<String> stream = Stream.of("a", "b");
    StreamRecordReader<String> streamRecordReader = new StreamRecordReader<>(stream, "source");
    streamRecordReader.open();
    Record<String> record1 = streamRecordReader.readRecord();
    assertThat(record1).isNotNull();
    assertThat(record1.getHeader().getSource().equals("source"));
    assertThat(record1.getPayload().equals("a"));

    Record<String> record2 = streamRecordReader.readRecord();
    assertThat(record2).isNotNull();
    assertThat(record2.getHeader().getSource().equals("source"));
    assertThat(record2.getPayload().equals("b"));

    Record<String> record3 = streamRecordReader.readRecord();
    assertThat(record3).isNull();
  }

  @Test
  public void whenStreamIsEmpty_thenThereShouldBeNoNextRecordToRead() throws Exception {
    Stream<String> stream = Stream.of();
    StreamRecordReader<String> streamRecordReader = new StreamRecordReader<>(stream, "source");
    streamRecordReader.open();
    Record<String> record = streamRecordReader.readRecord();
    assertThat(record).isNull();
  }

}
package org.easybatch.core.filter;

import org.easybatch.core.record.Record;

/**
 * Filter the current record comparing to reference record.
 * 
 * @author Somma Daniele
 *
 * @param <V>
 *          The type of reference record.
 * @param <R>
 *          The type of the record processed.
 */
public abstract class ComparatorRecordFilter<V, R> implements RecordFilter<Record<R>> {

  protected V refRecord;

  protected ComparatorRecordFilter(final V refRecord) {
    super();
    this.refRecord = refRecord;
  }

  @Override
  public Record<R> processRecord(Record<R> record) {
    return compare(record);
  }

  /**
   * The filtering logic.
   * 
   * @param record
   *          The current record
   * @return the current record if is passed the filtering logic,
   *         <code>null</code> otherwise.
   */
  public abstract Record<R> compare(Record<R> record);

}
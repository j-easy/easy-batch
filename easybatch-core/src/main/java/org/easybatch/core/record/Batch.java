package org.easybatch.core.record;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.addAll;

/**
 * Class representing a batch of records.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class Batch implements Iterable<Record> {

    private List<Record> records = new ArrayList<>();

    /**
     * Create a new {@link Batch}.
     */
    public Batch() {
    }

    /**
     * Create a new {@link Batch}.
     *
     * @param records to put in the batch
     */
    public Batch(Record... records) {
        addAll(this.records, records);
    }

    /**
     * Create a new {@link Batch}.
     *
     * @param records to put in the batch
     */
    public Batch(List<Record> records) {
        this.records = records;
    }

    /**
     * Add a record to the batch.
     *
     * @param record to add
     */
    public void addRecord(final Record record) {
        records.add(record);
    }

    /**
     * Remove a record from the batch.
     *
     * @param record to remove
     */
    public void removeRecord(final Record record) {
        records.remove(record);
    }

    /**
     * Check if the batch is empty.
     *
     * @return true if the batch is empty, false otherwise
     */
    public boolean isEmpty() {
        return records.isEmpty();
    }

    /**
     * Get the size of the batch.
     *
     * @return the size of the batch
     */
    public long size() {
        return records.size();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Batch batch = (Batch) o;

        return records.equals(batch.records);

    }

    @Override
    public int hashCode() {
        return records.hashCode();
    }


    @Override
    public Iterator<Record> iterator() {
        return records.iterator();
    }
}

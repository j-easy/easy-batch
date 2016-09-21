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

    public Batch() {
    }

    public Batch(Record... records) {
        addAll(this.records, records);
    }

    public Batch(List<Record> records) {
        this.records = records;
    }

    public void addRecord(final Record record) {
        records.add(record);
    }

    public void removeRecord(final Record record) {
        records.remove(record);
    }

    public boolean isEmpty() {
        return records.isEmpty();
    }

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

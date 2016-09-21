package org.easybatch.core.record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a batch of records.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class Batch {

    private List<Record> records;

    public Batch() {
        this(new ArrayList<>());
    }

    public Batch(Record... records) {
        List<Record> recordList = new ArrayList<>();
        Collections.addAll(recordList, records);
        this.records = recordList;
    }

    public Batch(List<Record> records) {
        this.records = records;
    }

    public List<Record> getRecords() {
        return records;
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


}

/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package org.easybatch.core.record;

import org.easybatch.core.util.Utils;

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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Batch: {");
        stringBuilder.append(Utils.LINE_SEPARATOR);
        for (Record record : records) {
            stringBuilder.append('\t');
            stringBuilder.append(record);
            stringBuilder.append(Utils.LINE_SEPARATOR);
        }
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}

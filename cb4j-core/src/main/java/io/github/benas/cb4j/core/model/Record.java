/*
 * The MIT License
 *
 *  Copyright (c) 2013, benas (md.benhassine@gmail.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package io.github.benas.cb4j.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A model class representing a record.
 * @author benas (md.benhassine@gmail.com)
 */
public abstract class Record {

    /**
     * The record number in the file.
     */
    protected final long number;

    /**
     * The record fields.
     */
    protected final List<Field> fields;

    /**
     * Constructor with a record number.
     * @param number the record number in the file
     */
    public Record(long number) {
        this.number = number;
        this.fields = new ArrayList<Field>();
    }

    /**
     * Getter for a field by its index in the record. The first field is at index 0.
     * @param index the field index
     * @return The Field with index index
     */
    public Field getFieldByIndex(int index) {
        if (index < 0 || index >= fields.size()) {
            throw new IndexOutOfBoundsException("Trying to get field content by index " + index + " which is out of bounds in fields list");
        }
        return fields.get(index);
    }

    /**
     * Getter for a field's content by its index in the record.<br/>
     * This method is a shortcut for getFieldByIndex(index).getContent() . The first field is at index 0
     * @param index the field index
     * @return The Field's content with the given index.
     */
    public String getFieldContentByIndex(int index) {
        if (index < 0 || index >= fields.size()) {
            throw new IndexOutOfBoundsException("Trying to get field content by index " + index + " which is out of bounds in fields list");
        }
        return fields.get(index).getContent();
    }

    /**
     * Getter for the record content as a String.
     * @return The record content as a String
     */
    public abstract String getContentAsString();

    /**
     * Get the record fields as list.
     * @return record fields
     */
    public List<Field> getFields() {
        return fields;
    }

    /**
     * Get the record number in the file.
     * @return record number in the file such as :<br/>
     *  <ul>
     *      <li>first record number is = 1</li>
     *      <li>if configuration parameter input.data.skipHeader = true, the header record is skipped from record numbering. Hence, the second record number is = 1.</li>
     *  </ul>
     */
    public long getNumber() {
        return number;
    }

}

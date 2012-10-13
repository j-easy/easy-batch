/*
 * The MIT License
 *
 *  Copyright (c) 2012, benas (md.benhassine@gmail.com)
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

package net.benas.cb4j.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A model class representing a CSV record.
 * @author benas (md.benhassine@gmail.com)
 */
public final class Record {

    /**
     * The record number in the file.
     */
    private long number;

    /**
     * The fields separator.
     */
    private String separator;

    /**
     * The record fields.
     */
    private List<Field> fields;

    public Record(long number, String separator) {
        this.number = number;
        this.separator = separator;
        this.fields = new ArrayList<Field>();
    }

    /**
     * Getter for a field by its index in the record. The first field is at index 0.
     * @param index the field index
     * @return The Field with index index
     * @throws IndexOutOfBoundsException if the given index is out of range in the fields list
     */
    public Field getFieldByIndex(int index) {
        if (index < 0 || index > fields.size()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bound in fields list");
        }
        return fields.get(index);
    }

    /**
     * Getter for a field's content by its index in the record.<br/>
     * This method is a shortcut for getFieldByIndex(index).getContent() . The first field is at index 0
     * @param index the field index
     * @return The Field's content with the given index.
     * @throws IndexOutOfBoundsException if the given index is out of range in the fields list
     */
    public String getFieldContentByIndex(int index) {
        if (index < 0 || index > fields.size()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bound in fields list");
        }
        return fields.get(index).getContent();
    }

    /**
     * Getter for the record content as a String.
     * @return The record content as a String
     */
    public String getContentAsString() {
        if (fields.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        //for each field, append the field content + separator
        for (int i = 0; i < fields.size() - 1; i++) {
            sb.append(fields.get(i).getContent());
            sb.append(separator);
        }
        sb.append(fields.get(fields.size() - 1).getContent()); //for the last field, append only field content, no separator
        return sb.toString();
    }

    public List<Field> getFields() {
        return fields;
    }

    public long getNumber() {
        return number;
    }
}

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
    private final long number;

    /**
     * The fields separator.
     */
    private final String separator;

    /**
     * Character(s) enclosing raw data in fields
     */
    private final String enclosingCharacter;

    /**
     * The record fields.
     */
    private final List<Field> fields;

    /**
     * Constructor with a number and separator
     * @param number the record number in the file
     * @param separator the field separator
     */
    public Record(long number, String separator, String enclosingCharacter) {
        this.number = number;
        this.separator = separator;
        this.enclosingCharacter = enclosingCharacter;
        this.fields = new ArrayList<Field>();
    }

    /**
     * Getter for a field by its index in the record. The first field is at index 0.
     * @param index the field index
     * @return The Field with index index
     * @throws IndexOutOfBoundsException if the given index is out of range in the fields list
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
     * @throws IndexOutOfBoundsException if the given index is out of range in the fields list
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
    public String getContentAsString() {
        if (fields.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        //for each field, append the field content + separator
        for (int i = 0; i < fields.size() - 1; i++) {
            sb.append(enclosingCharacter);
            sb.append(fields.get(i).getContent());
            sb.append(enclosingCharacter);
            sb.append(separator);
        }
        sb.append(enclosingCharacter).append(fields.get(fields.size() - 1).getContent()).append(enclosingCharacter); //for the last field, append only field content, no separator
        return sb.toString();
    }

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

    /**
     * Get field separator.
     * @return field separator in the record
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * Get data enclosure character
     * @return data enclosure character
     */
    public String getEnclosingCharacter() {
        return enclosingCharacter;
    }

}

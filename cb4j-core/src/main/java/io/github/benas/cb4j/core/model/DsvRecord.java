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

/**
 * A model class representing a delimiter-separated values record.
 * @author benas (md.benhassine@gmail.com)
 */
public final class DsvRecord extends Record {

    /**
     * Fields delimiter.
     */
    private final String delimiter;

    /**
     * Qualifier Character(s) enclosing raw data in fields
     */
    private final String qualifier;

    public DsvRecord(long number, String delimiter, String qualifier) {
        super(number);
        this.delimiter = delimiter;
        this.qualifier = qualifier;
    }

    /**
     * {@inheritDoc}
     */
    public String getContentAsString() {
        if (fields.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        //for each field, append the field content + delimiter
        for (int i = 0; i < fields.size() - 1; i++) {
            sb.append(qualifier);
            sb.append(fields.get(i).getContent());
            sb.append(qualifier);
            sb.append(delimiter);
        }
        sb.append(qualifier).append(fields.get(fields.size() - 1).getContent()).append(qualifier); //for the last field, append only field content, no separator
        return sb.toString();
    }

    /**
     * Get field delimiter.
     * @return field delimiter in the record
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * Get qualifier character.
     * @return data qualifier
     */
    public String getQualifier() {
        return qualifier;
    }

}

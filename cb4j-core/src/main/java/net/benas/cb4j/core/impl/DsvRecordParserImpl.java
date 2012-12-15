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

package net.benas.cb4j.core.impl;

import net.benas.cb4j.core.api.RecordParser;
import net.benas.cb4j.core.model.DsvRecord;
import net.benas.cb4j.core.model.Field;
import net.benas.cb4j.core.model.Record;

/**
 * Implementation of {@link RecordParser} to parse Delimiter-separated values.<br/>
 * This class is not intended to be used nor extended by framework users
 *
 * @author benas (md.benhassine@gmail.com)
 */
public final class DsvRecordParserImpl implements RecordParser {

    /**
     * The number of fields in the record.
     */
    private int fieldNumber;

    /**
     * The field delimiter.
     */
    private String fieldDelimiter;

    /**
     * Parameter to trim whitespaces.
     */
    private boolean trimWhitespaces;

    /**
     * Character(s) enclosing raw data in fields.
     */
    private String qualifier;

    public DsvRecordParserImpl(final int fieldNumber, final String fieldDelimiter, final boolean trimWhitespaces, final String qualifier) {

        this.fieldNumber = fieldNumber;
        String prefix = "";
        if ("|".equalsIgnoreCase(fieldDelimiter)) { //escape the "pipe" character used in regular expression of String.split method
            prefix = "\\";
        }
        this.fieldDelimiter = prefix + fieldDelimiter;
        this.trimWhitespaces = trimWhitespaces;
        this.qualifier = qualifier;
    }

    /**
     * {@inheritDoc}
     */
    public String analyseRecord(final String record) {

        String[] tokens = record.split(fieldDelimiter, -1);

        if (tokens.length != fieldNumber) {
            return "record size " + tokens.length + " not equal to expected size of " + fieldNumber;
        }

        if (qualifier.length() > 0) {
            for (String token : tokens) {
                if (!token.startsWith(qualifier) || !token.endsWith(qualifier)) {
                    return "field [" + token + "] is not enclosed as expected with '" + qualifier + "'";
                }
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Record parseRecord(final String stringRecord, final long currentRecordNumber) {
        Record record = new DsvRecord(currentRecordNumber, fieldDelimiter, qualifier);
        String[] tokens = stringRecord.split(fieldDelimiter, -1);
        int i = 0;
        for (String token : tokens) {
            if (trimWhitespaces) {
                token = token.trim();
            }
            final int qualifierLength = qualifier.length();
            if (qualifierLength > 0) {
                token = token.substring(qualifierLength, token.length() - qualifierLength);
            }
            Field field = new Field(i++, token);
            record.getFields().add(field);
        }
        return record;
    }

}

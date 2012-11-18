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
import net.benas.cb4j.core.model.Field;
import net.benas.cb4j.core.model.Record;

/**
 * Implementation of {@link RecordParser}.<br/>
 * This class is not intended to be used nor extended by framework users
 *
 * @author benas (md.benhassine@gmail.com)
 */
public final class RecordParserImpl implements RecordParser {

    /**
     * The number of fields in the record.
     */
    private int fieldNumber;

    /**
     * The field separator.
     */
    private String fieldSeparator;

    /**
     * Parameter to trim whitespaces.
     */
    private boolean trimWhitespaces;

    /**
     * Character(s) enclosing raw data in fields
     */
    private String enclosingCharacter;

    public RecordParserImpl(final int fieldNumber, final String fieldSeparator, final boolean trimWhitespaces, final String enclosingCharacter) {

        this.fieldNumber = fieldNumber;
        String prefix = "";
        if ("|".equalsIgnoreCase(fieldSeparator)) { //escape the "pipe" character used in regular expression of String.split method
            prefix = "\\";
        }
        this.fieldSeparator = prefix + fieldSeparator;
        this.trimWhitespaces = trimWhitespaces;
        this.enclosingCharacter = enclosingCharacter;
    }

    /**
     * {@inheritDoc}
     */
    public String analyseRecord(final String record) {

        String[] tokens = record.split(fieldSeparator, -1);

        if (tokens.length != fieldNumber) {
            return "record size " + tokens.length + " not equal to expected size of " + fieldNumber;
        }

        if (enclosingCharacter.length() > 0) {
            for (String token : tokens) {
                if ( !token.startsWith(enclosingCharacter) || !token.endsWith(enclosingCharacter)) {
                    return "field [" + token + "] is not enclosed as expected with '" + enclosingCharacter + "'";
                }
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public int getRecordSize(final String record) {
        String[] tokens = record.split(fieldSeparator, -1);
        return tokens.length;
    }

    /**
     * {@inheritDoc}
     */
    public Record parseRecord(final String stringRecord, final long currentRecordNumber) {
        Record record = new Record(currentRecordNumber, fieldSeparator, enclosingCharacter);
        String[] tokens = stringRecord.split(fieldSeparator, -1);
        int i = 0;
        for (String token : tokens) {
            if (trimWhitespaces) {
                token = token.trim();
            }
            if (enclosingCharacter.length() > 0) {
                token = token.substring(enclosingCharacter.length(), token.length() - enclosingCharacter.length());
            }
            Field field = new Field(i++, token);
            record.getFields().add(field);
        }
        return record;
    }

}

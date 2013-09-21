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

package io.github.benas.cb4j.core.impl;

import io.github.benas.cb4j.core.api.RecordParser;
import io.github.benas.cb4j.core.model.Field;
import io.github.benas.cb4j.core.model.FlrRecord;
import io.github.benas.cb4j.core.model.Record;

/**
 * Implementation of {@link io.github.benas.cb4j.core.api.RecordParser} to parse fixed length records.<br/>
 * This class is not intended to be used nor extended by framework users
 *
 * @author benas (md.benhassine@gmail.com)
 */
public final class FlrRecordParserImpl implements RecordParser {

    /**
     * Fields length array.
     */
    private int[] fieldsLength;

    /**
     * Fields offsets array.
     */
    private int[] fieldsOffsets;

    /**
     * Total number of characters expected based on declared fields length
     */
    private int recordExpectedLength;

    public FlrRecordParserImpl(final int[] fieldsLength) {
        this.fieldsLength = fieldsLength;
        for (int fieldLength : fieldsLength) {
            recordExpectedLength += fieldLength;
        }
        fieldsOffsets = calculateOffsets(fieldsLength);
    }

    /**
     * {@inheritDoc}
     */
    public String analyseRecord(final String record) {

        int recordLength = record.length();

        if (recordLength != recordExpectedLength) {
            return "record length " + recordLength + " not equal to expected length of " + recordExpectedLength;
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Record parseRecord(final String stringRecord, final long currentRecordNumber) {
        Record record = new FlrRecord(currentRecordNumber, fieldsLength);
        for (int i = 0; i < fieldsLength.length; i++) {
            String token = stringRecord.substring(fieldsOffsets[i], fieldsOffsets[i + 1]);
            Field field = new Field(i, token);
            record.getFields().add(field);
        }
        return record;
    }

    /**
     * utility method to calculate field offsets used to extract fields from record.
     * @param lengths fields length array
     * @return offsets array
     */
    private int[] calculateOffsets(int[] lengths) {
        int[] offsets = new int[ lengths.length + 1 ];
        offsets[0] = 0;
        for (int i = 0; i < lengths.length; i++) {
            offsets[i + 1] = offsets[i] + lengths[i];
        }
        return offsets;
    }

}

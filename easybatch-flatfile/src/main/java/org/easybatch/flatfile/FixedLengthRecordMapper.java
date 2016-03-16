/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.flatfile;

import org.easybatch.core.mapper.AbstractRecordMapper;
import org.easybatch.core.mapper.ObjectMapper;
import org.easybatch.core.mapper.RecordMapper;
import org.easybatch.core.mapper.RecordMappingException;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.StringRecord;

import java.util.HashMap;
import java.util.Map;

/**
 * Fixed Length Record to Object mapper implementation.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class FixedLengthRecordMapper extends AbstractRecordMapper implements RecordMapper<StringRecord, GenericRecord> {

    /**
     * Fields length array.
     */
    private int[] fieldsLength;

    /**
     * Fields offsets array.
     */
    private int[] fieldsOffsets;

    /**
     * Array of field names.
     */
    private String[] fieldNames;

    /**
     * Total number of characters expected based on declared fields length
     */
    private int recordExpectedLength;

    /**
     * Default whitespace trimming value.
     */
    public static final boolean DEFAULT_WHITESPACE_TRIMMING = false;

    /**
     * Parameter to trim whitespaces.
     */
    private boolean trimWhitespaces = DEFAULT_WHITESPACE_TRIMMING;

    /**
     * Create a {@link FixedLengthRecordMapper} instance.
     *
     * @param recordClass  the target domain object class
     * @param fieldsLength an array of fields length in the same order in the FLR flat file.
     * @param fieldNames   a String array representing fields name in the same order in the FLR flat file.
     */
    public FixedLengthRecordMapper(Class recordClass, int[] fieldsLength, String[] fieldNames) {
        super(recordClass);
        this.fieldsLength = fieldsLength.clone();
        this.fieldNames = fieldNames.clone();
        objectMapper = new ObjectMapper(recordClass);
        for (int fieldLength : fieldsLength) {
            recordExpectedLength += fieldLength;
        }
        fieldsOffsets = calculateOffsets(fieldsLength);
    }

    @Override
    public GenericRecord processRecord(final StringRecord record) throws RecordMappingException {

        FlatFileRecord flatFileRecord = parseRecord(record);
        Map<String, String> fieldsContents = new HashMap<>();
        for (FlatFileField flatFileField : flatFileRecord.getFlatFileFields()) {
            String fieldName = fieldNames[flatFileField.getIndex()];
            String fieldValue = flatFileField.getRawContent();
            fieldsContents.put(fieldName, fieldValue);
        }
        return new GenericRecord(record.getHeader(), objectMapper.mapObject(fieldsContents));
    }

    FlatFileRecord parseRecord(final StringRecord record) throws RecordMappingException {

        String payload = record.getPayload();
        int recordLength = payload.length();

        if (recordLength != recordExpectedLength) {
            throw new RecordMappingException("record length " + recordLength + " not equal to expected length of " + recordExpectedLength);
        }

        FlatFileRecord flatFileRecord = new FlatFileRecord(record.getHeader(), payload);
        for (int i = 0; i < fieldsLength.length; i++) {
            String token = payload.substring(fieldsOffsets[i], fieldsOffsets[i + 1]);
            token = trimWhitespaces(token);
            FlatFileField flatFileField = new FlatFileField(i, token);
            flatFileRecord.getFlatFileFields().add(flatFileField);
        }

        return flatFileRecord;
    }


    /**
     * utility method to calculate field offsets used to extract fields from record.
     *
     * @param lengths fields length array
     * @return offsets array
     */
    private int[] calculateOffsets(final int[] lengths) {
        int[] offsets = new int[lengths.length + 1];
        offsets[0] = 0;
        for (int i = 0; i < lengths.length; i++) {
            offsets[i + 1] = offsets[i] + lengths[i];
        }
        return offsets;
    }

    private String trimWhitespaces(final String token) {
        if (trimWhitespaces) {
            return token.trim();
        }
        return token;
    }

    /**
     * Trim white spaces when parsing the fixed length record.
     *
     * @param trimWhitespaces true if whitespaces should be trimmed
     */
    public void setTrimWhitespaces(final boolean trimWhitespaces) {
        this.trimWhitespaces = trimWhitespaces;
    }

}

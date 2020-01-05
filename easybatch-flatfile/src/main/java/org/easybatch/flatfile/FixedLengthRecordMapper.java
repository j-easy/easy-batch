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
package org.easybatch.flatfile;

import org.easybatch.core.mapper.AbstractRecordMapper;
import org.easybatch.core.mapper.ObjectMapper;
import org.easybatch.core.mapper.RecordMapper;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fixed Length Record to Object mapper implementation.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class FixedLengthRecordMapper<P> extends AbstractRecordMapper<P> implements RecordMapper<StringRecord, Record<P>> {

    /**
     * Default whitespace trimming value.
     */
    public static final boolean DEFAULT_WHITESPACE_TRIMMING = false;

    private int[] fieldsLength;
    private int[] fieldsOffsets;
    private String[] fieldNames;
    private int recordExpectedLength;
    private boolean trimWhitespaces = DEFAULT_WHITESPACE_TRIMMING;

    /**
     * Create a new {@link FixedLengthRecordMapper} instance.
     *
     * @param recordClass  the target domain object class
     * @param fieldsLength an array of fields length in the same order in the FLR flat file.
     * @param fieldNames   a String array representing fields name in the same order in the FLR flat file.
     */
    public FixedLengthRecordMapper(Class<P> recordClass, int[] fieldsLength, String[] fieldNames) {
        super(recordClass);
        this.fieldsLength = fieldsLength.clone();
        this.fieldNames = fieldNames.clone();
        objectMapper = new ObjectMapper<>(recordClass);
        for (int fieldLength : fieldsLength) {
            recordExpectedLength += fieldLength;
        }
        fieldsOffsets = calculateOffsets(fieldsLength);
    }

    @Override
    public Record<P> processRecord(final StringRecord record) throws Exception {

        List<Field> fields = parseRecord(record);
        Map<String, String> fieldsContents = new HashMap<>();
        for (Field field : fields) {
            String fieldName = fieldNames[field.getIndex()];
            String fieldValue = field.getRawContent();
            fieldsContents.put(fieldName, fieldValue);
        }
        return new GenericRecord<>(record.getHeader(), objectMapper.mapObject(fieldsContents));
    }

    protected List<Field> parseRecord(final StringRecord record) throws Exception {

        String payload = record.getPayload();
        int recordLength = payload.length();

        if (recordLength != recordExpectedLength) {
            throw new Exception("record length " + recordLength + " not equal to expected length of " + recordExpectedLength);
        }

        List<Field> fields = new ArrayList<>();
        for (int i = 0; i < fieldsLength.length; i++) {
            String token = payload.substring(fieldsOffsets[i], fieldsOffsets[i + 1]);
            token = trimWhitespaces(token);
            Field field = new Field(i, token);
            fields.add(field);
        }

        return fields;
    }


    // utility method to calculate field offsets used to extract fields from record.
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

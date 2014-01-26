/*
 * The MIT License
 *
 *   Copyright (c) 2014, benas (md.benhassine@gmail.com)
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

package io.github.benas.easybatch.flatfile.flr;

import io.github.benas.easybatch.core.api.Record;
import io.github.benas.easybatch.core.api.RecordMapper;
import io.github.benas.easybatch.core.mapper.ObjectMapper;
import io.github.benas.easybatch.flatfile.FlatFileField;
import io.github.benas.easybatch.flatfile.FlatFileRecord;
import io.github.benas.easybatch.core.converter.TypeConverter;

/**
 * Fixed Length Record to Object mapper implementation.
 *
 * @param <T> the target domain object type
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class FixedLengthRecordMapper<T> implements RecordMapper<T> {

    private ObjectMapper<T> objectMapper;

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

    /**
     * Constructs a FixedLengthRecordMapper instance.
     * @param recordClass the target domain object class
     * @param fieldsLength an array of fields length in the same order in the FLR flat file.
     * @param fieldsMapping a String array representing fields name in the same order in the FLR flat file.
     */
    public FixedLengthRecordMapper(Class<? extends T> recordClass, int[] fieldsLength, String[] fieldsMapping) {
        this.fieldsLength = fieldsLength.clone();
        objectMapper = new ObjectMapper<T>(recordClass, fieldsMapping);
        for (int fieldLength : fieldsLength) {
            recordExpectedLength += fieldLength;
        }
        fieldsOffsets = calculateOffsets(fieldsLength);
    }

    @Override
    public T mapRecord(final Record record) throws Exception {

        FlatFileRecord flatFileRecord = parseRecord(record);
        String[] fieldsContents = new String[flatFileRecord.getFlatFileFields().size()];
        for (FlatFileField flatFileField : flatFileRecord.getFlatFileFields()) {
            fieldsContents[flatFileField.getIndex()] = flatFileField.getRawContent();
        }
        return objectMapper.mapObject(fieldsContents);
    }

    public FlatFileRecord parseRecord(final Record record) throws Exception {

        String recordRawContent = (String) record.getRawContent();
        int recordLength = recordRawContent.length();

        if (recordLength != recordExpectedLength) {
            throw new Exception("record length " + recordLength + " not equal to expected length of " + recordExpectedLength);
        }

        FlatFileRecord flatFileRecord = new FlatFileRecord(record.getNumber(), recordRawContent);
        for (int i = 0; i < fieldsLength.length; i++) {
            String token = recordRawContent.substring(fieldsOffsets[i], fieldsOffsets[i + 1]);
            FlatFileField flatFileField = new FlatFileField(i, token);
            flatFileRecord.getFlatFileFields().add(flatFileField);
        }

        return flatFileRecord;
    }


    /**
     * utility method to calculate field offsets used to extract fields from record.
     * @param lengths fields length array
     * @return offsets array
     */
    private int[] calculateOffsets(final int[] lengths) {
        int[] offsets = new int[ lengths.length + 1 ];
        offsets[0] = 0;
        for (int i = 0; i < lengths.length; i++) {
            offsets[i + 1] = offsets[i] + lengths[i];
        }
        return offsets;
    }

    /**
     * Register a custom type converter.
     * @param type the target type
     * @param typeConverter the type converter to user
     */
    public void registerTypeConverter(final Class type, final TypeConverter typeConverter) {
        objectMapper.registerTypeConverter(type, typeConverter);
    }

}

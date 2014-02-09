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

package io.github.benas.easybatch.flatfile.dsv;

import io.github.benas.easybatch.core.api.Record;
import io.github.benas.easybatch.core.api.RecordMapper;
import io.github.benas.easybatch.core.mapper.ObjectMapper;
import io.github.benas.easybatch.flatfile.FlatFileField;
import io.github.benas.easybatch.flatfile.FlatFileRecord;
import io.github.benas.easybatch.core.converter.TypeConverter;

import java.util.Arrays;
import java.util.List;

/**
 * DSV to Object mapper implementation.
 *
 * @param <T> the target domain object type
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class DelimitedRecordMapper<T> implements RecordMapper<T> {

    /**
     * The default delimiter.
     */
    public static final String DEFAULT_DELIMITER = ",";

    /**
     * The default data qualifier.
     */
    public static final String DEFAULT_QUALIFIER = "";

    /**
     * Default whitespace trimming value.
     */
    public static final boolean DEFAULT_WHITESPACE_TRIMMING = true;

    /**
     * Fields delimiter.
     */
    private String delimiter;

    /**
     * Parameter to trim whitespaces.
     */
    private boolean trimWhitespaces;

    /**
     * Data qualifier.
     */
    private String qualifier;

    /**
     * The object mapper.
     */
    private ObjectMapper<T> objectMapper;

    /**
     * Total number of characters expected based on declared fields length.
     */
    private int recordExpectedLength;

    /**
     * Array of indexes of fields to retain.
     */
    private List<Integer> fieldsPositions;

    /**
     * private default constructor to initialize the mapper with default parameter values.
     */
    private DelimitedRecordMapper() {
        this.delimiter = DEFAULT_DELIMITER;
        this.qualifier = DEFAULT_QUALIFIER;
        this.trimWhitespaces = DEFAULT_WHITESPACE_TRIMMING;
    }

    /**
     * Constructs a default DelimitedRecordMapper instance. Column names and expected record size will be calculated from the header record.
     * and set to fields with the same name of the target object.
     * @param recordClass the target domain object class
     */
    public DelimitedRecordMapper(final Class<? extends T> recordClass) {
        this();
        objectMapper = new ObjectMapper<T>(recordClass);
    }

    /**
     * Constructs a DelimitedRecordMapper instance. Expected record size will be calculated from the header record.
     * @param recordClass the target domain object class
     * @param fieldsMapping a String array containing target type field names in the same order as in the delimited flat file.
     */
    public DelimitedRecordMapper(final Class<? extends T> recordClass, final String[] fieldsMapping) {
        this();
        recordExpectedLength = fieldsMapping.length;
        objectMapper = new ObjectMapper<T>(recordClass, fieldsMapping);
    }

    /**
     * Constructs a DelimitedRecordMapper instance. Column names and expected record size will be calculated from the header record.
     * @param recordClass the target domain object class
     * @param fieldsPositions array of indexes of fields to retain
     */
    public DelimitedRecordMapper(final Class<? extends T> recordClass, final Integer[] fieldsPositions) {
        this();
        this.fieldsPositions = Arrays.asList(fieldsPositions);
        objectMapper = new ObjectMapper<T>(recordClass);
    }

    /**
     * Constructs a DelimitedRecordMapper instance. Column names will be calculated from the header record.
     * @param recordClass the target domain object class
     * @param fieldsPositions array of indexes of fields to retain
     * @param recordExpectedLength record expected length
     */
    public DelimitedRecordMapper(final Class<? extends T> recordClass, final Integer[] fieldsPositions,
                                 final int recordExpectedLength) {
        this();
        this.fieldsPositions = Arrays.asList(fieldsPositions);
        this.recordExpectedLength = recordExpectedLength;
        objectMapper = new ObjectMapper<T>(recordClass);
    }

    /**
     * Constructs a DelimitedRecordMapper instance. Expected record size will be calculated from the header record.
     * @param recordClass the target domain object class
     * @param fieldsPositions array of indexes of fields to retain
     * @param fieldsMapping a String array representing fields name in the same order in the DSV flat file.
     */
    public DelimitedRecordMapper(final Class<? extends T> recordClass, final Integer[] fieldsPositions, final String[] fieldsMapping) {
        this();
        this.fieldsPositions = Arrays.asList(fieldsPositions);
        objectMapper = new ObjectMapper<T>(recordClass, fieldsMapping);
    }

    /**
     * Constructs a DelimitedRecordMapper instance.
     * @param recordClass the target domain object class
     * @param fieldsPositions array of indexes of fields to retain
     * @param fieldsMapping a String array representing fields name in the same order in the DSV flat file.
     * @param recordExpectedLength record expected length
     */
    public DelimitedRecordMapper(final Class<? extends T> recordClass, final Integer[] fieldsPositions,
                                 final String[] fieldsMapping, final int recordExpectedLength) {
        this();
        this.fieldsPositions = Arrays.asList(fieldsPositions);
        this.recordExpectedLength = recordExpectedLength;
        objectMapper = new ObjectMapper<T>(recordClass, fieldsMapping);
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
        String[] tokens = recordRawContent.split(delimiter, -1);

        // convention over configuration : if expected record size is not specified, calculate it from the header record
        if (this.recordExpectedLength == 0) {
            this.recordExpectedLength = tokens.length;
        }

        // convention over configuration : if fields mapping is not specified, retrieve it from the header record (done only once)
        if (objectMapper.getHeadersMapping() == null) {
            String[] fieldsMapping = new String[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                String token = tokens[i];
                if (trimWhitespaces) {
                    token = token.trim();
                }
                final int qualifierLength = qualifier.length();
                if (qualifierLength > 0) {
                    token = token.substring(qualifierLength, token.length() - qualifierLength);
                }
                fieldsMapping[i] = token.toLowerCase();
            }

            String[] headersMapping;
            if (fieldsPositions != null) {
                headersMapping = new String[fieldsPositions.size()];
                int j = 0;
                for (int i = 0; i < fieldsMapping.length; i++) {
                    if (fieldsPositions.contains(i + 1)) {
                        headersMapping[j++] = fieldsMapping[i];
                    }
                }
            } else {
                headersMapping = fieldsMapping;
            }

            objectMapper.setHeadersMapping(headersMapping);
        }

        if (tokens.length != recordExpectedLength) {
            throw new Exception("record length (" + tokens.length + " fields) not equal to expected length of "
                    + recordExpectedLength + " fields");
        }

        if (qualifier.length() > 0) {
            for (String token : tokens) {
                if (!token.startsWith(qualifier) || !token.endsWith(qualifier)) {
                    throw new Exception("field [" + token + "] is not enclosed as expected with '" + qualifier + "'");
                }
            }
        }

        FlatFileRecord flatFileRecord = new FlatFileRecord(record.getNumber(), recordRawContent);
        int i = 0;
        int j = 0;
        for (String token : tokens) {
            if (trimWhitespaces) {
                token = token.trim();
            }
            final int qualifierLength = qualifier.length();
            if (qualifierLength > 0) {
                token = token.substring(qualifierLength, token.length() - qualifierLength);
            }
            if (fieldsPositions != null && !fieldsPositions.contains(++j)) {
                continue;
            }
            flatFileRecord.getFlatFileFields().add(new FlatFileField(i++, token));
        }

        return flatFileRecord;
    }

    /**
     * Set the delimiter to use.
     * @param delimiter the delimiter to use
     */
    public void setDelimiter(final String delimiter) {
        String prefix = "";
        //escape the "pipe" character used in regular expression of String.split method
        if ("|".equals(delimiter)) {
            prefix = "\\";
        }
        this.delimiter = prefix + delimiter;
    }

    /**
     * Trim white spaces when parsing the DSV record.
     * @param trimWhitespaces true if whitespaces should be trimmed
     */
    public void setTrimWhitespaces(final boolean trimWhitespaces) {
        this.trimWhitespaces = trimWhitespaces;
    }

    /**
     * Set the data qualifier to use.
     * @param qualifier the data qualifier to use.
     */
    public void setQualifier(final String qualifier) {
        this.qualifier = qualifier;
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

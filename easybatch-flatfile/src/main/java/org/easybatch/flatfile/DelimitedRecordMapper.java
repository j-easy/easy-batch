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

import java.util.*;

/**
 * DSV to Object mapper implementation.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class DelimitedRecordMapper extends AbstractRecordMapper implements RecordMapper<StringRecord, GenericRecord> {

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
    public static final boolean DEFAULT_WHITESPACE_TRIMMING = false;

    /**
     * Fields delimiter.
     */
    private String delimiter = DEFAULT_DELIMITER;

    /**
     * Parameter to trim whitespaces.
     */
    private boolean trimWhitespaces = DEFAULT_WHITESPACE_TRIMMING;

    /**
     * Data qualifier.
     */
    private String qualifier = DEFAULT_QUALIFIER;

    /**
     * Total number of fields expected per record.
     */
    private int recordExpectedLength;

    /**
     * Array of indexes of fields to retain.
     */
    private List<Integer> fieldsPositions;

    /**
     * Array of field names.
     */
    private String[] fieldNames;

    /**
     * Flag telling if field names have been retrieved from the header record.
     */
    private boolean fieldNamesRetrievedFromHeader;

    /**
     * Create a {@link DelimitedRecordMapper} instance.
     * Column names and expected record size will be calculated from the header record.
     * and set to fields with the same name of the target object.
     *
     * @param recordClass the target domain object class
     */
    public DelimitedRecordMapper(final Class recordClass) {
        super(recordClass);
        objectMapper = new ObjectMapper(recordClass);
    }

    /**
     * Create a {@link DelimitedRecordMapper} instance.
     * Expected record size will be calculated from the header record.
     *
     * @param recordClass the target domain object class
     * @param fieldNames  a String array containing target type field names in the same order as in the delimited flat file.
     */
    public DelimitedRecordMapper(final Class recordClass, String... fieldNames) {
        this(recordClass);
        this.fieldNames = fieldNames;
        this.recordExpectedLength = fieldNames.length;
    }

    /**
     * Create a {@link DelimitedRecordMapper} instance.
     *
     * @param recordClass          the target domain object class
     * @param fieldNames           a String array containing target type field names in the same order as in the delimited flat file.
     * @param recordExpectedLength record expected length
     */
    public DelimitedRecordMapper(final Class recordClass, final String[] fieldNames, final int recordExpectedLength) {
        this(recordClass, fieldNames);
        this.recordExpectedLength = recordExpectedLength;
    }

    /**
     * Create a {@link DelimitedRecordMapper} instance.
     * Expected record size will be calculated from the header record.
     *
     * @param recordClass     the target domain object class
     * @param fieldsPositions array of indexes of fields to retain
     */
    public DelimitedRecordMapper(final Class recordClass, final Integer... fieldsPositions) {
        this(recordClass);
        this.fieldsPositions = Arrays.asList(fieldsPositions);
    }

    /**
     * Create a {@link DelimitedRecordMapper} instance.
     * Expected record size will be calculated from the header record.
     *
     * @param recordClass     the target domain object class
     * @param fieldsPositions array of indexes of fields to retain
     * @param fieldNames      a String array representing fields name in the same order in the DSV flat file.
     */
    public DelimitedRecordMapper(final Class recordClass, final Integer[] fieldsPositions, final String[] fieldNames) {
        this(recordClass);
        this.fieldNames = fieldNames;
        this.fieldsPositions = Arrays.asList(fieldsPositions);
    }

    /**
     * Create a {@link DelimitedRecordMapper} instance.
     *
     * @param recordClass          the target domain object class
     * @param fieldsPositions      array of indexes of fields to retain
     * @param fieldNames           a String array representing fields name in the same order in the DSV flat file.
     * @param recordExpectedLength record expected length
     */
    public DelimitedRecordMapper(final Class recordClass, final Integer[] fieldsPositions, final String[] fieldNames, final int recordExpectedLength) {
        this(recordClass, fieldsPositions, fieldNames);
        this.recordExpectedLength = recordExpectedLength;
    }

    @Override
    public GenericRecord processRecord(final StringRecord record) throws RecordMappingException {
        FlatFileRecord flatFileRecord = parseRecord(record);
        Map<String, String> fieldsContents = new HashMap<>();
        int index = 0;
        for (FlatFileField flatFileField : flatFileRecord.getFlatFileFields()) {
            String fieldName;
            if (fieldNamesRetrievedFromHeader) {
                fieldName = fieldNames[flatFileField.getIndex()];
            } else {
                fieldName = fieldNames[index++];
            }
            String fieldValue = flatFileField.getRawContent();
            fieldsContents.put(fieldName, fieldValue);
        }
        return new GenericRecord(record.getHeader(), objectMapper.mapObject(fieldsContents));
    }

    FlatFileRecord parseRecord(final StringRecord record) throws RecordMappingException {

        String payload = record.getPayload();
        String[] tokens = payload.split(delimiter, -1);

        setRecordExpectedLength(tokens);

        setFieldNames(tokens);

        checkRecordLength(tokens);

        checkQualifier(tokens);

        List<FlatFileField> fields = new ArrayList<>();
        int index = 0;
        for (String token : tokens) {
            token = trimWhitespaces(token);
            token = removeQualifier(token);
            fields.add(new FlatFileField(index++, token));
        }

        FlatFileRecord flatFileRecord = new FlatFileRecord(record.getHeader(), payload);
        if (fieldsPositions != null) {
            filterFields(fields);
        }
        flatFileRecord.getFlatFileFields().addAll(fields);
        return flatFileRecord;
    }

    private void checkQualifier(String[] tokens) throws RecordMappingException {
        if (qualifier.length() > 0) {
            for (String token : tokens) {
                if (!token.startsWith(qualifier) || !token.endsWith(qualifier)) {
                    throw new RecordMappingException("field [" + token + "] is not enclosed as expected with '" + qualifier + "'");
                }
            }
        }
    }

    private void checkRecordLength(String[] tokens) throws RecordMappingException {
        if (tokens.length != recordExpectedLength) {
            throw new RecordMappingException("record length (" + tokens.length + " fields) not equal to expected length of "
                    + recordExpectedLength + " fields");
        }
    }

    private void setFieldNames(String[] tokens) {
        // convention over configuration : if field names are not specified, retrieve them from the header record (done only once)
        if (fieldNames == null) {
            fieldNamesRetrievedFromHeader = true;
            fieldNames = new String[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                String token = tokens[i];
                token = trimWhitespaces(token);
                token = removeQualifier(token);
                fieldNames[i] = token;
            }
        }
    }

    private void setRecordExpectedLength(String[] tokens) {
        // convention over configuration : if expected record size is not specified, calculate it from the header record
        if (this.recordExpectedLength == 0) {
            this.recordExpectedLength = tokens.length;
        }
    }

    private void filterFields(List<FlatFileField> fields) {
        Iterator<FlatFileField> iterator = fields.iterator();
        while (iterator.hasNext()) {
            int index = iterator.next().getIndex();
            if (!fieldsPositions.contains(index)) {
                iterator.remove();
            }
        }
    }

    private String trimWhitespaces(final String token) {
        if (trimWhitespaces) {
            return token.trim();
        }
        return token;
    }

    private String removeQualifier(final String token) {
        int qualifierLength = qualifier.length();
        if (qualifierLength > 0) {
            return token.substring(qualifierLength, token.length() - qualifierLength);
        }
        return token;
    }

    /*
     * Public setters for parameters
     */

    /**
     * Set the delimiter to use.
     *
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
     *
     * @param trimWhitespaces true if whitespaces should be trimmed
     */
    public void setTrimWhitespaces(final boolean trimWhitespaces) {
        this.trimWhitespaces = trimWhitespaces;
    }

    /**
     * Set the data qualifier to use.
     *
     * @param qualifier the data qualifier to use.
     */
    public void setQualifier(final String qualifier) {
        this.qualifier = qualifier;
    }

}

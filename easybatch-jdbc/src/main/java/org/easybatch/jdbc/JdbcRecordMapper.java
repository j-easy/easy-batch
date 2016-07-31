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

package org.easybatch.jdbc;

import org.easybatch.core.mapper.AbstractRecordMapper;
import org.easybatch.core.mapper.RecordMapper;
import org.easybatch.core.mapper.RecordMappingException;
import org.easybatch.core.record.GenericRecord;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link RecordMapper} that maps database rows to domain objects.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JdbcRecordMapper extends AbstractRecordMapper implements RecordMapper<JdbcRecord, GenericRecord> {

    /**
     * Field names used for custom column mapping.
     */
    private String[] fields;

    /**
     * Constructs a default JdbcRecordMapper instance. Column names will be fetched from the jdbc result set meta data
     * and set to fields with the same name of the target object.
     *
     * @param recordClass the target domain object class
     */
    public JdbcRecordMapper(final Class recordClass) {
        super(recordClass);
    }

    /**
     * Constructs a JdbcRecordMapper. The supplied field names will be used to map columns to the target object fields.
     *
     * @param recordClass the target domain object class
     * @param fields      the list of fields names
     */
    public JdbcRecordMapper(final Class recordClass, final String... fields) {
        this(recordClass);
        this.fields = fields;
    }

    @Override
    public GenericRecord processRecord(final JdbcRecord record) throws RecordMappingException {

        ResultSet resultSet = record.getPayload();

        try {
            initFieldNames(resultSet);

            Map<String, String> values = new HashMap<>();
            for (int i = 0; i < fields.length; i++) {
                values.put(fields[i], resultSet.getString(i + 1));
            }
            return new GenericRecord(record.getHeader(), objectMapper.mapObject(values));
        } catch (SQLException e) {
            throw new RecordMappingException("Unable to map record " + record + " to target type", e);
        }

    }

    /**
     * When no field names are supplied, fetch column names from the ResultSet meta data.
     *
     * @param resultSet the result set to fetch column names.
     * @throws SQLException thrown if not able to get ResultSet meta data
     */
    private void initFieldNames(final ResultSet resultSet) throws SQLException {
        int columnCount = resultSet.getMetaData().getColumnCount();
        if (fields == null) {
            fields = new String[columnCount];
            for (int i = 1; i < columnCount + 1; i++) {
                fields[i - 1] = resultSet.getMetaData().getColumnLabel(i);
            }
        }
    }
}

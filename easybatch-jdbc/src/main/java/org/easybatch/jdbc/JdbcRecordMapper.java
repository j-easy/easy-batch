/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordMapper;
import org.easybatch.core.converter.TypeConverter;
import org.easybatch.core.mapper.ObjectMapper;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link org.easybatch.core.api.RecordMapper} that maps database rows to domain objects.
 *
 * @param <T> the target domain object type.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JdbcRecordMapper<T> implements RecordMapper<T> {

    /**
     * The object mapper.
     */
    private ObjectMapper<T> objectMapper;

    /**
     * Constructs a default JdbcRecordMapper instance. Column names will be fetched from the jdbc result set meta data
     * and set to fields with the same name of the target object.
     * @param recordClass the target domain object class
     */
    public JdbcRecordMapper(final Class<? extends T> recordClass) {
        objectMapper = new ObjectMapper<T>(recordClass);
    }

    @Override
    public T mapRecord(final Record record) throws Exception {

        JdbcRecord jdbcRecord = (JdbcRecord) record;
        ResultSet resultSet = jdbcRecord.getPayload();
        int columnCount = resultSet.getMetaData().getColumnCount();

        Map<String, String> values = new HashMap<String, String>();
        for (int i = 1; i < columnCount + 1; i++) {
            values.put(resultSet.getMetaData().getColumnLabel(i).toLowerCase(), resultSet.getString(i));
        }
        return objectMapper.mapObject(values);
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

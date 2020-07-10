/*
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
package org.jeasy.batch.jdbc;

import org.jeasy.batch.core.mapper.BeanIntrospectionException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import java.lang.reflect.Method;

/**
 * A prepared statement provider that prepares a statement by introspecting record fields.
 * The record's payload is expected to be a JavaBean as this prepared statement provider
 * uses JavaBean naming conventions to introspect fields.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class BeanPropertiesPreparedStatementProvider implements PreparedStatementProvider {

    protected String[] properties;
    protected PropertyDescriptor[] propertyDescriptors;
    protected final Map<Class<?>, Integer> javaTypesToSqlTypes = new HashMap<Class<?>, Integer>() {{
        put(boolean.class, Types.BOOLEAN);
        put(Boolean.class, Types.BOOLEAN);
        put(byte.class, Types.TINYINT);
        put(Byte.class, Types.TINYINT);
        put(short.class, Types.SMALLINT);
        put(Short.class, Types.SMALLINT);
        put(int.class, Types.INTEGER);
        put(Integer.class, Types.INTEGER);
        put(long.class, Types.BIGINT);
        put(Long.class, Types.BIGINT);
        put(BigInteger.class, Types.BIGINT);
        put(float.class, Types.FLOAT);
        put(Float.class, Types.FLOAT);
        put(double.class, Types.DOUBLE);
        put(Double.class, Types.DOUBLE);
        put(BigDecimal.class, Types.DECIMAL);
        put(java.util.Date.class, Types.DATE);
        put(java.util.Calendar.class, Types.DATE);
        put(java.sql.Date.class, Types.DATE);
        put(java.sql.Time.class, Types.TIME);
        put(java.sql.Timestamp.class, Types.TIMESTAMP);
        put(java.time.LocalDate.class, Types.DATE);
        put(java.time.LocalTime.class, Types.TIME);
        put(java.time.LocalDateTime.class, Types.TIMESTAMP);
        put(java.time.OffsetTime.class, Types.TIME_WITH_TIMEZONE);
        put(java.time.OffsetDateTime.class, Types.TIMESTAMP_WITH_TIMEZONE);
        put(java.sql.Blob.class, Types.BLOB);
        put(java.sql.Clob.class, Types.CLOB);
        put(CharSequence.class, Types.VARCHAR);
        put(String.class, Types.VARCHAR);
        put(StringBuffer.class, Types.VARCHAR);
        put(StringBuilder.class, Types.VARCHAR);
    }};

    /**
     * Create a new {@link BeanPropertiesPreparedStatementProvider}.
     *
     * @param type the record type
     * @param properties the properties to set in the statement in that order.
     * @throws BeanIntrospectionException when unable to introspect the record type
     */
    public BeanPropertiesPreparedStatementProvider(Class<?> type, String... properties) throws BeanIntrospectionException {
        this.properties = properties;
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(type);
            propertyDescriptors = beanInfo.getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new BeanIntrospectionException("Unable to introspect type " + type, e);
        }
    }

    @Override
    public void prepareStatement(PreparedStatement preparedStatement, Object record) throws SQLException {
        int index = 1;
        for (String property: properties) {
            try {
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    if (propertyDescriptor.getName().equals(property)) {
                        Method readMethod = propertyDescriptor.getReadMethod();
                        Integer sqlType = javaTypesToSqlTypes.get(readMethod.getReturnType());
                        Object value = readMethod.invoke(record);
                        if (sqlType != null) {
                            preparedStatement.setObject(index++, value, sqlType);
                        } else {
                            preparedStatement.setObject(index++, value);
                        }
                        break;
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new BeanIntrospectionException(format("Unable to get property %s from type %s", property, record.getClass().getName()), e);
            }
        }
    }
}

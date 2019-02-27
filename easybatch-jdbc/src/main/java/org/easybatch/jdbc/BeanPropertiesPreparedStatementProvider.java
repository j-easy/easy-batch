/**
 * The MIT License
 *
 *   Copyright (c) 2019, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.easybatch.jdbc;

import org.easybatch.core.mapper.BeanIntrospectionException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.lang.String.format;

/**
 * A prepared statement provider that prepares a statement by introspecting record fields.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class BeanPropertiesPreparedStatementProvider implements PreparedStatementProvider {

    private String[] properties;
    private PropertyDescriptor[] propertyDescriptors;

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
                        Object value = propertyDescriptor.getReadMethod().invoke(record);
                        preparedStatement.setObject(index++, value);
                        break;
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new BeanIntrospectionException(format("Unable to get property %s from type %s", property, record.getClass().getName()), e);
            }
        }
    }
}

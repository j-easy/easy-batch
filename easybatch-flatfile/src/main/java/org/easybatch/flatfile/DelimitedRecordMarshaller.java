/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

import org.easybatch.core.api.RecordMarshallingException;
import org.easybatch.core.processor.AbstractRecordMarshaller;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Marshals a POJO to CSV format.
 * <p/>
 * <strong>This marshaller does not support recursive marshalling.</strong>
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class DelimitedRecordMarshaller extends AbstractRecordMarshaller {

    public static final String DEFAULT_DELIMITER = ",";

    public static final String DEFAULT_QUALIFIER = "\"";

    private List<String> fields;

    private String delimiter;

    private String qualifier;

    Map<String, Method> getters;

    /**
     * Create a delimited record marshaller.
     *
     * @param type   the type of object to marshal
     * @param fields the list of fields to marshal in order
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    public DelimitedRecordMarshaller(final Class type, final String[] fields) throws IntrospectionException {
        this(type, fields, DEFAULT_DELIMITER, DEFAULT_QUALIFIER);
    }

    /**
     * Create a delimited record marshaller.
     *
     * @param type      the type of object to marshal
     * @param fields    the list of fields to marshal in order
     * @param delimiter the field delimiter
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    public DelimitedRecordMarshaller(final Class type, final String[] fields, final String delimiter) throws IntrospectionException {
        this(type, fields, delimiter, DEFAULT_QUALIFIER);
    }

    /**
     * Create a delimited record marshaller.
     *
     * @param type      the type of object to marshal
     * @param fields    the list of fields to marshal in order
     * @param delimiter the field delimiter
     * @param qualifier the field qualifier
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    public DelimitedRecordMarshaller(final Class type, final String[] fields, final String delimiter, final String qualifier) throws IntrospectionException {
        this.fields = Arrays.asList(fields);
        this.delimiter = delimiter;
        this.qualifier = qualifier;
        getters = new HashMap<String, Method>();
        introspectBean(type);
    }

    private void introspectBean(Class type) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            getters.put(propertyDescriptor.getName(), propertyDescriptor.getReadMethod());
        }
    }

    @Override
    protected String marshal(final Object record) throws RecordMarshallingException {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            Iterator<String> iterator = fields.iterator();
            while (iterator.hasNext()) {
                String field = iterator.next();
                stringBuilder.append(qualifier);
                stringBuilder.append(getValue(field, record));
                stringBuilder.append(qualifier);
                if (iterator.hasNext()) {
                    stringBuilder.append(delimiter);
                }
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            throw new RecordMarshallingException(e);
        }
    }

    private Object getValue(final String field, final Object object) throws InvocationTargetException, IllegalAccessException {
        return getters.get(field).invoke(object);
    }
}

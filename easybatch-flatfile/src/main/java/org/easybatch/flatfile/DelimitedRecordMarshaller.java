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

import org.easybatch.core.api.RecordFieldExtractor;
import org.easybatch.core.api.RecordMarshallingException;
import org.easybatch.core.field.BeanRecordFieldExtractor;
import org.easybatch.core.processor.AbstractRecordMarshaller;

import java.beans.IntrospectionException;
import java.util.Arrays;
import java.util.Iterator;

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

    private String delimiter;

    private String qualifier;

    private final RecordFieldExtractor fieldExtractor;

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
        this(new BeanRecordFieldExtractor(type, fields), delimiter, qualifier);
    }

    /**
     * Create a delimited record marshaller.
     *
     * @param fieldExtractor the field extractor
     * @param delimiter      the field delimiter
     * @param qualifier      the field qualifier
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    public DelimitedRecordMarshaller(RecordFieldExtractor fieldExtractor, final String delimiter, final String qualifier) throws IntrospectionException {
        this.fieldExtractor = fieldExtractor;
        this.delimiter = delimiter;
        this.qualifier = qualifier;
    }

    @Override
    protected String marshal(final Object record) throws RecordMarshallingException {
        Iterable<?> values = fieldExtractor.extractFields(record);
        try {
            StringBuilder stringBuilder = new StringBuilder();
            Iterator<?> iterator = values.iterator();
            while (iterator.hasNext()) {
                Object value = iterator.next();
                stringBuilder.append(qualifier);
                stringBuilder.append(value);
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
}

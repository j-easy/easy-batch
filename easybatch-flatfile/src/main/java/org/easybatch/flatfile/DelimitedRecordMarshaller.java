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

import org.easybatch.core.field.BeanFieldExtractor;
import org.easybatch.core.field.FieldExtractor;
import org.easybatch.core.marshaller.RecordMarshaller;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;

import java.beans.IntrospectionException;
import java.util.Iterator;

/**
 * Marshals a POJO to CSV format.
 *
 * <strong>This marshaller does not support recursive marshalling.</strong>
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class DelimitedRecordMarshaller<P> implements RecordMarshaller<Record<P>, StringRecord> {

    /**
     * Default delimiter.
     */
    public static final String DEFAULT_DELIMITER = ",";

    /**
     * Default qualifier.
     */
    public static final String DEFAULT_QUALIFIER = "\"";

    private String delimiter;
    private String qualifier;
    private FieldExtractor<P> fieldExtractor;

    /**
     * Create a new {@link DelimitedRecordMarshaller}.
     *
     * @param type   of object to marshal
     * @param fields to marshal in order
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    public DelimitedRecordMarshaller(final Class<P> type, final String... fields) throws IntrospectionException {
        this(type, fields, DEFAULT_DELIMITER, DEFAULT_QUALIFIER);
    }

    /**
     * Create a new {@link DelimitedRecordMarshaller}
     *
     * @param type      of object to marshal
     * @param fields    of fields to marshal in order
     * @param delimiter of fields
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    public DelimitedRecordMarshaller(final Class<P> type, final String[] fields, final String delimiter) throws IntrospectionException {
        this(type, fields, delimiter, DEFAULT_QUALIFIER);
    }

    /**
     * Create a new {@link DelimitedRecordMarshaller}
     *
     * @param type      of object to marshal
     * @param fields    to marshal in order
     * @param delimiter of fields
     * @param qualifier of fields
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    public DelimitedRecordMarshaller(final Class<P> type, final String[] fields, final String delimiter, final String qualifier) throws IntrospectionException {
        this(new BeanFieldExtractor<>(type, fields), delimiter, qualifier);
    }

    /**
     * Create a new {@link DelimitedRecordMarshaller}
     *
     * @param fieldExtractor extract fields
     * @param delimiter      of fields
     * @param qualifier      of fields
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    public DelimitedRecordMarshaller(FieldExtractor<P> fieldExtractor, final String delimiter, final String qualifier) throws IntrospectionException {
        this.fieldExtractor = fieldExtractor;
        this.delimiter = delimiter;
        this.qualifier = qualifier;
    }

    @Override
    public StringRecord processRecord(final Record<P> record) throws Exception {
        Iterable<Object> values = fieldExtractor.extractFields(record.getPayload());
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
        return new StringRecord(record.getHeader(), stringBuilder.toString());
    }
}

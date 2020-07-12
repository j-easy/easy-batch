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
package org.jeasy.batch.flatfile;

import org.jeasy.batch.core.field.BeanFieldExtractor;
import org.jeasy.batch.core.field.FieldExtractor;
import org.jeasy.batch.core.marshaller.RecordMarshaller;
import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.core.record.StringRecord;
import org.jeasy.batch.core.util.Utils;

import java.util.Iterator;

/**
 * Marshals a POJO to CSV format.
 *
 * <strong>This marshaller does not support recursive marshalling.</strong>
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class DelimitedRecordMarshaller<P> implements RecordMarshaller<P, String> {

    public static final String DEFAULT_DELIMITER = ",";
    public static final String DEFAULT_QUALIFIER = "\"";

    private String delimiter;
    private String qualifier;
    private FieldExtractor<P> fieldExtractor;

    /**
     * Create a new {@link DelimitedRecordMarshaller}.
     *
     * @param type   of object to marshal
     * @param fields to marshal in order
     */
    public DelimitedRecordMarshaller(final Class<P> type, final String... fields) {
        this(new BeanFieldExtractor<>(type, fields));
    }

    /**
     * Create a new {@link DelimitedRecordMarshaller}.
     *
     * @param fieldExtractor to use to extract fields
	 */
    public DelimitedRecordMarshaller(FieldExtractor<P> fieldExtractor) {
        Utils.checkNotNull(fieldExtractor, "field extractor");
        this.fieldExtractor = fieldExtractor;
        this.delimiter = DEFAULT_DELIMITER;
        this.qualifier = DEFAULT_QUALIFIER;
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

    /**
     * Set the delimiter to use.
     *
     * @param delimiter the delimiter to use
     */
    public void setDelimiter(final String delimiter) {
        this.delimiter = delimiter;
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

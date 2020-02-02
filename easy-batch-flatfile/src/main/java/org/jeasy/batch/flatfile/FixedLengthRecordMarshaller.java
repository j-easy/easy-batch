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
import org.jeasy.batch.core.record.Header;
import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.core.record.StringRecord;
import org.jeasy.batch.core.util.Utils;

import java.util.Locale;
import java.util.stream.StreamSupport;

/**
 * Marshals a POJO to fixed length format using {@link String#format(Locale, String, Object...)}.
 *
 * <strong>This marshaller can be used to left/right pad fields with white spaces to a fixed length.
 * However, it does NOT truncate data if a field value is longer than the specified
 * field length.</strong>
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class FixedLengthRecordMarshaller<P> implements RecordMarshaller<Record<P>, StringRecord> {

    private FieldExtractor<P> fieldExtractor;
    private String format;
    private Locale locale = Locale.getDefault();

    /**
     * Create a new {@link FixedLengthRecordMarshaller}. This constructor will
     * create a field extractor of type {@link BeanFieldExtractor}.
     *
     * @param type     of object to marshal
     * @param format   to use (in {@link java.util.Formatter} syntax)
     * @param fields   to marshal in the right order
     */
    public FixedLengthRecordMarshaller(final Class<P> type, String format, final String... fields) {
        this(new BeanFieldExtractor<>(type, fields), format);
    }

    /**
     * Create a new {@link FixedLengthRecordMarshaller}.
     *
     * @param fieldExtractor to use to extract fields
     * @param format         to use (in {@link java.util.Formatter} syntax)
     */
    public FixedLengthRecordMarshaller(FieldExtractor<P> fieldExtractor, String format) {
        Utils.checkNotNull(fieldExtractor, "field extractor");
        Utils.checkNotNull(format, "format");
        this.fieldExtractor = fieldExtractor;
        this.format = format;
    }

    @Override
    public StringRecord processRecord(final Record<P> record) throws Exception {
        Header header = record.getHeader();
        Object[] fields = toArray(fieldExtractor.extractFields(record.getPayload()));
        String payload = String.format(locale, format, fields);
        return new StringRecord(header, payload);
    }

    /**
     * Set the locale to use to format records.
     * @param locale to use to format records
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    // SDD: https://stackoverflow.com/a/51875471/5019386
    private Object[] toArray(Iterable<Object> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).toArray(Object[]::new);
    }
}

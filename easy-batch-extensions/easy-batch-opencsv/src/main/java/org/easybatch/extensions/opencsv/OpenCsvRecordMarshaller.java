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
package org.easybatch.extensions.opencsv;

import com.opencsv.CSVWriter;
import org.easybatch.core.field.BeanFieldExtractor;
import org.easybatch.core.field.FieldExtractor;
import org.easybatch.core.marshaller.RecordMarshaller;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;
import org.easybatch.core.util.Utils;

import java.beans.IntrospectionException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Marshals a POJO to CSV format using <a href="http://opencsv.sourceforge.net">Open CSV</a>.
 *
 * <strong>This marshaller does not support recursive marshalling.</strong>
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class OpenCsvRecordMarshaller<P> implements RecordMarshaller<Record<P>, StringRecord> {

    public static final char DEFAULT_DELIMITER = ',';
    public static final char DEFAULT_QUALIFIER = '\"';

    private char delimiter;
    private char qualifier;
    private FieldExtractor<P> fieldExtractor;

    /**
     * Create a new {@link OpenCsvRecordMarshaller}.
     *
     * @param type   the type of object to marshal
     * @param fields the list of fields to marshal in order
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    public OpenCsvRecordMarshaller(final Class<P> type, final String... fields) throws IntrospectionException {
        this.delimiter = DEFAULT_DELIMITER;
        this.qualifier = DEFAULT_QUALIFIER;
        this.fieldExtractor = new BeanFieldExtractor<>(type, fields);
    }

    /**
     * Create a new {@link OpenCsvRecordMarshaller}.
     *
     * @param fieldExtractor to use to extract fields
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    public OpenCsvRecordMarshaller(final FieldExtractor<P> fieldExtractor) throws IntrospectionException {
        Utils.checkNotNull(fieldExtractor, "field extractor");
        this.fieldExtractor = fieldExtractor;
        this.delimiter = DEFAULT_DELIMITER;
        this.qualifier = DEFAULT_QUALIFIER;
    }

    @Override
    public StringRecord processRecord(Record<P> record) throws Exception {
        StringWriter stringWriter = null;
        CSVWriter csvWriter = null;
        try {
            stringWriter = new StringWriter();
            csvWriter = new CSVWriter(stringWriter, delimiter, qualifier, ""); // force lineEnd to empty string
            P payload = record.getPayload();
            List<String> fields = extractFields(payload);
            String[] items = fields.toArray(new String[fields.size()]);
            csvWriter.writeNext(items);
            csvWriter.flush();
            return new StringRecord(record.getHeader(), stringWriter.toString());
        } finally {
            if (csvWriter != null) {
                csvWriter.close();
            }
            if (stringWriter != null) {
                stringWriter.close();
            }
        }
    }

    private List<String> extractFields(P payload) throws Exception {
        List<String> tokens = new ArrayList<>();
        Iterable<Object> objects = fieldExtractor.extractFields(payload);
        for (Object object : objects) {
            tokens.add(object.toString());
        }
        return tokens;
    }

    /**
     * Set the delimiter.
     *
     * @param delimiter to use
     */
    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Set the qualifier.
     *
     * @param qualifier to use
     */
    public void setQualifier(char qualifier) {
        this.qualifier = qualifier;
    }
}

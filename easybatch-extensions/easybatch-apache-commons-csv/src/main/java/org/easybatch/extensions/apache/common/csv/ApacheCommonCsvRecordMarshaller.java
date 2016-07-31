/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.extensions.apache.common.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.easybatch.core.field.BeanRecordFieldExtractor;
import org.easybatch.core.field.RecordFieldExtractor;
import org.easybatch.core.marshaller.RecordMarshaller;
import org.easybatch.core.marshaller.RecordMarshallingException;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.StringRecord;

import java.beans.IntrospectionException;
import java.io.StringWriter;

/**
 * Marshals a POJO to CSV format using <a href="http://commons.apache.org/proper/commons-csv/">Apache Common CSV</a>.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class ApacheCommonCsvRecordMarshaller implements RecordMarshaller<GenericRecord, StringRecord> {

    public static final char DEFAULT_DELIMITER = ',';

    public static final char DEFAULT_QUALIFIER = '\"';

    private final RecordFieldExtractor fieldExtractor;

    private CSVFormat csvFormat;

    /**
     * Create a record marshaller.
     *
     * @param type   the type of object to marshal
     * @param fields the list of fields to marshal in order
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    public ApacheCommonCsvRecordMarshaller(final Class type, final String... fields) throws IntrospectionException {
        this(type, fields, DEFAULT_DELIMITER, DEFAULT_QUALIFIER);
    }

    /**
     * Create a record marshaller.
     *
     * @param type      the type of object to marshal
     * @param fields    the list of fields to marshal in order
     * @param delimiter the field delimiter
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    public ApacheCommonCsvRecordMarshaller(final Class type, final String[] fields, final char delimiter) throws IntrospectionException {
        this(type, fields, delimiter, DEFAULT_QUALIFIER);
    }

    /**
     * Create a record marshaller.
     *
     * @param type      the type of object to marshal
     * @param fields    the list of fields to marshal in order
     * @param delimiter the field delimiter
     * @param qualifier the field qualifier
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    public ApacheCommonCsvRecordMarshaller(final Class type, final String[] fields, final char delimiter, final char qualifier) throws IntrospectionException {
        this(new BeanRecordFieldExtractor(type, fields), delimiter, qualifier);
    }

    /**
     * Create a record marshaller.
     *
     * @param fieldExtractor the field extractor
     * @param delimiter      the field delimiter
     * @param qualifier      the field qualifier
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    public ApacheCommonCsvRecordMarshaller(RecordFieldExtractor fieldExtractor, final char delimiter, final char qualifier) throws IntrospectionException {
        this.fieldExtractor = fieldExtractor;
        this.csvFormat = CSVFormat.newFormat(delimiter)
                .withQuote(qualifier)
                .withQuoteMode(QuoteMode.ALL)
                .withRecordSeparator(null);// recordSeparator is forced to null to avoid CSVPrinter to print new lines. New lines are written later by EasyBatch RecordWriter
    }

    @Override
    public StringRecord processRecord(final GenericRecord record) throws RecordMarshallingException {
        try {
            StringWriter stringWriter = new StringWriter();
            CSVPrinter csvPrinter = new CSVPrinter(stringWriter, csvFormat);
            Iterable<Object> iterable = fieldExtractor.extractFields(record.getPayload());
            csvPrinter.printRecord(iterable);
            csvPrinter.flush();
            return new StringRecord(record.getHeader(), stringWriter.toString());
        } catch (Exception e) {
            throw new RecordMarshallingException(e);
        }
    }

}

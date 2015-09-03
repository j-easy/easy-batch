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

package org.easybatch.integration.apache.common.csv;

import java.beans.IntrospectionException;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import org.easybatch.core.api.RecordFieldExtractor;
import org.easybatch.core.api.RecordMarshallingException;
import org.easybatch.core.field.BeanRecordFieldExtractor;
import org.easybatch.core.processor.AbstractRecordMarshaller;

/**
 * Marshals a POJO to CSV format using <a href="http://commons.apache.org/proper/commons-csv/">Apache Common CSV</a>.
 *
 * @author  Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class ApacheCommonCsvRecordMarshaller extends AbstractRecordMarshaller {

    private final RecordFieldExtractor fieldExtractor;
    private CSVFormat csvFormat;

    private CSVPrinter csvPrinter;

    /**
     * Create a record marshaller.
     *
     * @param   type       the type of object to marshal
     * @param   fields     the list of fields to marshal in order
     * @param   csvFormat  a pre-configured {@link CSVFormat} instance
     *
     * @throws  IntrospectionException  if the object to marshal cannot be introspected
     */
    public ApacheCommonCsvRecordMarshaller(final Class type, final String[] fields, final CSVFormat csvFormat)
        throws IntrospectionException, IOException {
        this(new BeanRecordFieldExtractor(type, fields), csvFormat);
    }

    /**
     * Create a record marshaller.
     *
     * @param   fieldExtractor  the field extractor
     * @param   csvFormat       a pre-configured {@link CSVFormat} instance
     *
     * @throws  IntrospectionException  if the object to marshal cannot be introspected
     */
    public ApacheCommonCsvRecordMarshaller(final RecordFieldExtractor fieldExtractor, final CSVFormat csvFormat)
        throws IntrospectionException, IOException {
        this.fieldExtractor = fieldExtractor;
        this.csvFormat = csvFormat;
        csvPrinter = new CSVPrinter(new StringWriter(), csvFormat.withRecordSeparator(null));
    }

    @Override
    protected String marshal(final Object record) throws RecordMarshallingException {
        try {

            // recordSeparator is forced to null to avoid CSVPrinter to print new lines.
            // New lines are written later by EasyBatch RecordWriter
            Iterable<?> iterable = fieldExtractor.extractFields(record);
            csvPrinter.printRecord(iterable);
            csvPrinter.flush();
            return csvPrinter.getOut().toString();
        } catch (Exception e) {
            throw new RecordMarshallingException(e);
        }
    }

}

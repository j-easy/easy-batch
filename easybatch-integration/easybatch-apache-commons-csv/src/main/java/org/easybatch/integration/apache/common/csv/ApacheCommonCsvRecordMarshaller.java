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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.easybatch.core.api.RecordMarshallingException;
import org.easybatch.core.processor.AbstractRecordMarshaller;
import org.easybatch.core.util.Utils;

import java.beans.IntrospectionException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Marshals a record to CSV format.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class ApacheCommonCsvRecordMarshaller extends AbstractRecordMarshaller {

    private List<String> fields;

    private Map<String, Method> getters;

    private CSVFormat csvFormat;

    /**
     * Create a record marshaller.
     *
     * @param type the type of object to marshal
     * @param fields the list of fields to marshal in order
     * @param csvFormat a pre-configured {@link CSVFormat} instance
     * @throws IntrospectionException if the object to marshal cannot be introspected
     */
    public ApacheCommonCsvRecordMarshaller(final Class type, final String[] fields, CSVFormat csvFormat) throws IntrospectionException {
        this.fields = Arrays.asList(fields);
        this.csvFormat = csvFormat;
        getters = Utils.getGetters(type);
    }

    @Override
    protected String marshal(Object record) throws RecordMarshallingException {
        try {
            StringWriter stringWriter = new StringWriter();
            CSVPrinter csvPrinter = new CSVPrinter(stringWriter, csvFormat);
            List<Object> values = new ArrayList<Object>();
            for (String field : fields) {
                values.add(getValue(field, record));
            }
            csvPrinter.printRecord(values);
            csvPrinter.flush();
            // by default, the csvPrinter adds a line separator, this should be removed since Easy Batch writers will add it
            return removeRecordSeparator(stringWriter);
        } catch (Exception e) {
            throw new RecordMarshallingException(e);
        }
    }

    private String removeRecordSeparator(StringWriter stringWriter) {
        String result = stringWriter.toString();
        String recordSeparator = csvFormat.getRecordSeparator();
        return result.substring(0, result.indexOf(recordSeparator));
    }

    protected Object getValue(final String field, final Object object) throws InvocationTargetException, IllegalAccessException {
        return getters.get(field).invoke(object);
    }
}

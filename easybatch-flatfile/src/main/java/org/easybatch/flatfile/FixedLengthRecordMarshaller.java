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

package org.easybatch.flatfile;

import org.easybatch.core.field.RecordFieldExtractor;
import org.easybatch.core.marshaller.RecordMarshaller;
import org.easybatch.core.marshaller.RecordMarshallingException;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.StringRecord;

import java.beans.IntrospectionException;

/**
 * Marshals a POJO to fixed length format.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class FixedLengthRecordMarshaller implements RecordMarshaller<GenericRecord, StringRecord> {

    private DelimitedRecordMarshaller delimitedRecordMarshaller;

    /**
     * Create a fixed length record marshaller.
     *
     * @param type   the type of object to marshal
     * @param fields the list of fields to marshal in order
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    public FixedLengthRecordMarshaller(final Class type, final String... fields) throws IntrospectionException {
        delimitedRecordMarshaller = new DelimitedRecordMarshaller(type, fields, "", "");
    }

    /**
     * Create a fixed length record marshaller.
     *
     * @param fieldExtractor the field extractor
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    public FixedLengthRecordMarshaller(RecordFieldExtractor fieldExtractor) throws IntrospectionException {
        delimitedRecordMarshaller = new DelimitedRecordMarshaller(fieldExtractor, "", "");
    }

    @Override
    public StringRecord processRecord(final GenericRecord record) throws RecordMarshallingException {
        return new StringRecord(record.getHeader(), delimitedRecordMarshaller.processRecord(record).getPayload());
    }

}

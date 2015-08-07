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
import org.easybatch.core.processor.AbstractRecordMarshaller;

import java.beans.IntrospectionException;

/**
 * Marshals a POJO to fixed length format.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class FixedLengthRecordMarshaller extends AbstractRecordMarshaller {

    private DelimitedRecordMarshaller delimitedRecordMarshaller;

    /**
     * Create a fixed length record marshaller.
     *
     * @param type   the type of object to marshal
     * @param fields the list of fields to marshal in order
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    public FixedLengthRecordMarshaller(final Class type, final String[] fields) throws IntrospectionException {
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
    protected String marshal(final Object record) throws RecordMarshallingException {
        return delimitedRecordMarshaller.marshal(record);
    }

}

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

package org.easybatch.integration.jackson;

import org.codehaus.jackson.map.ObjectMapper;
import org.easybatch.core.api.RecordMarshallingException;
import org.easybatch.core.processor.AbstractRecordMarshaller;

import java.io.IOException;

/**
 * Marshals a POJO to Json using Jackson.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JacksonRecordMarshaller extends AbstractRecordMarshaller {

    private ObjectMapper mapper;

    public JacksonRecordMarshaller() {
        mapper = new ObjectMapper();
    }

    /**
     * Create a record marshaller.
     *
     * @param mapper a pre-configured {@link ObjectMapper} instance
     */
    public JacksonRecordMarshaller(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    protected String marshal(final Object record) throws RecordMarshallingException {
        try {
            return mapper.writeValueAsString(record);
        } catch (IOException e) {
            throw new RecordMarshallingException(e);
        }
    }

}

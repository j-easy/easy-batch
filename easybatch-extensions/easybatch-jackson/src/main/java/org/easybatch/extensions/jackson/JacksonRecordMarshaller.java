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

package org.easybatch.extensions.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.easybatch.core.marshaller.RecordMarshaller;
import org.easybatch.core.marshaller.RecordMarshallingException;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.json.JsonRecord;

import java.io.IOException;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Marshals a POJO to Json using <a href="http://jackson.codehaus.org/">Jackson</a>.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JacksonRecordMarshaller implements RecordMarshaller<GenericRecord, JsonRecord> {

    private ObjectMapper mapper;

    public JacksonRecordMarshaller() {
        mapper = new ObjectMapper();
    }

    /**
     * Create a Json record marshaller.
     *
     * @param mapper a pre-configured {@link ObjectMapper} instance
     */
    public JacksonRecordMarshaller(final ObjectMapper mapper) {
        checkNotNull(mapper, "object mapper");
        this.mapper = mapper;
    }

    @Override
    public JsonRecord processRecord(final GenericRecord record) throws RecordMarshallingException {
        try {
            return new JsonRecord(record.getHeader(), mapper.writeValueAsString(record.getPayload()));
        } catch (IOException e) {
            throw new RecordMarshallingException(e);
        }
    }

}

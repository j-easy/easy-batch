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
package org.easybatch.extensions.xstream;

import com.thoughtworks.xstream.XStream;
import org.easybatch.core.mapper.RecordMapper;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Record;
import org.easybatch.xml.XmlRecord;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Mapper that uses <a href="http://xstream.codehaus.org/">XStream</a>
 * to map XML records to domain objects.
 *
 * @param <P> Target domain object class.
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class XstreamRecordMapper<P> implements RecordMapper<XmlRecord, Record<P>> {

    private XStream xStream;

    /**
     * Create a new {@link XstreamRecordMapper}.
     *
     * @param xStream the {@link XStream} mapper.
     */
    public XstreamRecordMapper(final XStream xStream) {
        checkNotNull(xStream, "xStream");
        this.xStream = xStream;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Record<P> processRecord(final XmlRecord record) throws Exception {
        P unmarshalledObject = (P) xStream.fromXML(record.getPayload());
        return new GenericRecord<>(record.getHeader(), unmarshalledObject);
    }

}

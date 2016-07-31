/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.extensions.xstream;

import com.thoughtworks.xstream.XStream;
import org.easybatch.core.mapper.RecordMapper;
import org.easybatch.core.mapper.RecordMappingException;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.xml.XmlRecord;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Mapper that uses <a href="http://xstream.codehaus.org/">XStream</a>
 * to map XML records to domain objects.
 *
 * @param <P> Target domain object class.
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class XstreamRecordMapper<P> implements RecordMapper<XmlRecord, GenericRecord<P>> {

    private XStream xStream;

    /**
     * Mapper that uses <a href="http://xstream.codehaus.org/">XStream</a>
     * to map XML records to domain objects.
     *
     * @param xStream the {@link XStream} mapper.
     */
    public XstreamRecordMapper(final XStream xStream) {
        checkNotNull(xStream, "xStream");
        this.xStream = xStream;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public GenericRecord<P> processRecord(final XmlRecord record) throws RecordMappingException {
        try {
            P unmarshalledObject = (P) xStream.fromXML(record.getPayload());
            return new GenericRecord<>(record.getHeader(), unmarshalledObject);
        } catch (Exception e) {
            throw new RecordMappingException("Unable to map record " + record + " to target type", e);
        }
    }

}

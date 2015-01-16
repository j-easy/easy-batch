/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.xml.xstream;

import com.thoughtworks.xstream.XStream;
import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordMapper;
import org.easybatch.xml.XmlRecord;

/**
 * Mapper that uses <a href="http://xstream.codehaus.org/">XStream</a>
 * to map XML records to domain objects.
 *
 * @param <T> Target domain object class.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class XstreamRecordMapper<T> implements RecordMapper<T> {

    private XStream xStream;

    public XstreamRecordMapper(XStream xStream) {
        this.xStream = xStream;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public T mapRecord(Record record) throws Exception {
        XmlRecord xmlRecord = (XmlRecord) record;
        return (T) xStream.fromXML(xmlRecord.getPayload());
    }

}

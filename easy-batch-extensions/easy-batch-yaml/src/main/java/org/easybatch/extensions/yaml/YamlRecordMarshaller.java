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
package org.easybatch.extensions.yaml;

import com.esotericsoftware.yamlbeans.YamlWriter;
import org.easybatch.core.marshaller.RecordMarshaller;
import org.easybatch.core.record.Record;

import java.io.StringWriter;

/**
 * {@link RecordMarshaller} that marshals java objects to Yaml format using <a href="https://github.com/EsotericSoftware/yamlbeans">yamlbeans</a>.
 *
 * @param <P> type of objects to marshal.
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class YamlRecordMarshaller<P> implements RecordMarshaller<Record<P>, YamlRecord> {

    @Override
    public YamlRecord processRecord(Record<P> record) throws Exception {
        StringWriter stringWriter = new StringWriter();
        YamlWriter yamlWriter = new YamlWriter(stringWriter);
        yamlWriter.write(record.getPayload());
        yamlWriter.close();
        return new YamlRecord(record.getHeader(), stringWriter.toString());
    }
}

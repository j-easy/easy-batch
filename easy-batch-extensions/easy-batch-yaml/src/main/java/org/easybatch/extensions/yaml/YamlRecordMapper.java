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

import com.esotericsoftware.yamlbeans.YamlReader;
import org.easybatch.core.mapper.RecordMapper;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Record;

/**
 * {@link RecordMapper} that maps records to java objects using <a href="https://github.com/EsotericSoftware/yamlbeans">yamlbeans</a>.
 *
 * @param <P> type of target objects
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class YamlRecordMapper<P> implements RecordMapper<YamlRecord, Record<P>> {

    private Class<P> type;

    /**
     * Create a new {@link YamlRecordMapper}.
     *
     * @param type of the target object
     */
    public YamlRecordMapper(Class<P> type) {
        this.type = type;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Record<P> processRecord(YamlRecord record) throws Exception {
        YamlReader reader = new YamlReader(record.getPayload());
        P unmarshalledObject = reader.read(type);
        reader.close();
        return new GenericRecord<>(record.getHeader(), unmarshalledObject);
    }
}

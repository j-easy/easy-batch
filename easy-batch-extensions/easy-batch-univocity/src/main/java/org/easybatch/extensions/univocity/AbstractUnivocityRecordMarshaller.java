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
package org.easybatch.extensions.univocity;

import com.univocity.parsers.common.AbstractWriter;
import com.univocity.parsers.common.CommonWriterSettings;
import org.easybatch.core.field.BeanFieldExtractor;
import org.easybatch.core.field.FieldExtractor;
import org.easybatch.core.marshaller.RecordMarshaller;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;

import java.beans.IntrospectionException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Marshals a POJO to a format supported by  <a href="http://www.univocity.com/">uniVocity</a>.
 *
 * <strong>This marshaller does not support recursive marshalling.</strong>
 *
 * @param <S> The settings type that is used to configure the writer.
 * @author Anthony Bruno (anthony.bruno196@gmail.com)
 */
abstract class AbstractUnivocityRecordMarshaller<P, S extends CommonWriterSettings<?>> implements RecordMarshaller<Record<P>, StringRecord> {

    private final FieldExtractor<P> fieldExtractor;
    final S settings;
    StringWriter stringWriter;

    /**
     * Create a new univocty record marshaller to marshal a POJO to a format supported by
     * <a href="http://www.univocity.com/">uniVocity</a>.
     *
     * @param recordClass the type of object to marshal
     * @param settings    settings used to configure the writer object
     * @param fields      the list of fields to marshal in order
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    AbstractUnivocityRecordMarshaller(Class<P> recordClass, S settings, String... fields) throws IntrospectionException {
        this.fieldExtractor = new BeanFieldExtractor<>(recordClass, fields);
        this.settings = settings;
    }

    @Override
    public StringRecord processRecord(Record<P> record) throws Exception {
        stringWriter = new StringWriter();
        AbstractWriter<S> writer = getWriter();
        String[] rowToWrite = extractFields(record.getPayload());

        writer.writeRow(rowToWrite);
        writer.close();

        return new StringRecord(record.getHeader(), stringWriter.toString());
    }


    private String[] extractFields(P payload) throws Exception {
        List<String> resultList = new ArrayList<>();
        Iterable<Object> iterable = fieldExtractor.extractFields(payload);
        for (Object object : iterable) {
            resultList.add(object.toString());
        }
        return resultList.toArray(new String[0]);
    }

    abstract AbstractWriter<S> getWriter();
}

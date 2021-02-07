/*
 * The MIT License
 *
 *   Copyright (c) 2021, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.jeasy.batch.extensions.univocity;

import com.univocity.parsers.common.AbstractParser;
import com.univocity.parsers.common.CommonParserSettings;
import com.univocity.parsers.common.processor.BeanListProcessor;
import org.jeasy.batch.core.mapper.RecordMapper;
import org.jeasy.batch.core.record.GenericRecord;
import org.jeasy.batch.core.record.Record;

import java.io.StringReader;

/**
 * A record mapper that uses <a href="http://www.univocity.com/">uniVocity parsers</a> to map delimited records to
 * domain objects.
 *
 * @param <S> The settings type that is used to configure the parser.
 * @author Anthony Bruno (anthony.bruno196@gmail.com)
 */
abstract class AbstractUnivocityRecordMapper<T, S extends CommonParserSettings<?>> implements RecordMapper<String, T> {

    S settings;
    private AbstractParser<S> parser;
    private BeanListProcessor<T> beanListProcessor;

    /**
     * Creates a new mapper that uses <a href="http://www.univocity.com/">uniVocity parsers</a> to map delimited records
     * to domain objects.
     *
     * @param recordClass the target type
     * @param settings    the settings that is is used to configure the parser
     */
    AbstractUnivocityRecordMapper(Class<T> recordClass, S settings) {
        this.settings = settings;
        this.beanListProcessor = new BeanListProcessor<>(recordClass);
        this.settings.setProcessor(this.beanListProcessor);
        this.parser = getParser();
    }


    @Override
    public Record<T> processRecord(Record<String> record) {
        String payload = record.getPayload();
        parser.parse(new StringReader(payload));

        T result = beanListProcessor.getBeans().get(0);
        return new GenericRecord<>(record.getHeader(), result);
    }

    protected abstract AbstractParser<S> getParser();

}

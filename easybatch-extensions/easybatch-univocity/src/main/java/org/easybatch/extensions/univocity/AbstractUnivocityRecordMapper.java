package org.easybatch.extensions.univocity;

import com.univocity.parsers.common.AbstractParser;
import com.univocity.parsers.common.CommonParserSettings;
import com.univocity.parsers.common.processor.BeanListProcessor;
import org.easybatch.core.mapper.RecordMapper;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;

import java.io.StringReader;

/**
 * A record mapper that uses <a href="http://www.univocity.com/">uniVocity parsers</a> to map delimited records to
 * domain objects.
 *
 * @param <S> The settings type that is used to configure the parser.
 * @author Anthony Bruno (anthony.bruno196@gmail.com)
 */
abstract class AbstractUnivocityRecordMapper<T, S extends CommonParserSettings<?>> implements RecordMapper<StringRecord, Record<T>> {

    S settings;
    private Class<T> recordClass;

    /**
     * Creates a new mapper that uses <a href="http://www.univocity.com/">uniVocity parsers</a> to map delimited records
     * to domain objects.
     *
     * @param recordClass the target type
     * @param settings    the settings that is is used to configure the parser
     */
    AbstractUnivocityRecordMapper(Class<T> recordClass, S settings) {
        this.recordClass = recordClass;
        this.settings = settings;
    }


    @Override
    public Record<T> processRecord(StringRecord record) throws Exception {
        BeanListProcessor<T> beanListProcessor = new BeanListProcessor<>(recordClass);
        settings.setProcessor(beanListProcessor);

        AbstractParser<S> parser = getParser();
        String payload = record.getPayload();
        parser.parse(new StringReader(payload));

        T result = beanListProcessor.getBeans().get(0);
        return new GenericRecord<>(record.getHeader(), result);
    }

    protected abstract AbstractParser<S> getParser();

}

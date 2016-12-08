package org.easybatch.extensions.univocity;

import com.univocity.parsers.common.AbstractParser;
import com.univocity.parsers.common.CommonParserSettings;
import com.univocity.parsers.common.Format;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.fixed.FixedWidthParser;
import com.univocity.parsers.fixed.FixedWidthParserSettings;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import org.easybatch.core.mapper.RecordMapper;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;

import java.io.StringReader;

/**
 * A record mapper that uses <a href="http://www.univocity.com/">uniVocity parsers</a> to map a delimited record to domain object.
 *
 * @author Anthony Bruno (anthony.bruno196@gmail.com)
 */
public abstract class AbstractUnivocityRecordMapper<T, S extends CommonParserSettings<?>> implements RecordMapper<StringRecord, Record<T>> {

    protected S settings;
    private Class<T> recordClass;

    public AbstractUnivocityRecordMapper(Class<T> recordClass, S settings)  {
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

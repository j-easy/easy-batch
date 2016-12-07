package org.easybatch.extensions.univocity;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
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
public class UnivocityRecordMapper<T> implements RecordMapper<StringRecord, Record<T>> {

    private CsvParserSettings settings;
    private Class<T> recordClass;

    public UnivocityRecordMapper(Class<T> recordClass, CsvParserSettings settings)  {
        this.recordClass = recordClass;
        this.settings = settings;
    }


    @Override
    public Record<T> processRecord(StringRecord record) throws Exception {
        BeanListProcessor<T> beanListProcessor = new BeanListProcessor<>(recordClass);
        settings.setProcessor(beanListProcessor);

        CsvParser parser = new CsvParser(settings);
        String payload = record.getPayload();
        parser.parse(new StringReader(payload));

        T result = beanListProcessor.getBeans().get(0);
        return new GenericRecord<>(record.getHeader(), result);
    }

}

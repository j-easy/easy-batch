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
 * Created by anthony on 4/12/16.
 */
public class UnivocityRecordMapper<T> implements RecordMapper<StringRecord, Record<T>> {

    private char delimiter;
    private char qualifier;
    private boolean strictQualifiers;
    private boolean headerExtraction;
    private Class<T> recordClass;

    public UnivocityRecordMapper(Class<T> recordClass)  {
        this.recordClass = recordClass;
    }

    @Override
    public Record<T> processRecord(StringRecord record) throws Exception {
        String payload = record.getPayload();
        CsvParserSettings settings = new CsvParserSettings();
        BeanListProcessor<T> beanListProcessor = new BeanListProcessor<>(recordClass);
        settings.setProcessor(beanListProcessor);
        CsvParser parser = new CsvParser(settings);

        parser.parse(new StringReader(payload));

        T result =  beanListProcessor.getBeans().get(0);
        return new GenericRecord<>(record.getHeader(), result);
    }

    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    public void setQualifier(char qualifier) {
        this.qualifier = qualifier;
    }

    public void setStrictQualifiers(boolean strictQualifiers) {
        this.strictQualifiers = strictQualifiers;
    }

    public void setHeaderExtractionEnabled(boolean headerExtraction) {
        this.headerExtraction = headerExtraction;
    }
}

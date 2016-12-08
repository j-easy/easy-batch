package org.easybatch.extensions.univocity;

import com.univocity.parsers.common.AbstractParser;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

/**
 * Created by anthony on 8/12/16.
 */
public class UnivocityCsvRecordMapper<T> extends AbstractUnivocityRecordMapper<T, CsvParserSettings> {

    public UnivocityCsvRecordMapper(Class<T> recordClass, CsvParserSettings settings) {
        super(recordClass, settings);
    }

    @Override
    protected AbstractParser<CsvParserSettings> getParser() {
        return new CsvParser(settings);
    }

}

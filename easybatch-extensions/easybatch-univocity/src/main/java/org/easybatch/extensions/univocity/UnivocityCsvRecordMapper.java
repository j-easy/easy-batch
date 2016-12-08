package org.easybatch.extensions.univocity;

import com.univocity.parsers.common.AbstractParser;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

/**
 * A record mapper that uses <a href="http://www.univocity.com/">uniVocity parsers</a> to map CSV records
 * to domain objects.
 *
 * @author Anthony Bruno (anthony.bruno196@gmail.com)
 */
public class UnivocityCsvRecordMapper<T> extends AbstractUnivocityRecordMapper<T, CsvParserSettings> {

    /**
     * Creates a new mapper that uses <a href="http://www.univocity.com/">uniVocity parsers</a> to map CSV records
     * to domain objects.
     *
     * @param recordClass the target type
     * @param settings    the settings that is is used to configure the parser
     */
    public UnivocityCsvRecordMapper(Class<T> recordClass, CsvParserSettings settings) {
        super(recordClass, settings);
    }

    @Override
    protected AbstractParser<CsvParserSettings> getParser() {
        return new CsvParser(settings);
    }

}

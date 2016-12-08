package org.easybatch.extensions.univocity;

import com.univocity.parsers.common.AbstractParser;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

/**
 * A record mapper that uses <a href="http://www.univocity.com/">uniVocity parsers</a> to map TSV records
 * to domain objects.
 *
 * @author Anthony Bruno (anthony.bruno196@gmail.com)
 */
public class UnivocityTsvRecordMapper<T> extends AbstractUnivocityRecordMapper<T, TsvParserSettings> {

    /**
     * Creates a new mapper that uses <a href="http://www.univocity.com/">uniVocity parsers</a> to map TSV records
     * to domain objects.
     *
     * @param recordClass the target type
     * @param settings    the settings that is is used to configure the parser
     */
    public UnivocityTsvRecordMapper(Class<T> recordClass, TsvParserSettings settings) {
        super(recordClass, settings);
    }

    @Override
    protected AbstractParser<TsvParserSettings> getParser() {
        return new TsvParser(settings);
    }
}

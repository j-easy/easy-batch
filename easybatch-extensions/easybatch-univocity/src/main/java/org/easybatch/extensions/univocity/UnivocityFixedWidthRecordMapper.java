package org.easybatch.extensions.univocity;

import com.univocity.parsers.common.AbstractParser;
import com.univocity.parsers.fixed.FixedWidthParser;
import com.univocity.parsers.fixed.FixedWidthParserSettings;

/**
 * A record mapper that uses <a href="http://www.univocity.com/">uniVocity parsers</a> to map fixed width records
 * to domain objects.
 *
 * @author Anthony Bruno (anthony.bruno196@gmail.com)
 */
public class UnivocityFixedWidthRecordMapper<T> extends AbstractUnivocityRecordMapper<T, FixedWidthParserSettings> {

    /**
     * Creates a new mapper that uses <a href="http://www.univocity.com/">uniVocity parsers</a> to map fixed width records
     * to domain objects.
     *
     * @param recordClass the target type
     * @param settings    the settings that is is used to configure the parser
     */
    public UnivocityFixedWidthRecordMapper(Class<T> recordClass, FixedWidthParserSettings settings) {
        super(recordClass, settings);
    }

    @Override
    protected AbstractParser<FixedWidthParserSettings> getParser() {
        return new FixedWidthParser(settings);
    }
}

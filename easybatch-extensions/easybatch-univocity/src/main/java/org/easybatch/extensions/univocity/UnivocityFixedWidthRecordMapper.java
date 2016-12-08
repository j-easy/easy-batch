package org.easybatch.extensions.univocity;

import com.univocity.parsers.common.AbstractParser;
import com.univocity.parsers.fixed.FixedWidthParser;
import com.univocity.parsers.fixed.FixedWidthParserSettings;

/**
 * Created by anthony on 8/12/16.
 */
public class UnivocityFixedWidthRecordMapper<T> extends AbstractUnivocityRecordMapper<T, FixedWidthParserSettings> {

    public UnivocityFixedWidthRecordMapper(Class<T> recordClass, FixedWidthParserSettings settings) {
        super(recordClass, settings);
    }

    @Override
    protected AbstractParser<FixedWidthParserSettings> getParser() {
        return new FixedWidthParser(settings);
    }
}

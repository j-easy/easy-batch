package org.easybatch.extensions.univocity;

import com.univocity.parsers.common.AbstractParser;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

/**
 * Created by anthony on 8/12/16.
 */
public class UnivocityTsvRecordMapper<T> extends AbstractUnivocityRecordMapper<T, TsvParserSettings> {

    public UnivocityTsvRecordMapper(Class<T> recordClass, TsvParserSettings settings) {
        super(recordClass, settings);
    }

    @Override
    protected AbstractParser<TsvParserSettings> getParser() {
        return new TsvParser(settings);
    }
}

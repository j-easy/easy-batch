package org.easybatch.extensions.univocity;

import com.univocity.parsers.common.AbstractParser;
import com.univocity.parsers.common.AbstractWriter;
import com.univocity.parsers.tsv.TsvWriter;
import com.univocity.parsers.tsv.TsvWriterSettings;

import java.beans.IntrospectionException;

/**
 * Created by anthony on 8/12/16.
 */
public class UnivocityTsvRecordMarshaller<P> extends AbstractUnivocityRecordMarshaller<P, TsvWriterSettings> {

    public UnivocityTsvRecordMarshaller(Class<P> recordClass, TsvWriterSettings settings, String... fields) throws IntrospectionException {
        super(recordClass, settings, fields);
    }

    @Override
    AbstractWriter<TsvWriterSettings> getWriter() {
        return new TsvWriter(stringWriter, settings);
    }
}

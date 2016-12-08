package org.easybatch.extensions.univocity;

import com.univocity.parsers.common.AbstractWriter;
import com.univocity.parsers.fixed.FixedWidthWriter;
import com.univocity.parsers.fixed.FixedWidthWriterSettings;

import java.beans.IntrospectionException;

/**
 * Created by anthony on 8/12/16.
 */
public class UnivocityFixedWidthRecordMarshaller<P> extends AbstractUnivocityRecordMarshaller<P, FixedWidthWriterSettings> {
    public UnivocityFixedWidthRecordMarshaller(Class<P> recordClass, FixedWidthWriterSettings settings, String... fields) throws IntrospectionException {
        super(recordClass, settings, fields);
    }

    @Override
    AbstractWriter<FixedWidthWriterSettings> getWriter() {
        return new FixedWidthWriter(stringWriter, settings);
    }
}

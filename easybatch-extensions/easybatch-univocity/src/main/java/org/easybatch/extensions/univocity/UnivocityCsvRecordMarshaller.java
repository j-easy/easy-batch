package org.easybatch.extensions.univocity;

import com.univocity.parsers.common.AbstractWriter;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import java.beans.IntrospectionException;

/**
 * Created by anthony on 8/12/16.
 */
public class UnivocityCsvRecordMarshaller<P> extends AbstractUnivocityRecordMarshaller<P, CsvWriterSettings> {

    public UnivocityCsvRecordMarshaller(Class<P> recordClass, CsvWriterSettings settings, String... fields) throws IntrospectionException {
        super(recordClass, settings, fields);
    }

    @Override
    AbstractWriter<CsvWriterSettings> getWriter() {
        return new CsvWriter(stringWriter, settings);
    }
}

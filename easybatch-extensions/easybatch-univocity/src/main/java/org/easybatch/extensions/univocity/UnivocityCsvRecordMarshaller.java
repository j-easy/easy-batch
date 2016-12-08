package org.easybatch.extensions.univocity;

import com.univocity.parsers.common.AbstractWriter;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import java.beans.IntrospectionException;

/**
 * Marshals a POJO to CSV format using  <a href="http://www.univocity.com/">uniVocity</a>.
 * <p/>
 * <strong>This marshaller does not support recursive marshalling.</strong>
 *
 * @author Anthony Bruno (anthony.bruno196@gmail.com)
 */
public class UnivocityCsvRecordMarshaller<P> extends AbstractUnivocityRecordMarshaller<P, CsvWriterSettings> {

    /**
     * Create a new univocty record marshaller to marshal a POJO to CSV format using
     * <a href="http://www.univocity.com/">uniVocity</a>.
     *
     * @param recordClass the type of object to marshal
     * @param settings    settings used to configure the writer object
     * @param fields      the list of fields to marshal in order
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    public UnivocityCsvRecordMarshaller(Class<P> recordClass, CsvWriterSettings settings, String... fields) throws IntrospectionException {
        super(recordClass, settings, fields);
    }

    @Override
    AbstractWriter<CsvWriterSettings> getWriter() {
        return new CsvWriter(stringWriter, settings);
    }
}

package org.easybatch.extensions.univocity;

import com.univocity.parsers.common.AbstractWriter;
import com.univocity.parsers.tsv.TsvWriter;
import com.univocity.parsers.tsv.TsvWriterSettings;

import java.beans.IntrospectionException;

/**
 * Marshals a POJO to TSV format using  <a href="http://www.univocity.com/">uniVocity</a>.
 * <p/>
 * <strong>This marshaller does not support recursive marshalling.</strong>
 *
 * @author Anthony Bruno (anthony.bruno196@gmail.com)
 */
public class UnivocityTsvRecordMarshaller<P> extends AbstractUnivocityRecordMarshaller<P, TsvWriterSettings> {

    /**
     * Create a new uniVocity record marshaller to marshal a POJO to TSV format using
     * <a href="http://www.univocity.com/">uniVocity</a>.
     *
     * @param recordClass the type of object to marshal
     * @param settings    settings used to configure the writer object
     * @param fields      the list of fields to marshal in order
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    public UnivocityTsvRecordMarshaller(Class<P> recordClass, TsvWriterSettings settings, String... fields) throws IntrospectionException {
        super(recordClass, settings, fields);
    }

    @Override
    AbstractWriter<TsvWriterSettings> getWriter() {
        return new TsvWriter(stringWriter, settings);
    }
}

package org.easybatch.extensions.univocity;

import com.univocity.parsers.common.AbstractWriter;
import com.univocity.parsers.fixed.FixedWidthWriter;
import com.univocity.parsers.fixed.FixedWidthWriterSettings;

import java.beans.IntrospectionException;

/**
 * Marshals a POJO to fixed width format using  <a href="http://www.univocity.com/">uniVocity</a>.
 * <p/>
 * <strong>This marshaller does not support recursive marshalling.</strong>
 *
 * @author Anthony Bruno (anthony.bruno196@gmail.com)
 */
public class UnivocityFixedWidthRecordMarshaller<P> extends AbstractUnivocityRecordMarshaller<P, FixedWidthWriterSettings> {

    /**
     * Create a new uniVocity record marshaller to marshal a POJO to fixed width format using
     * <a href="http://www.univocity.com/">uniVocity</a>.
     *
     * @param recordClass the type of object to marshal
     * @param settings    settings used to configure the writer object
     * @param fields      the list of fields to marshal in order
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    public UnivocityFixedWidthRecordMarshaller(Class<P> recordClass, FixedWidthWriterSettings settings, String... fields) throws IntrospectionException {
        super(recordClass, settings, fields);
    }

    @Override
    AbstractWriter<FixedWidthWriterSettings> getWriter() {
        return new FixedWidthWriter(stringWriter, settings);
    }
}

package org.easybatch.extensions.univocity;

import com.univocity.parsers.common.AbstractWriter;
import com.univocity.parsers.common.CommonWriterSettings;
import org.easybatch.core.field.BeanFieldExtractor;
import org.easybatch.core.field.FieldExtractor;
import org.easybatch.core.marshaller.RecordMarshaller;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;

import java.beans.IntrospectionException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Marshals a POJO to a format supported by  <a href="http://www.univocity.com/">uniVocity</a>.
 * <p/>
 * <strong>This marshaller does not support recursive marshalling.</strong>
 *
 * @param <S> The settings type that is used to configure the writer.
 * @author Anthony Bruno (anthony.bruno196@gmail.com)
 */
abstract class AbstractUnivocityRecordMarshaller<P, S extends CommonWriterSettings<?>> implements RecordMarshaller<Record<P>, StringRecord> {

    private final FieldExtractor<P> fieldExtractor;
    final S settings;
    StringWriter stringWriter;

    /**
     * Create a new univocty record marshaller to marshal a POJO to a format supported by
     * <a href="http://www.univocity.com/">uniVocity</a>.
     *
     * @param recordClass the type of object to marshal
     * @param settings    settings used to configure the writer object
     * @param fields      the list of fields to marshal in order
     * @throws IntrospectionException If the object to marshal cannot be introspected
     */
    AbstractUnivocityRecordMarshaller(Class<P> recordClass, S settings, String... fields) throws IntrospectionException {
        this.fieldExtractor = new BeanFieldExtractor<>(recordClass, fields);
        this.settings = settings;
    }

    @Override
    public StringRecord processRecord(Record<P> record) throws Exception {
        stringWriter = new StringWriter();
        AbstractWriter<S> writer = getWriter();
        String[] rowToWrite = extractFields(record.getPayload());

        writer.writeRow(rowToWrite);
        writer.close();

        return new StringRecord(record.getHeader(), stringWriter.toString());
    }


    private String[] extractFields(P payload) throws Exception {
        List<String> resultList = new ArrayList<>();
        Iterable<Object> iterable = fieldExtractor.extractFields(payload);
        for (Object object : iterable) {
            resultList.add(object.toString());
        }
        return resultList.toArray(new String[0]);
    }

    abstract AbstractWriter<S> getWriter();
}

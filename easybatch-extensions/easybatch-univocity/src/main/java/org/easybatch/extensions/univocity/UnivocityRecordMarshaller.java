package org.easybatch.extensions.univocity;

import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
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
 * Created by anthony on 4/12/16.
 */
public class UnivocityRecordMarshaller<P> implements RecordMarshaller<Record<P>, StringRecord> {

    private final Class<P> recordClass;
    private final FieldExtractor<P> fieldExtractor;
    private final CsvWriterSettings settings;

    public UnivocityRecordMarshaller(Class<P> recordClass, CsvWriterSettings settings, String... fields) throws IntrospectionException {
        this.recordClass = recordClass;
        this.fieldExtractor = new BeanFieldExtractor<>(recordClass, fields);
        this.settings = settings;
    }

    @Override
    public StringRecord processRecord(Record<P> record) throws Exception {
        StringWriter stringWriter = new StringWriter();
        CsvWriter csvWriter = new CsvWriter(stringWriter,settings);
        String[] rowToWrite = extractFields(record.getPayload());
        csvWriter.writeRow(rowToWrite);
        csvWriter.close();

        return new StringRecord(record.getHeader(), stringWriter.toString());
    }


    private String[] extractFields(P payload) throws Exception {
        List<String> resultList = new ArrayList<>();
        Iterable<Object>  iterable = fieldExtractor.extractFields(payload);
        for (Object object : iterable) {
            resultList.add(object.toString());
        }
        return resultList.toArray(new String[0]);
    }
}

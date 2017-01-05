package org.easybatch.extensions.yaml;

import com.esotericsoftware.yamlbeans.YamlWriter;
import org.easybatch.core.marshaller.RecordMarshaller;
import org.easybatch.core.record.Record;

import java.io.StringWriter;

public class YamlRecordMarshaller<P> implements RecordMarshaller<Record<P>, YamlRecord> {

    @Override
    public YamlRecord processRecord(Record<P> record) throws Exception {
        StringWriter stringWriter = new StringWriter();
        YamlWriter yamlWriter = new YamlWriter(stringWriter);
        yamlWriter.write(record.getPayload());
        yamlWriter.close();
        return new YamlRecord(record.getHeader(), stringWriter.toString());
    }
}

package org.easybatch.extensions.yaml;

import com.esotericsoftware.yamlbeans.YamlReader;
import org.easybatch.core.mapper.RecordMapper;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Record;

public class YamlRecordMapper<P> implements RecordMapper<YamlRecord, Record<P>> {

    private Class<P> type;

    public YamlRecordMapper(Class<P> type) {
        this.type = type;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Record<P> processRecord(YamlRecord record) throws Exception {
        YamlReader reader = new YamlReader(record.getPayload());
        P unmarshalledObject = reader.read(type);
        reader.close();
        return new GenericRecord<>(record.getHeader(), unmarshalledObject);
    }
}

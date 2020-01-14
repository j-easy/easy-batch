package org.easybatch.tutorials.basic.wordcount;

import org.easybatch.core.mapper.RecordMapper;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;

import java.util.Arrays;
import java.util.List;

/**
 * Mapper that splits each line into a list of words.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class LineTokenizer implements RecordMapper<StringRecord, Record<List<String>>> {

    public Record<List<String>> processRecord(StringRecord record) {
        String payload = record.getPayload();
        return new GenericRecord<>(record.getHeader(), Arrays.asList(payload.split(" ")));
    }

}

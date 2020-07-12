package org.jeasy.batch.tutorials.basic.wordcount;

import org.jeasy.batch.core.mapper.RecordMapper;
import org.jeasy.batch.core.record.GenericRecord;
import org.jeasy.batch.core.record.Record;

import java.util.Arrays;
import java.util.List;

/**
 * Mapper that splits each line into a list of words.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class LineTokenizer implements RecordMapper<String, List<String>> {

    public Record<List<String>> processRecord(Record<String> record) {
        String payload = record.getPayload();
        return new GenericRecord<>(record.getHeader(), Arrays.asList(payload.split(" ")));
    }

}

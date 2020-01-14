package org.easybatch.tutorials.basic.wordcount;

import org.easybatch.core.processor.RecordProcessor;
import org.easybatch.core.record.Record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Processor that counts the number of occurrences of each word.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class WordCounter implements RecordProcessor<Record, Record> {

    private Map<String, Integer> words = new HashMap<>();

    Map<String, Integer> getCount() {
        return words;
    }

    @SuppressWarnings("unchecked")
    public Record processRecord(Record record) {
        List<String> tokens = (List<String>) record.getPayload();
        for (String token : tokens) {
            Integer count = words.get(token);
            count = (count == null) ? 1 : count + 1;
            words.put(token, count);
        }
        return record;
    }
}

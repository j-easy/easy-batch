package org.jeasy.batch.tutorials.basic.wordcount;

import org.jeasy.batch.core.processor.RecordProcessor;
import org.jeasy.batch.core.record.Record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Processor that counts the number of occurrences of each word.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class WordCounter implements RecordProcessor<List<String>, List<String>> {

    private Map<String, Integer> words = new HashMap<>();

    Map<String, Integer> getCount() {
        return words;
    }

    @Override
    public Record<List<String>> processRecord(Record<List<String>> record) {
        List<String> tokens = record.getPayload();
        for (String token : tokens) {
            Integer count = words.get(token);
            count = (count == null) ? 1 : count + 1;
            words.put(token, count);
        }
        return record;
    }
}

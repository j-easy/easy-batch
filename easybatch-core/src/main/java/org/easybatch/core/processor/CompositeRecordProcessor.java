package org.easybatch.core.processor;

import org.easybatch.core.record.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite record processor delegating processing to a pipeline of processors.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class CompositeRecordProcessor implements RecordProcessor {

    private List<RecordProcessor> processors;

    public CompositeRecordProcessor() {
        this.processors = new ArrayList<>();
    }

    @Override
    public Record processRecord(Record record) throws Exception {
        Record processedRecord = record;
        for (RecordProcessor processor : processors) {
            processedRecord = processor.processRecord(processedRecord);
            if (processedRecord == null) {
                return null;
            }
        }
        return processedRecord;
    }

    public void addRecordProcessor(RecordProcessor recordProcessor) {
        processors.add(recordProcessor);
    }
}

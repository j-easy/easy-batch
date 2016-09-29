package org.easybatch.core.processor;

import org.easybatch.core.record.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Record processor that delegates processing to a pipeline of processors.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class CompositeRecordProcessor implements RecordProcessor {

    private List<RecordProcessor> processors;

    /**
     * Create a new {@link CompositeRecordProcessor}.
     */
    public CompositeRecordProcessor() {
        this(new ArrayList<RecordProcessor>());
    }

    /**
     * Create a new {@link CompositeRecordProcessor}.
     *
     * @param processors delegates
     */
    public CompositeRecordProcessor(List<RecordProcessor> processors) {
        this.processors = processors;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
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

    /**
     * Add a delegate record processor.
     *
     * @param recordProcessor to add
     */
    public void addRecordProcessor(RecordProcessor recordProcessor) {
        processors.add(recordProcessor);
    }
}

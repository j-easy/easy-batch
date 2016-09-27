package org.easybatch.core.listener;

import org.easybatch.core.record.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite listener that delegates processing to other listeners.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class CompositePipelineListener implements PipelineListener {

    private List<PipelineListener> listeners;

    /**
     * Create a new {@link CompositePipelineListener}.
     */
    public CompositePipelineListener() {
        this(new ArrayList<PipelineListener>());
    }

    /**
     * Create a new {@link CompositePipelineListener}.
     *
     * @param listeners delegates
     */
    public CompositePipelineListener(List<PipelineListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public Record beforeRecordProcessing(Record record) {
        Record recordToProcess = record;
        for (PipelineListener listener : listeners) {
            recordToProcess = listener.beforeRecordProcessing(recordToProcess);
        }
        return recordToProcess;
    }

    @Override
    public void afterRecordProcessing(Record inputRecord, Record outputRecord) {
        for (PipelineListener listener : listeners) {
            listener.afterRecordProcessing(inputRecord, outputRecord);
        }
    }

    @Override
    public void onRecordProcessingException(Record record, Throwable throwable) {
        for (PipelineListener listener : listeners) {
            listener.onRecordProcessingException(record, throwable);
        }
    }

    public void addPipelineListener(PipelineListener pipelineListener) {
        listeners.add(pipelineListener);
    }
}

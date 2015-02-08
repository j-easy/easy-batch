package org.easybatch.core.util;

import org.easybatch.core.api.RecordDispatcher;
import org.easybatch.core.api.event.global.BatchProcessEventListener;
import org.easybatch.core.record.PoisonRecord;

/**
 * A utility batch event listener that broadcasts a poison record at the end of batch
 * using a delegate {@link RecordDispatcher}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class PoisonRecordBroadcaster implements BatchProcessEventListener {

    private RecordDispatcher recordDispatcher;

    /**
     * Create a new {@link PoisonRecordBroadcaster}.
     * @param recordDispatcher the delegate {@link RecordDispatcher} used to dispatch the poison record
     */
    public PoisonRecordBroadcaster(RecordDispatcher recordDispatcher) {
        this.recordDispatcher = recordDispatcher;
    }

    @Override
    public void beforeBatchStart() {

    }

    @Override
    public void afterBatchEnd() {
        try {
            recordDispatcher.dispatchRecord(new PoisonRecord());
        } catch (Exception e) {
            throw new RuntimeException("Unable to broadcast poison record");
        }
    }

    @Override
    public void onException(Throwable throwable) {

    }
}

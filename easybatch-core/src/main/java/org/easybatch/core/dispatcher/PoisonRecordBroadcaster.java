package org.easybatch.core.dispatcher;

import org.easybatch.core.api.event.batch.BatchProcessEventListener;
import org.easybatch.core.record.PoisonRecord;

/**
 * A utility batch event listener that broadcasts a {@link PoisonRecord} record at the end of batch
 * using a delegate record dispatcher.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class PoisonRecordBroadcaster implements BatchProcessEventListener {

    private AbstractRecordDispatcher recordDispatcher;

    /**
     * Create a new {@link PoisonRecordBroadcaster}.
     * @param recordDispatcher the delegate record dispatcher used to dispatch the poison record
     */
    public PoisonRecordBroadcaster(AbstractRecordDispatcher recordDispatcher) {
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
            throw new RuntimeException("Unable to broadcast poison record.", e);
        }
    }

    @Override
    public void onException(Throwable throwable) {

    }
}

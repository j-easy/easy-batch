package org.easybatch.core.api.event.record;

import org.easybatch.core.api.Record;

/**
 * Event interface collection for RecordReader events.
 * <p>
 * You should implement this interface in your reader to
 * declare the ability to receive events from the batch process.
 *
 * @author Mario Mueller (mario@xenji.com)
 */
public interface RecordReaderEventListener {

    /**
     * Called before the call to {@link org.easybatch.core.api.RecordReader#open()}
     */
    public void beforeReaderOpen();

    /**
     * Called after the call to {@link org.easybatch.core.api.RecordReader#open()}
     */
    public void afterReaderOpen();

    /**
     * Called before each record read
     */
    public void beforeRecordRead();

    /**
     * Called after each record read operation.
     * @param record The record that has been read.
     */
    public void afterRecordRead(Record record);

    /**
     * Called when an exception occurs during record reading.
     * @param throwable the throwable thrown during record reading
     */
    public void onRecordReadException(Throwable throwable);

    /**
     * Called before the reader gets closed.
     */
    public void beforeReaderClose();

    /**
     * Called after the reader has been closed.
     */
    public void afterReaderClose();
}

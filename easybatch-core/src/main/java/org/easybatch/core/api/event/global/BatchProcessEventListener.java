package org.easybatch.core.api.event.global;

/**
 * Enables the implementing class to get an event on different steps within the batch process.
 * <p>
 * Use this interface when you want a listener that is not bound to a specific processor type, otherwise look at:
 * <ul>
 *     <li>{@link org.easybatch.core.api.event.record.RecordReaderEventListener}: for pre/post reading events</li>
 *     <li>{@link org.easybatch.core.api.event.record.RecordFilterEventListener}: for pre/post filtering events</li>
 *     <li>{@link org.easybatch.core.api.event.record.RecordMapperEventListener}: for pre/post mapping events</li>
 *     <li>{@link org.easybatch.core.api.event.record.RecordValidatorEventListener}: for pre/post validating events</li>
 *     <li>{@link org.easybatch.core.api.event.record.RecordProcessorEventListener}: for pre/post processing events</li>
 * </ul>
 *
 * @author Mario Mueller (mario@xenji.com)
 */
public interface BatchProcessEventListener {

    /**
     * Happens before the {@link org.easybatch.core.api.RecordReader#open()} call.
     */
    public void beforeBatchStart();

    /**
     * Happens after the {@link org.easybatch.core.api.RecordReader#close()} call.
     */
    public void afterBatchEnd();

    /**
     * Happens on any throwable event while processing.
     * <p>
     * There is no context information available, so this will probably something you want to use for logging
     * purposes or similar.
     *
     * @param throwable The exception thrown at the time of call.
     */
    public void onException(Throwable throwable);
}

package org.easybatch.core.listener;

import org.easybatch.core.job.JobParameters;
import org.easybatch.core.job.JobReport;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Composite listener that delegates processing to other listeners.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class CompositeJobListener implements JobListener {

    private List<JobListener> listeners;

    /**
     * Create a new {@link CompositeJobListener}.
     */
    public CompositeJobListener() {
        this(new ArrayList<JobListener>());
    }

    /**
     * Create a new {@link CompositeJobListener}.
     *
     * @param listeners delegates
     */
    public CompositeJobListener(List<JobListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void beforeJobStart(JobParameters jobParameters) {
        for (JobListener listener : listeners) {
            listener.beforeJobStart(jobParameters);
        }
    }

    @Override
    public void afterJobEnd(JobReport jobReport) {
        for (ListIterator<JobListener> iterator
                = listeners.listIterator(listeners.size());
                iterator.hasPrevious();) {
            iterator.previous().afterJobEnd(jobReport);
        }
    }

    /**
     * Add a delegate listener.
     *
     * @param jobListener to add
     */
    public void addJobListener(JobListener jobListener) {
        listeners.add(jobListener);
    }
}

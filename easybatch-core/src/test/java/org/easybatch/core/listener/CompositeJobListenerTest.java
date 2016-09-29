package org.easybatch.core.listener;

import org.easybatch.core.job.JobParameters;
import org.easybatch.core.job.JobReport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.inOrder;

@RunWith(MockitoJUnitRunner.class)
public class CompositeJobListenerTest {

    @Mock
    private JobParameters parameters;
    @Mock
    private JobReport report;
    @Mock
    private JobListener jobListener1, jobListener2;

    private CompositeJobListener compositeJobListener;

    @Before
    public void setUp() throws Exception {
        compositeJobListener = new CompositeJobListener(asList(jobListener1, jobListener2));
    }

    @Test
    public void testBeforeJobStart() throws Exception {
        compositeJobListener.beforeJobStart(parameters);

        InOrder inOrder = inOrder(jobListener1, jobListener2);
        inOrder.verify(jobListener1).beforeJobStart(parameters);
        inOrder.verify(jobListener2).beforeJobStart(parameters);
    }

    @Test
    public void testAfterJobEnd() throws Exception {
        compositeJobListener.afterJobEnd(report);

        InOrder inOrder = inOrder(jobListener1, jobListener2);
        inOrder.verify(jobListener1).afterJobEnd(report);
        inOrder.verify(jobListener2).afterJobEnd(report);
    }
}
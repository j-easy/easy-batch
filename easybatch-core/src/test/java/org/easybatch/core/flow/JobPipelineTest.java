package org.easybatch.core.flow;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobReport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.flow.JobPipelineBuilder.aNewJobPipelineBuilder;
import static org.easybatch.core.flow.predicate.JobCompleted.jobCompleted;
import static org.easybatch.core.flow.predicate.JobFailed.jobFailed;
import static org.easybatch.core.job.JobStatus.COMPLETED;
import static org.easybatch.core.job.JobStatus.FAILED;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobPipelineTest {

    @Mock
    private Job job1, job2, job3;
    @Mock
    private JobReport jobReport1, jobReport2, jobReport3;

    @Test
    public void testJobPipeline() throws Exception {
        when(job1.call()).thenReturn(jobReport1);
        when(jobReport1.getStatus()).thenReturn(COMPLETED);
        when(job2.call()).thenReturn(jobReport2);
        when(jobReport2.getStatus()).thenReturn(FAILED);
        when(job3.call()).thenReturn(jobReport3);

        JobPipeline jobPipeline = aNewJobPipelineBuilder()
                .startWith(job1)
                .when(jobCompleted()).then(job2)
                .when(jobFailed()).then(job3)
                .build();

        JobReport lastJobReport = jobPipeline.call();

        verify(job1).call();
        verify(job2).call();
        verify(job3).call();
        assertThat(lastJobReport).isEqualTo(jobReport3);
    }
}
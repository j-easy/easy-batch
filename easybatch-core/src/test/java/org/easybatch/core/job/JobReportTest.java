package org.easybatch.core.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class JobReportTest {

  @Test
  public void testJobReport() throws Exception {
    //Given
    JobReport jobReport = new JobReport();
    jobReport.setJobName("jobName");
    jobReport.setLastError(null);
    jobReport.setMetrics(new JobMetrics());
    jobReport.setParameters(new JobParameters());
    jobReport.setStatus(JobStatus.COMPLETED);
    //When
    String result = jobReport.toString();
    //Then
    assertTrue(result.contains("jobName"));
    assertTrue(result.contains("COMPLETED"));
  }


}
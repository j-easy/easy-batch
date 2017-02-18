package org.easybatch.core.job.pipeline;

import org.easybatch.core.job.JobReport;

import java.util.HashSet;
import java.util.Set;

public class StageReport {

    private Set<JobReport> jobReports;

    public StageReport() {
        jobReports = new HashSet<>();
    }

    public StageReport(Set<JobReport> jobReports) {
        this.jobReports = jobReports;
    }

    public void add(JobReport jobReport) {
        jobReports.add(jobReport);
    }


    public Set<JobReport> getJobReports() {
        return jobReports;
    }

}

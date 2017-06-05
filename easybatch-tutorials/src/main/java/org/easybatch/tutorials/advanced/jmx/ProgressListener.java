package org.easybatch.tutorials.advanced.jmx;

import org.easybatch.core.jmx.JobMonitoringListener;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.job.JobStatus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ProgressListener extends JobMonitoringListener {

    private long totalRecords;

    public ProgressListener(final File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            scanner.nextLine();
            totalRecords++;
        }
    }

    @Override
    public void onJobReportUpdate(JobReport jobReport) {
        if (jobReport.getStatus().equals(JobStatus.COMPLETED)) {
            System.out.println("Done!");
            return;
        }
        long readCount = jobReport.getMetrics().getReadCount();
        System.out.print("\rprogress = " + ( (double) readCount / (double) totalRecords) * 100 + "%");
    }

    @Override
    public void onConnectionOpened() {

    }

    @Override
    public void onConnectionClosed() {

    }
}

package org.jeasy.batch.tutorials.advanced.jmx;

import org.jeasy.batch.core.jmx.JobMonitoringListener;
import org.jeasy.batch.core.job.JobReport;
import org.jeasy.batch.core.job.JobStatus;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class ProgressListener extends JobMonitoringListener {

    private long totalRecords;

    public ProgressListener(final Path file) throws IOException {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            scanner.nextLine();
            totalRecords++;
        }
        scanner.close();
    }

    @Override
    public void onJobReportUpdate(JobReport jobReport) {
        if (jobReport.getStatus().equals(JobStatus.COMPLETED)) {
            System.out.println("Done!");
            return;
        }
        long readCount = jobReport.getMetrics().getReadCount();
        System.out.println("progress = " + ( (double) readCount / (double) totalRecords) * 100 + "%");
    }

    @Override
    public void onConnectionOpened() {

    }

    @Override
    public void onConnectionClosed() {

    }
}

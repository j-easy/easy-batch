package org.easybatch.core.job;

import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiThreadedBatchJob extends BatchJob {

    private static final Logger LOGGER = Logger.getLogger(MultiThreadedBatchJob.class.getName());

    private final ExecutorService executorService;
    private final boolean jmxMonitoring;
    private final long errorThreshold;
    private final int batchSize;

    MultiThreadedBatchJob(JobParameters parameters, int numThreads) {
        super(parameters);
        this.executorService = Executors.newFixedThreadPool(numThreads);
        this.batchSize = parameters.getBatchSize();
        jmxMonitoring = parameters.isJmxMonitoring();
        errorThreshold = parameters.getErrorThreshold();
    }

    @Override
    Batch readAndProcessBatch() throws RecordReadingException, ErrorThresholdExceededException {
        batchListener.beforeBatchReading();
        List<Callable<Record>> taskList = new ArrayList<>(batchSize);
        for (int i = 0; i < batchSize; i++) {
            final Record record = readRecord();
            if (record == null) {
                recordTracker.noMoreRecords();
                break;
            } else {
                metrics.incrementReadCount();
            }
            taskList.add(generateCallable(record));
        }

        try {
            List<Future<Record>> futureRecordList = executorService.invokeAll(taskList);
            Batch batch = new Batch();
            addRecordsToBatch(batch, futureRecordList);
            batchListener.afterBatchProcessing(batch);
            return batch;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void addRecordsToBatch(Batch batch, List<Future<Record>> futureList) throws ErrorThresholdExceededException {
        for (Future<Record> recordFuture : futureList) {
            try {
                Record record = recordFuture.get();
                if (record != null) {
                    batch.addRecord(record);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                if (e.getCause() instanceof ErrorThresholdExceededException) {
                    throw (ErrorThresholdExceededException) e.getCause();
                }
                throw new RuntimeException(e);
            }
        }
    }

    private Callable<Record> generateCallable(final Record record) {
        return new Callable<Record>() {
            @Override
            public Record call() throws Exception {
                return processRecord(record, jmxMonitoring, errorThreshold);
            }
        };
    }
}

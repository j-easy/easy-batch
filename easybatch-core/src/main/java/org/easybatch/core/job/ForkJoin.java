package org.easybatch.core.job;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.easybatch.core.jmx.JobMonitorProxy;
import org.easybatch.core.jmx.JobMonitoringListener;
import org.easybatch.core.record.Record;

/**
 * Base structure to create a multi-thread job. The user provide the number of
 * thread in which will be split the principal job and the implementation of
 * three task: "fork job", "workers job" and "join job". The number of threads
 * created is equal the number provided more two, that are the fork and join
 * job. Is returned a list of {@link JobReport}
 * 
 * @author Somma Daniele
 */
public abstract class ForkJoin {

  private static final String DEFAULT_HOST = "localhost";
  private static final int    DEFAULT_PORT = 9999;

  private final int           threadPoolSize;
  private final int           nWorkQueue;
  private final int           nPoisonRecord;
  private final String        name;
  private final boolean       jmx;

  private Job                 forkJob;
  private List<Job>           workersJob;
  private Job                 joinJob;
  private List<Thread>        jobMonitoringListener;

  protected ForkJoin(String name, int threadPoolSize, boolean jmx) {
    this(name, threadPoolSize, jmx, DEFAULT_HOST, DEFAULT_PORT);
  }

  protected ForkJoin(String name, int threadPoolSize, boolean jmx, String host, int port) {
    super();
    this.nWorkQueue = threadPoolSize;
    this.threadPoolSize = threadPoolSize + 2;
    this.nPoisonRecord = threadPoolSize;
    this.name = name;
    this.jmx = jmx;

    setup(host, port);
  }

  private void setup(String host, int port) {
    BlockingQueue<Record> joinQueue = new LinkedBlockingQueue<>();
    List<BlockingQueue<Record>> workQueues = new ArrayList<>(nWorkQueue);
    workersJob = new ArrayList<>(nWorkQueue);
    if (jmx) {
      jobMonitoringListener = new ArrayList<>(nWorkQueue);
    }
    for (int i = 0; i < nWorkQueue; i++) {
      BlockingQueue<Record> workQueue = new LinkedBlockingQueue<>();
      workQueues.add(workQueue);
      String jobName = name + "-thread-" + (i + 1);
      workersJob.add(buildWorkerJob(jobName, workQueue, joinQueue, nWorkQueue, jmx));
      // A job monitor proxy for each workers...
      if (jmx) {
        jobMonitoringListener.add(buildJobMonitoringListener(host, port, jobName));
      }
    }
    forkJob = buildForkJob(name + "-fork-job", workQueues);
    joinJob = buildJoinJob(name + "-join-job", joinQueue, nPoisonRecord);
  }

  private Thread buildJobMonitoringListener(String host, int port, String jobName) {
    JobMonitorProxy jm = new JobMonitorProxy(host, port, jobName);
    for (JobMonitoringListener jml : buildJobMonitoringListeners()) {
      jm.addMonitoringListener(jml);
    }

    Thread thread = new Thread(jm);
    thread.setDaemon(true);
    return thread;
  }

  private void startJobMonitoringListener() {
    for (Thread thread : jobMonitoringListener) {
      thread.start();
    }
  }

  public List<Future<JobReport>> run() {
    if (jmx) {
      startJobMonitoringListener();
    }

    // Create executor and submit jobs in parallel...
    JobExecutor je = new JobExecutor(threadPoolSize);
    List<Job> jobToSubmit = new LinkedList<>();
    jobToSubmit.add(forkJob);
    jobToSubmit.addAll(workersJob);
    jobToSubmit.add(joinJob);
    List<Future<JobReport>> jobsReport = je.submitAll(jobToSubmit);
    je.shutdown();
    return jobsReport;
  }

  protected abstract Job buildForkJob(final String jobName, final List<BlockingQueue<Record>> workQueues);

  protected abstract Job buildWorkerJob(final String jobName, final BlockingQueue<Record> workQueue, final BlockingQueue<Record> joinQueue, int batchSize,
      boolean jmx);

  protected abstract Job buildJoinJob(final String jobName, final BlockingQueue<Record> joinQueue, final int nPoisonRecord);

  protected abstract List<JobMonitoringListener> buildJobMonitoringListeners();

}
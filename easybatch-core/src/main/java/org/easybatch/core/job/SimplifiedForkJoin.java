package org.easybatch.core.job;

import static org.easybatch.core.job.JobBuilder.aNewJob;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.easybatch.core.filter.PoisonRecordFilter;
import org.easybatch.core.jmx.JobMonitoringListener;
import org.easybatch.core.listener.PoisonRecordBroadcaster;
import org.easybatch.core.reader.BlockingQueueRecordReader;
import org.easybatch.core.record.Record;
import org.easybatch.core.writer.BlockingQueueRecordWriter;
import org.easybatch.core.writer.RoundRobinBlockingQueueRecordWriter;

/**
 * Simply and common configuration of multi-thread job. The three principal job
 * are created with a base configuration where records are splitted in
 * "BlockingQueue", one for each "workers job". "Fork job" add in each queue a
 * "Poison record" at the end. "Workers job" process the record in the queues
 * and write the record in the join queue, "Join job" filter/remove the "Poison
 * record". User must decorate the base configuration providing readers,
 * mappers, listeners, writers, ecc, ecc. Important point is the filters in
 * "workers job" must preserve the "Poison record" in the queue.
 * 
 * @author Somma Daniele
 */
public abstract class SimplifiedForkJoin extends ForkJoin {

  protected SimplifiedForkJoin(String name, int threadPoolSize, boolean jmx) {
    super(name, threadPoolSize, jmx);
  }

  @Override
  protected Job buildForkJob(String jobName, List<BlockingQueue<Record>> workQueues) {
    JobBuilder jb = aNewJob().named(jobName);
    builderForkJob(jb);
    return jb.writer(new RoundRobinBlockingQueueRecordWriter(workQueues)).jobListener(new PoisonRecordBroadcaster(workQueues)).build();
  }

  @Override
  protected Job buildWorkerJob(String jobName, BlockingQueue<Record> workQueue, BlockingQueue<Record> joinQueue, int batchSize, boolean jmx) {
    JobBuilder jb = aNewJob().named(jobName).batchSize(batchSize);
    if (jmx) {
      jb.enableJmx();
    }
    jb.reader(new BlockingQueueRecordReader(workQueue));
    builderWorkerJob(jb);
    return jb.writer(new BlockingQueueRecordWriter(joinQueue)).build();
  }

  @Override
  protected Job buildJoinJob(String jobName, BlockingQueue<Record> joinQueue, int nPoisonRecord) {
    JobBuilder jb = aNewJob().named(jobName).reader(new BlockingQueueRecordReader(joinQueue, nPoisonRecord)).filter(new PoisonRecordFilter());
    builderJoinJob(jb);
    return jb.build();
  }

  @Override
  protected List<JobMonitoringListener> buildJobMonitoringListeners() {
    return null;
  }

  public abstract void builderForkJob(JobBuilder jobBuilder);

  public abstract void builderWorkerJob(JobBuilder jobBuilder);

  public abstract void builderJoinJob(JobBuilder jobBuilder);

}
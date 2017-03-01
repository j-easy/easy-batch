package org.easybatch.core.flow2;

import org.easybatch.core.dispatcher.PoisonRecordBroadcaster;
import org.easybatch.core.filter.PoisonRecordFilter;
import org.easybatch.core.job.Job;
import org.easybatch.core.reader.BlockingQueueRecordReader;
import org.easybatch.core.reader.StringRecordReader;
import org.easybatch.core.record.Record;
import org.easybatch.core.writer.BlockingQueueRecordWriter;
import org.easybatch.core.writer.CollectionRecordWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.Collections.singletonList;
import static org.easybatch.core.flow2.JobState.SUCCESS;
import static org.easybatch.core.job.JobBuilder.aNewJob;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

/**
 * Created by Sunand on 5/25/2016.
 */
public class JobPipelineTest {
  public static void main(String[] args) throws Exception {

    String dataSource = "foo" + LINE_SEPARATOR + "bar";
    BlockingQueue<Record> buffer = new LinkedBlockingQueue<>();
    List<String> records = new ArrayList<>();

    Job job1 = aNewJob()
            .named("job1")
            .reader(new StringRecordReader(dataSource))
            .writer(new BlockingQueueRecordWriter<>(buffer))
            .jobListener(new PoisonRecordBroadcaster<>(singletonList(buffer)))
            .build();

    Job job2 = aNewJob()
            .named("job2")
            .reader(new BlockingQueueRecordReader<>(buffer))
            .filter(new PoisonRecordFilter())
            .writer(new CollectionRecordWriter(records))
            .build();

    Job job3 = aNewJob()
            .named("job3")
            .reader(new StringRecordReader("Job1 Failed"))
            .writer(new CollectionRecordWriter(records))
            .build();

    FlowDefinition flowDefinition = new FlowDefinition();
    final WorkflowGraph<Job> workflowGraph = flowDefinition
            .flow(job1, SUCCESS, job2)
            .flow(job1, SUCCESS, job3)
            .build();

    workflowGraph.breadthFirstSearch(workflowGraph.getRootVertex(), new JobVisitor());

    //switch the scenario above to see the results
    System.out.println(records);

  }
}

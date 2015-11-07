package ${packageName};

import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.job.Job;
import org.easybatch.core.reader.StringRecordReader;

import static org.easybatch.core.job.JobBuilder.aNewJob;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

public class App {

    public static void main(String[] args) {

        // Create tweets data source (tweet: id,user,content)
        String dataSource =
                "1,foo,easy batch is really easy! #EasyBatch" + LINE_SEPARATOR +
                        "2,bar,@foo I do confirm :-)";

        // Define a job
        Job job = aNewJob()
                .reader(new StringRecordReader(dataSource))
                .processor(new TweetProcessor())
                .build();

        // Execute the job
        JobReport jobReport = JobExecutor.execute(job);

        // Print the job execution report
        System.out.println(jobReport);
    }

}

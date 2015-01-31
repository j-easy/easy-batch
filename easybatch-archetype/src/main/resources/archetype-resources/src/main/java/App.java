package ${packageName};

import org.easybatch.core.api.Report;
import org.easybatch.core.impl.Engine;
import org.easybatch.core.reader.StringRecordReader;

import static org.easybatch.core.impl.EngineBuilder.aNewEngine;

public class App {

    public static void main(String[] args) throws Exception {

        // Create tweets data source (tweet: id,user,content)
        String dataSource =
                "1,foo,easy batch rocks! #EasyBatch\n" +
                "2,bar,@foo I do confirm :-)";

        // Build a batch engine
        Engine engine = aNewEngine()
                .reader(new StringRecordReader(dataSource))
                .processor(new TweetProcessor())
                .build();

        // Run the batch engine
        Report report = engine.call();

        // Print the batch execution report
        System.out.println(report);
    }

}

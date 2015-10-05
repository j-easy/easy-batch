package ${packageName};

import org.easybatch.core.api.Report;
import org.easybatch.core.api.Engine;
import org.easybatch.core.reader.StringRecordReader;

import static org.easybatch.core.impl.EngineBuilder.aNewEngine;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

public class App {

    public static void main(String[] args) throws Exception {

        // Create tweets data source (tweet: id,user,content)
        String dataSource =
                "1,foo,easy batch rocks! #EasyBatch" + LINE_SEPARATOR +
                "2,bar,@foo I do confirm :-)";

        // Build a batch job engine
        Engine engine = aNewEngine()
                .reader(new StringRecordReader(dataSource))
                .processor(new TweetProcessor())
                .build();

        // Run the engine
        Report report = engine.call();

        // Print the job execution report
        System.out.println(report);
    }

}

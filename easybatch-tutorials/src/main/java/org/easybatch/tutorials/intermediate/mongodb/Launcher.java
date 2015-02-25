package org.easybatch.tutorials.intermediate.mongodb;

import com.mongodb.MongoClient;
import org.easybatch.core.filter.HeaderRecordFilter;
import org.easybatch.core.impl.Engine;
import org.easybatch.core.impl.EngineBuilder;
import org.easybatch.flatfile.FlatFileRecordReader;
import org.easybatch.flatfile.dsv.DelimitedRecordMapper;
import org.easybatch.tutorials.common.Tweet;

import java.io.File;

/**
 * Main class to run MongoDB tutorial.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class Launcher {

    public static void main(String[] args) throws Exception {

        // create a mongo client, mongod should be up and running on default port (27017)
        MongoClient mongoClient = new MongoClient();

        //load tweets from tweets.csv
        File tweets = new File(Launcher.class
                .getResource("/org/easybatch/tutorials/basic/keyapis/tweets.csv").toURI());

        Engine engine = new EngineBuilder()
                .reader(new FlatFileRecordReader(tweets))
                .filter(new HeaderRecordFilter())
                .mapper(new DelimitedRecordMapper<Tweet>(Tweet.class, new String[]{"id", "user", "message"}))
                .processor(new TweetLoader(mongoClient, "test", "tweets"))
                .build();

        // Run easy batch engine
        engine.call();

    }
}

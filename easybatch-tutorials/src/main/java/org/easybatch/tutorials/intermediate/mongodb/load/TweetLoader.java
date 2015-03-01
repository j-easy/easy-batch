package org.easybatch.tutorials.intermediate.mongodb.load;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import org.easybatch.core.api.RecordProcessor;
import org.easybatch.tutorials.common.Tweet;

/**
 * A record processor that inserts tweets in MongoDB.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class TweetLoader implements RecordProcessor<Tweet, Tweet> {

    private DBCollection tweetsCollection;

    public TweetLoader(MongoClient client, String database,  String collection) {
        tweetsCollection = client.getDB(database).getCollection(collection);
    }

    @Override
    public Tweet processRecord(Tweet tweet) throws Exception {

        DBObject dbObject = new BasicDBObject()
                .append("_id", tweet.getId())
                .append("user", tweet.getUser())
                .append("message", tweet.getMessage());

        tweetsCollection.insert(dbObject);

        return tweet;
    }
}

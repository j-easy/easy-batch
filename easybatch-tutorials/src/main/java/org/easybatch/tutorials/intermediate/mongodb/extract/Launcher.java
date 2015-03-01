/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.tutorials.intermediate.mongodb.extract;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.easybatch.core.impl.Engine;
import org.easybatch.core.impl.EngineBuilder;
import org.easybatch.integration.mongodb.MongoDBRecordMapper;
import org.easybatch.integration.mongodb.MongoDBRecordReader;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Main class to export tweets from MongoDB to an XML file.
 *
 * <strong>Pre requisite: mongod should be up and running on default port (27017)</strong>
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class Launcher {

    public static void main(String[] args) throws Exception {

        // create a mongo client
        MongoClient mongoClient = new MongoClient();
        DBCollection tweetsCollections = mongoClient.getDB("test").getCollection("tweets");

        // create output file tweets.xml
        String outputDirectory = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator");
        String fileName = outputDirectory + "tweets.xml";
        OutputStream tweets = new FileOutputStream(fileName);

        Engine engine = new EngineBuilder()
                .reader(new MongoDBRecordReader(tweetsCollections, new BasicDBObject()))
                .mapper(new MongoDBRecordMapper<Tweet>(Tweet.class))
                .processor(new TweetExporter(tweets))
                .batchProcessEventListener(new TweetExporterBatchEventListener(tweets))
                .build();

        // run easy batch engine
        engine.call();

        System.out.println("Successfully exported tweets in : " + fileName);

        mongoClient.close();
    }
}

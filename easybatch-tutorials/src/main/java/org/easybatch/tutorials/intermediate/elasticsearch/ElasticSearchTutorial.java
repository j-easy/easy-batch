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

package org.easybatch.tutorials.intermediate.elasticsearch;

import org.easybatch.core.impl.Engine;
import org.easybatch.core.impl.EngineBuilder;
import org.easybatch.json.JsonRecordReader;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.io.InputStream;

/**
 * Main class to launch Elastic search tutorial.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class ElasticSearchTutorial {

    public static void main(String[] args) throws IOException {

        InputStream tweetsInputStream = ElasticSearchTutorial.class.getResourceAsStream("tweets.json");

        //start an embedded elastic search node
        Node node = ElasticSearchUtils.startEmbeddedNode();
        Client client = node.client();

        // Build a batch engine
        Engine engine = new EngineBuilder()
                .reader(new JsonRecordReader(tweetsInputStream))
                .processor(new ElasticSearchIndexer(client))
                .build();

        // Run the batch engine
        engine.call();

        //check if tweets have been successfully indexed in elastic search
        node.client().admin().indices().prepareRefresh().execute().actionGet();
        SearchResponse searchResponse = node.client().prepareSearch()
                .setQuery(QueryBuilders.matchAllQuery())
                .execute().actionGet();

        System.out.println("Total tweets = " + searchResponse.getHits().totalHits());
        for (SearchHit searchHitFields : searchResponse.getHits().getHits()) {
            System.out.println("tweet: " + searchHitFields.getSourceAsString());
        }

        //shutdown elastic search node
        ElasticSearchUtils.stopEmbeddedNode(node);
    }

}

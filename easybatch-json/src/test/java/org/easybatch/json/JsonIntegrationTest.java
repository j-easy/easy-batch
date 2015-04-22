package org.easybatch.json;

import org.easybatch.core.api.ComputationalRecordProcessor;
import org.easybatch.core.api.Report;
import org.easybatch.core.api.Status;
import org.easybatch.core.impl.Engine;
import org.easybatch.core.impl.EngineBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for json processing.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JsonIntegrationTest {

    private static final String EXPECTED_DATA_SOURCE_NAME = "Json stream";

    @SuppressWarnings("unchecked")
    @Test
    public void testJsonProcessing() throws Exception {

        final InputStream jsonDataSource = this.getClass().getResourceAsStream("/tweets.json");
        final ComputationalRecordProcessor recordProcessor = new TweetProcessor();

        Engine engine = EngineBuilder.aNewEngine()
                .reader(new JsonRecordReader(jsonDataSource))
                .processor(recordProcessor)
                .build();

        Report report = engine.call();

        assertThat(report).isNotNull();
        assertThat(report.getTotalRecords()).isEqualTo(2);
        assertThat(report.getErrorRecordsCount()).isEqualTo(0);
        assertThat(report.getFilteredRecordsCount()).isEqualTo(0);
        assertThat(report.getIgnoredRecordsCount()).isEqualTo(0);
        assertThat(report.getRejectedRecordsCount()).isEqualTo(0);
        assertThat(report.getSuccessRecordsCount()).isEqualTo(2);
        assertThat(report.getStatus()).isEqualTo(Status.FINISHED);
        assertThat(report.getDataSource()).isEqualTo(EXPECTED_DATA_SOURCE_NAME);

        List<JsonRecord> tweets = (List<JsonRecord>) recordProcessor.getComputationResult();

        assertThat(tweets).isNotEmpty().hasSize(2);

        final JsonRecord tweet1 = tweets.get(0);
        assertThat(tweet1.getHeader()).isNotNull();
        assertThat(tweet1.getHeader().getNumber()).isEqualTo(1);
        assertThat(tweet1.getPayload()).isEqualTo("{\"id\":1,\"user\":\"foo\",\"message\":\"Hello\"}");

        final JsonRecord tweet2 = tweets.get(1);
        assertThat(tweet2.getHeader()).isNotNull();
        assertThat(tweet2.getHeader().getNumber()).isEqualTo(2);
        assertThat(tweet2.getPayload()).isEqualTo("{\"id\":2,\"user\":\"bar\",\"message\":\"Hi!\"}");

    }

    private class TweetProcessor implements ComputationalRecordProcessor<JsonRecord, JsonRecord, List<JsonRecord>> {

        private List<JsonRecord> tweets = new ArrayList<JsonRecord>();

        @Override
        public JsonRecord processRecord(JsonRecord tweet) throws Exception {
            tweets.add(tweet);
            return tweet;
        }

        @Override
        public List<JsonRecord> getComputationResult() {
            return tweets;
        }

    }

}

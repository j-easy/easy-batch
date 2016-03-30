/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package org.easybatch.json;

import org.easybatch.core.job.*;
import org.easybatch.core.processor.RecordCollector;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unchecked")
public class JsonIntegrationTest {

    private static final String EXPECTED_DATA_SOURCE_NAME = "Json stream";

    @Test
    public void testTweetsJsonStreamProcessing() throws Exception {

        final InputStream jsonDataSource = getDataSource("/tweets.json");

        Job job = JobBuilder.aNewJob()
                .reader(new JsonRecordReader(jsonDataSource))
                .processor(new RecordCollector())
                .build();

        JobReport jobReport = JobExecutor.execute(job);

        assertThatReportIsValid(jobReport);

        List<JsonRecord> tweets = (List<JsonRecord>) jobReport.getResult();

        assertThat(tweets).isNotEmpty().hasSize(3);

        JsonRecord tweet = tweets.get(0);
        assertThat(tweet.getHeader()).isNotNull();
        assertThat(tweet.getHeader().getNumber()).isEqualTo(1);
        assertThat(tweet.getPayload()).isEqualTo("{\"id\":1,\"user\":\"foo\",\"message\":\"Hello\"}");

        tweet = tweets.get(1);
        assertThat(tweet.getHeader()).isNotNull();
        assertThat(tweet.getHeader().getNumber()).isEqualTo(2);
        assertThat(tweet.getPayload()).isEqualTo("{\"id\":2,\"user\":\"bar\",\"message\":\"Hi!\"}");

        tweet = tweets.get(2);
        assertThat(tweet.getHeader()).isNotNull();
        assertThat(tweet.getHeader().getNumber()).isEqualTo(3);
        assertThat(tweet.getPayload()).isEqualTo("{\"id\":3,\"user\":\"toto\",\"message\":\"yep ;-)\"}");

    }

    @Test
    public void testTreesJsonStreamProcessing() throws Exception {

        //data source : http://opendata.paris.fr/explore/dataset/arbresalignementparis2010/download/?format=csv
        final InputStream jsonDataSource = getDataSource("/trees.json");

        Job job = JobBuilder.aNewJob()
                .reader(new JsonRecordReader(jsonDataSource))
                .processor(new RecordCollector())
                .build();

        JobReport jobReport = JobExecutor.execute(job);

        assertThatReportIsValid(jobReport);

        List<JsonRecord> trees = (List<JsonRecord>) jobReport.getResult();
        assertThat(trees).hasSize(3);

        JsonRecord record = trees.get(0);
        assertThat(record.getHeader().getNumber()).isEqualTo(1);
        assertThat(record.getPayload()).isEqualTo("{\"datasetid\":\"arbresalignementparis2010\",\"recordid\":\"0e6cfe03082224225c54690e5a700987ffb1310f\",\"fields\":{\"adresse\":\"AVENUE GAMBETTA\",\"hauteurenm\":0.0,\"espece\":\"Tilia tomentosa\",\"circonfere\":78.0,\"geom_x_y\":[48.8691944661,2.40210336054],\"dateplanta\":\"1971-02-27\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[2.40210336054,48.8691944661]},\"record_timestamp\":\"2014-09-11T14:39:16.131843\"}");

        record = trees.get(1);
        assertThat(record.getHeader().getNumber()).isEqualTo(2);
        assertThat(record.getPayload()).isEqualTo("{\"datasetid\":\"arbresalignementparis2010\",\"recordid\":\"f737f8efb565bbd36092495a9899ec612fe06d5e\",\"fields\":{\"circonfere\":0.0,\"hauteurenm\":0.0,\"espece\":\"Gymnocladus dioica\",\"adresse\":\"BOULEVARD SERURIER\",\"geom_x_y\":[48.8794011563,2.4006442003]},\"geometry\":{\"type\":\"Point\",\"coordinates\":[2.4006442003,48.8794011563]},\"record_timestamp\":\"2014-09-11T14:39:16.131843\"}");

        record = trees.get(2);
        assertThat(record.getHeader().getNumber()).isEqualTo(3);
        assertThat(record.getPayload()).isEqualTo("{\"datasetid\":\"arbresalignementparis2010\",\"recordid\":\"51c45ad287d0c91357545f5812ff79ed629c50c9\",\"fields\":{\"circonfere\":56.0,\"hauteurenm\":0.0,\"espece\":\"Platanus x hispanica\",\"adresse\":\"BOULEVARD BEAUMARCHAIS\",\"geom_x_y\":[48.8605686299,2.36720956668]},\"geometry\":{\"type\":\"Point\",\"coordinates\":[2.36720956668,48.8605686299]},\"record_timestamp\":\"2014-09-11T14:39:16.131843\"}");

    }

    @Test
    public void testComplaintsJsonStreamProcessing() throws Exception {

        // data source: http://catalog.data.gov/dataset/consumer-complaint-database
        final InputStream jsonDataSource = getDataSource("/complaints.json");

        Job job = JobBuilder.aNewJob()
                .reader(new JsonRecordReader(jsonDataSource))
                .processor(new RecordCollector())
                .build();

        JobReport jobReport = JobExecutor.execute(job);

        assertThatReportIsValid(jobReport);

        List<JsonRecord> complaints = (List<JsonRecord>) jobReport.getResult();
        assertThat(complaints).hasSize(3);

        JsonRecord record = complaints.get(0);
        assertThat(record.getHeader().getNumber()).isEqualTo(1);
        assertThat(record.getPayload()).isEqualTo("{\"id\":\"25ei-6bcr\",\"name\":\"Credit Card Complaints\",\"averageRating\":0,\"createdAt\":1337199939,\"displayType\":\"table\",\"downloadCount\":5011,\"indexUpdatedAt\":1414647847,\"newBackend\":false,\"numberOfComments\":0,\"oid\":2956892,\"publicationAppendEnabled\":false,\"publicationDate\":1364274981,\"publicationGroup\":342069,\"publicationStage\":\"published\",\"rowIdentifierColumnId\":53173967,\"rowsUpdatedAt\":1364274443,\"rowsUpdatedBy\":\"dfzt-mv86\",\"tableId\":756116,\"totalTimesRated\":0,\"viewCount\":68124,\"viewLastModified\":1364274981,\"viewType\":\"tabular\",\"grants\":[{\"inherited\":true,\"type\":\"viewer\",\"flags\":[\"public\"]}],\"metadata\":{\"custom_fields\":{\"TEST\":{\"CFPB1\":\"\"}},\"renderTypeConfig\":{\"visible\":{\"table\":true}},\"availableDisplayTypes\":[\"table\",\"fatrow\",\"page\"],\"rdfSubject\":\"0\",\"rowIdentifier\":53173967},\"owner\":{\"id\":\"dfzt-mv86\",\"displayName\":\"CFPB Administrator\",\"roleName\":\"publisher\",\"screenName\":\"CFPB Administrator\",\"rights\":[\"create_datasets\",\"edit_others_datasets\",\"edit_nominations\",\"approve_nominations\",\"moderate_comments\",\"manage_stories\",\"feature_items\",\"change_configurations\",\"view_domain\",\"view_others_datasets\",\"create_pages\",\"edit_pages\",\"view_goals\",\"view_dashboards\",\"edit_goals\",\"edit_dashboards\"]},\"rights\":[\"read\"],\"tableAuthor\":{\"id\":\"dfzt-mv86\",\"displayName\":\"CFPB Administrator\",\"roleName\":\"publisher\",\"screenName\":\"CFPB Administrator\",\"rights\":[\"create_datasets\",\"edit_others_datasets\",\"edit_nominations\",\"approve_nominations\",\"moderate_comments\",\"manage_stories\",\"feature_items\",\"change_configurations\",\"view_domain\",\"view_others_datasets\",\"create_pages\",\"edit_pages\",\"view_goals\",\"view_dashboards\",\"edit_goals\",\"edit_dashboards\"]},\"flags\":[\"default\"]}");

        record = complaints.get(1);
        assertThat(record.getHeader().getNumber()).isEqualTo(2);
        assertThat(record.getPayload()).isEqualTo("{\"id\":\"fphp-cr5a\",\"name\":\"Debt collection complaints\",\"averageRating\":0,\"createdAt\":1383061830,\"displayType\":\"table\",\"downloadCount\":23761,\"indexUpdatedAt\":1430489336,\"moderationStatus\":true,\"newBackend\":false,\"numberOfComments\":0,\"oid\":11110911,\"publicationAppendEnabled\":false,\"publicationDate\":1430482892,\"publicationGroup\":713240,\"publicationStage\":\"published\",\"rowsUpdatedAt\":1430481392,\"rowsUpdatedBy\":\"3gpd-kj7i\",\"tableId\":2758736,\"totalTimesRated\":0,\"viewCount\":73673,\"viewLastModified\":1383061850,\"viewType\":\"tabular\",\"grants\":[{\"inherited\":true,\"type\":\"viewer\",\"flags\":[\"public\"]}],\"metadata\":{\"custom_fields\":{\"TEST\":{\"CFPB1\":\"\"}},\"renderTypeConfig\":{\"visible\":{\"table\":true}},\"availableDisplayTypes\":[\"table\",\"fatrow\",\"page\"],\"rdfSubject\":\"0\"},\"owner\":{\"id\":\"2m4m-3z9t\",\"displayName\":\"Micheal Keane\",\"screenName\":\"Micheal Keane\"},\"rights\":[\"read\"],\"tableAuthor\":{\"id\":\"dfzt-mv86\",\"displayName\":\"CFPB Administrator\",\"roleName\":\"publisher\",\"screenName\":\"CFPB Administrator\",\"rights\":[\"create_datasets\",\"edit_others_datasets\",\"edit_nominations\",\"approve_nominations\",\"moderate_comments\",\"manage_stories\",\"feature_items\",\"change_configurations\",\"view_domain\",\"view_others_datasets\",\"create_pages\",\"edit_pages\",\"view_goals\",\"view_dashboards\",\"edit_goals\",\"edit_dashboards\"]}}");

        record = complaints.get(2);
        assertThat(record.getHeader().getNumber()).isEqualTo(3);
        assertThat(record.getPayload()).isEqualTo("{\"id\":\"x94z-ydhh\",\"name\":\"Consumer Complaints\",\"averageRating\":0,\"createdAt\":1362426812,\"displayType\":\"table\",\"downloadCount\":60996,\"indexUpdatedAt\":1430488302,\"newBackend\":false,\"numberOfComments\":0,\"oid\":11110158,\"publicationAppendEnabled\":false,\"publicationDate\":1430482892,\"publicationGroup\":713240,\"publicationStage\":\"published\",\"rowsUpdatedAt\":1430481392,\"rowsUpdatedBy\":\"3gpd-kj7i\",\"tableId\":2758736,\"totalTimesRated\":0,\"viewCount\":245320,\"viewLastModified\":1430482892,\"viewType\":\"tabular\",\"grants\":[{\"inherited\":true,\"type\":\"viewer\",\"flags\":[\"public\"]}],\"metadata\":{\"custom_fields\":{\"TEST\":{\"CFPB1\":\"\"}},\"renderTypeConfig\":{\"visible\":{\"table\":true}},\"availableDisplayTypes\":[\"table\",\"fatrow\",\"page\"],\"jsonQuery\":{\"order\":[{\"ascending\":false,\"columnFieldName\":\"date_received\"}]},\"rdfSubject\":\"0\",\"filterCondition\":{\"value\":\"AND\",\"type\":\"operator\",\"metadata\":{\"unifiedVersion\":2,\"advanced\":false}}},\"owner\":{\"id\":\"dfzt-mv86\",\"displayName\":\"CFPB Administrator\",\"roleName\":\"publisher\",\"screenName\":\"CFPB Administrator\",\"rights\":[\"create_datasets\",\"edit_others_datasets\",\"edit_nominations\",\"approve_nominations\",\"moderate_comments\",\"manage_stories\",\"feature_items\",\"change_configurations\",\"view_domain\",\"view_others_datasets\",\"create_pages\",\"edit_pages\",\"view_goals\",\"view_dashboards\",\"edit_goals\",\"edit_dashboards\"]},\"rights\":[\"read\"],\"tableAuthor\":{\"id\":\"dfzt-mv86\",\"displayName\":\"CFPB Administrator\",\"roleName\":\"publisher\",\"screenName\":\"CFPB Administrator\",\"rights\":[\"create_datasets\",\"edit_others_datasets\",\"edit_nominations\",\"approve_nominations\",\"moderate_comments\",\"manage_stories\",\"feature_items\",\"change_configurations\",\"view_domain\",\"view_others_datasets\",\"create_pages\",\"edit_pages\",\"view_goals\",\"view_dashboards\",\"edit_goals\",\"edit_dashboards\"]},\"flags\":[\"default\"]}");

    }

    @Test
    public void testEmptyDataSourceProcessing() throws Exception {
        final InputStream jsonDataSource = getDataSource("/empty.json");

        Job job = JobBuilder.aNewJob()
                .reader(new JsonRecordReader(jsonDataSource))
                .processor(new RecordCollector())
                .build();

        JobReport jobReport = JobExecutor.execute(job);

        assertThat(jobReport).isNotNull();
        assertThat(jobReport.getMetrics().getTotalCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(0);
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.COMPLETED);
        assertThat(jobReport.getParameters().getDataSource()).isEqualTo(EXPECTED_DATA_SOURCE_NAME);

        List<JsonRecord> records = (List<JsonRecord>) jobReport.getResult();
        assertThat(records).isNotNull().isEmpty();

    }

    private InputStream getDataSource(String name) {
        return this.getClass().getResourceAsStream(name);
    }

    private void assertThatReportIsValid(JobReport jobReport) {
        assertThat(jobReport).isNotNull();
        assertThat(jobReport.getMetrics().getTotalCount()).isEqualTo(3);
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(3);
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.COMPLETED);
        assertThat(jobReport.getParameters().getDataSource()).isEqualTo(EXPECTED_DATA_SOURCE_NAME);
    }

}

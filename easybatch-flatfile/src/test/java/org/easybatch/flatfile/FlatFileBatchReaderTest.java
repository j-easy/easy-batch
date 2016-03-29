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

package org.easybatch.flatfile;

import org.easybatch.core.job.JobReport;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.job.JobBuilder.aNewJob;

@SuppressWarnings("unchecked")
public class FlatFileBatchReaderTest {

    public static final int BATCH_SIZE = 4;

    private FlatFileBatchReader flatFileBatchReader;

    @Before
    public void setUp() throws Exception {
        flatFileBatchReader = new FlatFileBatchReader(new File(getFileUri("/complaints.csv")), BATCH_SIZE);
        flatFileBatchReader.open();
    }

    @Test
    public void testFlatFileBatchReading() throws Exception {
        assertThat(flatFileBatchReader.hasNextRecord()).isTrue();
        Batch batch = flatFileBatchReader.readNextRecord();
        List<Record> batch1 = batch.getPayload();

        assertThat(batch1).extracting("payload").containsExactly(
            "Complaint ID,Product,Sub-product,Issue,Sub-issue,State,ZIP code,Submitted via,Date received,Date sent to company,Company,Company response,Timely response?,Consumer disputed?",
            "1355160,Student loan,Non-federal student loan,Dealing with my lender or servicer,,NJ,08807,Web,04/30/2015,04/30/2015,Transworld Systems Inc.,In progress,Yes,",
            "1354249,Bank account or service,Checking account,Problems caused by my funds being low,,AL,35127,Web,04/30/2015,04/30/2015,Wells Fargo,Closed with explanation,Yes,",
            "1354227,Debt collection,Medical,False statements or representation,Indicated committed crime not paying,FL,32792,Web,04/29/2015,04/30/2015,Transworld Systems Inc.,In progress,Yes,");

        assertThat(flatFileBatchReader.hasNextRecord()).isTrue();
        batch = flatFileBatchReader.readNextRecord();
        List<Record> batch2 = batch.getPayload();

        assertThat(batch2).extracting("payload").containsExactly(
            "1354200,Debt collection,Credit card,False statements or representation,Indicated committed crime not paying,AZ,85304,Web,04/29/2015,04/30/2015,Patenaude & Felix APC,Closed with explanation,Yes,",
            "1354191,Bank account or service,Checking account,Problems caused by my funds being low,,CA,90044,Web,04/29/2015,04/30/2015,Wells Fargo,Closed with explanation,Yes,",
            "1353502,Consumer loan,Vehicle loan,Problems when you are unable to pay,,TX,75287,Web,04/29/2015,04/29/2015,Ally Financial Inc.,In progress,Yes,",
            "1353247,Money transfers,Domestic (US) money transfer,Fraud or scam,,AR,72712,Web,04/29/2015,04/29/2015,MoneyGram,In progress,Yes,");

        assertThat(flatFileBatchReader.hasNextRecord()).isTrue();
        batch = flatFileBatchReader.readNextRecord();
        List<Record> batch3 = batch.getPayload();

        assertThat(batch3).extracting("payload").containsExactly(
            "1351451,Consumer loan,Installment loan,Taking out the loan or lease,,GA,30346,Web,04/28/2015,04/30/2015,Springleaf Finance Corporation,In progress,Yes,",
            "1353946,Debt collection,Medical,Disclosure verification of debt,Not given enough info to verify debt,NJ,07740,Web,04/28/2015,04/29/2015,Senex Services Corp.,Closed with explanation,Yes,",
            "1351334,Money transfers,International money transfer,Money was not available when promised,,TX,78666,Phone,04/28/2015,04/29/2015,MoneyGram,In progress,Yes,");

        assertThat(flatFileBatchReader.hasNextRecord()).isFalse();
    }

    @Test
    public void batchProcessingIntegrationTest() throws Exception {

        JobReport jobReport = aNewJob()
                .reader(new FlatFileBatchReader(new File(getFileUri("/complaints.csv")), BATCH_SIZE))
                .processor(new RecordCollector())
                .call();

        List<Batch> batches = (List<Batch>) jobReport.getResult();

        assertThat(batches).hasSize(3);

        Batch batch1 = batches.get(0);
        assertThat(batch1).isNotNull();
        assertThat(batch1.getHeader().getNumber()).isEqualTo(1);
        assertThat(batch1.getPayload()).hasSize(4);

        Batch batch2 = batches.get(1);
        assertThat(batch2).isNotNull();
        assertThat(batch2.getHeader().getNumber()).isEqualTo(2);
        assertThat(batch2.getPayload()).hasSize(4);

        Batch batch3 = batches.get(2);
        assertThat(batch3).isNotNull();
        assertThat(batch3.getHeader().getNumber()).isEqualTo(3);
        assertThat(batch3.getPayload()).hasSize(3);
    }

    @After
    public void tearDown() throws Exception {
        flatFileBatchReader.close();
    }

    private URI getFileUri(String fileName) throws URISyntaxException {
        return this.getClass().getResource(fileName).toURI();
    }
}

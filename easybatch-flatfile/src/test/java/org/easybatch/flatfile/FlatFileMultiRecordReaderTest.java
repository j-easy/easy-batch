/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

import org.easybatch.core.api.JobReport;
import org.easybatch.core.api.Record;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.record.MultiRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.impl.JobBuilder.aNewJob;

@SuppressWarnings("unchecked")
public class FlatFileMultiRecordReaderTest {

    public static final int CHUNK_SIZE = 4;

    private FlatFileMultiRecordReader flatFileMultiRecordReader;

    @Before
    public void setUp() throws Exception {
        flatFileMultiRecordReader = new FlatFileMultiRecordReader(new File(getFileUri("/complaints.csv")), CHUNK_SIZE);
        flatFileMultiRecordReader.open();
    }

    @Test
    public void testFlatFileMultiRecordReading() throws Exception {
        assertThat(flatFileMultiRecordReader.hasNextRecord()).isTrue();
        MultiRecord multiRecord = flatFileMultiRecordReader.readNextRecord();
        List<Record> chunk1 = multiRecord.getPayload();

        assertThat(chunk1).isNotNull().isNotEmpty().hasSize(4);
        assertThat(chunk1.get(0).getPayload()).isEqualTo("Complaint ID,Product,Sub-product,Issue,Sub-issue,State,ZIP code,Submitted via,Date received,Date sent to company,Company,Company response,Timely response?,Consumer disputed?");
        assertThat(chunk1.get(1).getPayload()).isEqualTo("1355160,Student loan,Non-federal student loan,Dealing with my lender or servicer,,NJ,08807,Web,04/30/2015,04/30/2015,Transworld Systems Inc.,In progress,Yes,");
        assertThat(chunk1.get(2).getPayload()).isEqualTo("1354249,Bank account or service,Checking account,Problems caused by my funds being low,,AL,35127,Web,04/30/2015,04/30/2015,Wells Fargo,Closed with explanation,Yes,");
        assertThat(chunk1.get(3).getPayload()).isEqualTo("1354227,Debt collection,Medical,False statements or representation,Indicated committed crime not paying,FL,32792,Web,04/29/2015,04/30/2015,Transworld Systems Inc.,In progress,Yes,");

        assertThat(flatFileMultiRecordReader.hasNextRecord()).isTrue();
        multiRecord = flatFileMultiRecordReader.readNextRecord();
        List<Record> chunk2 = multiRecord.getPayload();

        assertThat(chunk2).isNotNull().isNotEmpty().hasSize(4);
        assertThat(chunk2.get(0).getPayload()).isEqualTo("1354200,Debt collection,Credit card,False statements or representation,Indicated committed crime not paying,AZ,85304,Web,04/29/2015,04/30/2015,Patenaude & Felix APC,Closed with explanation,Yes,");
        assertThat(chunk2.get(1).getPayload()).isEqualTo("1354191,Bank account or service,Checking account,Problems caused by my funds being low,,CA,90044,Web,04/29/2015,04/30/2015,Wells Fargo,Closed with explanation,Yes,");
        assertThat(chunk2.get(2).getPayload()).isEqualTo("1353502,Consumer loan,Vehicle loan,Problems when you are unable to pay,,TX,75287,Web,04/29/2015,04/29/2015,Ally Financial Inc.,In progress,Yes,");
        assertThat(chunk2.get(3).getPayload()).isEqualTo("1353247,Money transfers,Domestic (US) money transfer,Fraud or scam,,AR,72712,Web,04/29/2015,04/29/2015,MoneyGram,In progress,Yes,");

        assertThat(flatFileMultiRecordReader.hasNextRecord()).isTrue();
        multiRecord = flatFileMultiRecordReader.readNextRecord();
        List<Record> chunk3 = multiRecord.getPayload();

        assertThat(chunk3).isNotNull().isNotEmpty().hasSize(3);
        assertThat(chunk3.get(0).getPayload()).isEqualTo("1351451,Consumer loan,Installment loan,Taking out the loan or lease,,GA,30346,Web,04/28/2015,04/30/2015,Springleaf Finance Corporation,In progress,Yes,");
        assertThat(chunk3.get(1).getPayload()).isEqualTo("1353946,Debt collection,Medical,Disclosure verification of debt,Not given enough info to verify debt,NJ,07740,Web,04/28/2015,04/29/2015,Senex Services Corp.,Closed with explanation,Yes,");
        assertThat(chunk3.get(2).getPayload()).isEqualTo("1351334,Money transfers,International money transfer,Money was not available when promised,,TX,78666,Phone,04/28/2015,04/29/2015,MoneyGram,In progress,Yes,");

        assertThat(flatFileMultiRecordReader.hasNextRecord()).isFalse();
    }

    @Test
    public void chunkProcessingIntegrationTest() throws Exception {

        JobReport jobReport = aNewJob()
                .reader(new FlatFileMultiRecordReader(new File(getFileUri("/complaints.csv")), CHUNK_SIZE))
                .processor(new RecordCollector<MultiRecord>())
                .call();

        List<MultiRecord> multiRecords = (List<MultiRecord>) jobReport.getResult();

        assertThat(multiRecords).isNotNull().isNotEmpty().hasSize(3);

        MultiRecord chunk1 = multiRecords.get(0);
        assertThat(chunk1).isNotNull();
        assertThat(chunk1.getHeader().getNumber()).isEqualTo(1);
        assertThat(chunk1.getPayload()).isNotEmpty().hasSize(4);

        MultiRecord chunk2 = multiRecords.get(1);
        assertThat(chunk2).isNotNull();
        assertThat(chunk2.getHeader().getNumber()).isEqualTo(2);
        assertThat(chunk2.getPayload()).isNotEmpty().hasSize(4);

        MultiRecord chunk3 = multiRecords.get(2);
        assertThat(chunk3).isNotNull();
        assertThat(chunk3.getHeader().getNumber()).isEqualTo(3);
        assertThat(chunk3.getPayload()).isNotEmpty().hasSize(3);

    }

    @After
    public void tearDown() throws Exception {
        flatFileMultiRecordReader.close();
    }

    private URI getFileUri(String fileName) throws URISyntaxException {
        return this.getClass().getResource(fileName).toURI();
    }
}

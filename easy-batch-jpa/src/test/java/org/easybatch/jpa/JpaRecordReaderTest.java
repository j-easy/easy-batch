/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.easybatch.jpa;

import org.easybatch.core.record.Record;
import org.easybatch.test.common.AbstractDatabaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.assertj.core.api.Assertions.assertThat;

public class JpaRecordReaderTest extends AbstractDatabaseTest {

    private static final int FETCH_SIZE = 10;

    private JpaRecordReader<Tweet> jpaRecordReader;

    @Before
    public void setUp() throws Exception {
        addScript("data.sql");
        super.setUp();
        String query = "from Tweet";
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("tweet");
        jpaRecordReader = new JpaRecordReader<>(entityManagerFactory, query, Tweet.class);
        jpaRecordReader.setFetchSize(FETCH_SIZE);
        jpaRecordReader.open();
    }

    @Test
    public void testReadRecord() throws Exception {
        Record<Tweet> record = jpaRecordReader.readRecord();
        long recordNumber = record.getHeader().getNumber();
        Tweet tweet = record.getPayload();

        assertThat(recordNumber).isEqualTo(1);
        assertThat(tweet).isEqualTo(new Tweet(1, "foo", "easy batch rocks! #EasyBatch"));
    }

    @Test
    public void testPaging() {
        int nbRecords = 0;
        while (jpaRecordReader.readRecord() != null) {
            nbRecords++;
        }
        assertThat(nbRecords).isEqualTo(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fetchSizeParameterMustBeAtLeastEqualToOne() throws Exception {
        jpaRecordReader.setFetchSize(0);
    }

    @After
    public void tearDown() throws Exception {
        jpaRecordReader.close();
        super.tearDown();
    }

}

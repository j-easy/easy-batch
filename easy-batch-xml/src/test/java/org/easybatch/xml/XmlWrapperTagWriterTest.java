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
package org.easybatch.xml;

import org.easybatch.core.job.JobParameters;
import org.easybatch.core.job.JobReport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

@RunWith(MockitoJUnitRunner.class)
public class XmlWrapperTagWriterTest {

    private static final String DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";

    private File file;

    @Mock
    private JobReport jobReport;
    @Mock
    private JobParameters jobParameters;

    private String wrapperTag;

    private XmlWrapperTagWriter xmlWrapperTagWriter;

    @Before
    public void setUp() throws Exception {
        wrapperTag = "tweets";
        file = new File("target/tweets.xml");
        xmlWrapperTagWriter = new XmlWrapperTagWriter(file, wrapperTag);
    }

    @Test
    public void testBeforeJobStart() throws Exception {
        xmlWrapperTagWriter.beforeJobStart(jobParameters);
        assertThat(file).hasContent(DECLARATION + LINE_SEPARATOR + "<" + wrapperTag + ">" + LINE_SEPARATOR);
    }

    @Test
    public void testAfterJobEnd() throws Exception {
        xmlWrapperTagWriter.afterJobEnd(jobReport);
        assertThat(file).hasContent("</" + wrapperTag + ">");
    }

    @Test
    public void integrationTest() throws Exception {
        xmlWrapperTagWriter.beforeJobStart(jobParameters);
        xmlWrapperTagWriter.afterJobEnd(jobReport);

        assertThat(file).hasContent(DECLARATION + LINE_SEPARATOR + "<" + wrapperTag + ">" + LINE_SEPARATOR + "</" + wrapperTag + ">");
    }

    @After
    public void tearDown() throws Exception {
        file.delete();
    }
}

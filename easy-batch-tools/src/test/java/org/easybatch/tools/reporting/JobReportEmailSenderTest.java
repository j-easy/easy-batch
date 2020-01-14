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
package org.easybatch.tools.reporting;

import org.easybatch.core.job.JobReport;
import org.easybatch.core.job.JobReportFormatter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.mail.Message;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobReportEmailSenderTest {

    @Mock
    private JobReport jobReport;
    @Mock
    private JobReportEmailSender.EmailSender emailSender;
    @Mock
    private JobReportFormatter<String> jobReportFormatter;

    private JobReportEmailSender jobReportEmailSender;

    @Before
    public void setUp() throws Exception {
        Properties properties = new Properties();
        properties.setProperty(JobReportEmailSender.USER, "foo");
        properties.setProperty(JobReportEmailSender.PASSWORD, "bar");
        properties.setProperty(JobReportEmailSender.SENDER, "foo");
        properties.setProperty(JobReportEmailSender.RECIPIENT, "foo@yopmail.com");
        properties.setProperty(JobReportEmailSender.SUBJECT, "test");
        jobReportEmailSender = new JobReportEmailSender(properties, emailSender);
        jobReportEmailSender.setJobReportFormatter(jobReportFormatter);
    }

    @Test
    public void afterJobEnd() throws Exception {
        when(jobReportFormatter.formatReport(jobReport)).thenReturn("formattedJobReport");
        jobReportEmailSender.afterJobEnd(jobReport);
        verify(jobReportFormatter).formatReport(jobReport);
        verify(emailSender).send(any(Message.class));
    }

    @Ignore("This test works with a real Gmail account. Gmail settings should allow 'less secured apps' to send emails")
    @Test
    public void emailSendingTest() throws Exception {
        // FIXME use http://www.icegreen.com/greenmail/ for this test
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put(JobReportEmailSender.USER, "");// fill in user
        props.put(JobReportEmailSender.PASSWORD, "");// fill in password
        props.put(JobReportEmailSender.SENDER, "");// fill in sender
        props.put(JobReportEmailSender.RECIPIENT, "");// fill in recipient
        props.put(JobReportEmailSender.SUBJECT, ""); // fill in subject

        JobReportEmailSender jobReportEmailSender = new JobReportEmailSender(props);
        jobReportEmailSender.afterJobEnd(new JobReport () {
            @Override
            public String toString() {
                return "it works!";
            }
        });
    }
}

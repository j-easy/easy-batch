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

import org.easybatch.core.job.DefaultJobReportFormatter;
import org.easybatch.core.job.JobParameters;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.job.JobReportFormatter;
import org.easybatch.core.listener.JobListener;
import org.easybatch.core.util.Utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Job listener that sends the job report by email to a given account.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 * @deprecated This class is deprecated since v5.3 and will be removed in v6.
 */
@Deprecated
public class JobReportEmailSender implements JobListener {

    public static final String USER = "org.easybatch.tools.reporting.email.user";
    public static final String PASSWORD = "org.easybatch.tools.reporting.email.password";
    public static final String SENDER = "org.easybatch.tools.reporting.email.sender";
    public static final String RECIPIENT = "org.easybatch.tools.reporting.email.recipient";
    public static final String SUBJECT = "org.easybatch.tools.reporting.email.subject";

    private Properties properties;
    private EmailSender messageSender;
    private JobReportFormatter<String> jobReportFormatter;

    /**
     * Create a new {@link JobReportEmailSender}.
     *
     * @param properties configuration properties. This should contain all email server configuration properties plus ( {@link JobReportEmailSender#USER}, {@link JobReportEmailSender#PASSWORD},
     * {@link JobReportEmailSender#SENDER}, {@link JobReportEmailSender#RECIPIENT} and {@link JobReportEmailSender#SUBJECT} )
     */
    public JobReportEmailSender(final Properties properties) {
        validate(properties);
        this.properties = properties;
        this.messageSender = new EmailSender();
        this.jobReportFormatter = new DefaultJobReportFormatter();
    }

    /**
     * Set a custom {@link JobReportFormatter}.
     *
     * @param jobReportFormatter used to format the report before sending the email
     */
    public void setJobReportFormatter(JobReportFormatter<String> jobReportFormatter) {
        Utils.checkNotNull(jobReportFormatter, "job report formatter");
        this.jobReportFormatter = jobReportFormatter;
    }

    JobReportEmailSender(final Properties properties, final EmailSender emailSender) {
        validate(properties);
        this.properties = properties;
        this.messageSender = emailSender;
    }

    @Override
    public void beforeJobStart(JobParameters jobParameters) {
        // no op
    }

    @Override
    public void afterJobEnd(JobReport jobReport) {
        String formattedJobReport = format(jobReport);
        send(formattedJobReport);
    }

    private String format(JobReport jobReport) {
        return jobReportFormatter.formatReport(jobReport);
    }

    private void send(String formattedJobReport) {
        Session session = Session.getInstance(properties, getAuthenticator());
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(properties.getProperty(SENDER)));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(properties.getProperty(RECIPIENT)));
            message.setSubject(properties.getProperty(SUBJECT));
            message.setText(formattedJobReport);
            messageSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send job report by email", e);
        }
    }

    private Authenticator getAuthenticator() {
        final String username = properties.getProperty(USER);
        final String password = properties.getProperty(PASSWORD);
        return new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
    }

    private void validate(Properties properties) {
        check(properties, USER);
        check(properties, PASSWORD);
        check(properties, SENDER);
        check(properties, RECIPIENT);
        check(properties, SUBJECT);
    }

    private void check(Properties properties, String property) {
        if (properties.getProperty(property) == null) {
            throw new IllegalArgumentException(String.format("Property %s is mandatory", property));
        }
    }

    class EmailSender {
        void send(Message message) throws MessagingException {
            Transport.send(message);
        }
    }

}

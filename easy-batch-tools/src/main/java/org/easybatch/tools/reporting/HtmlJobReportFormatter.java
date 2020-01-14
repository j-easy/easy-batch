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

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.log.NullLogChute;
import org.easybatch.core.job.JobMetrics;
import org.easybatch.core.job.JobParameters;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.job.JobReportFormatter;

import java.io.StringWriter;
import java.util.Properties;
import java.util.TreeMap;

import static org.easybatch.core.util.Utils.formatDuration;
import static org.easybatch.core.util.Utils.formatErrorThreshold;
import static org.easybatch.core.util.Utils.formatTime;

/**
 * Format a report into HTML format.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 * @deprecated This class is deprecated since v5.3 and will be removed in v6.
 */
@Deprecated
public class HtmlJobReportFormatter implements JobReportFormatter<String> {

    private VelocityEngine velocityEngine;

    /**
     * Create a new {@link HtmlJobReportFormatter}.
     */
    public HtmlJobReportFormatter() {
        Properties properties = new Properties();
        properties.put("resource.loader", "class");
        properties.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.put(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, NullLogChute.class.getName());
        velocityEngine = new VelocityEngine(properties);
        velocityEngine.init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String formatReport(final JobReport jobReport) {
        Template template = velocityEngine.getTemplate("/org/easybatch/tools/reporting/HtmlReport.vm");
        StringWriter stringWriter = new StringWriter();
        Context context = new VelocityContext();
        context.put("report", jobReport);

        /*
         * Job name and status
         */
        context.put("name",jobReport.getJobName());
        context.put("status",jobReport.getStatus());

        /*
         * Job parameters
         */
        JobParameters parameters = jobReport.getParameters();
        context.put("batchSize",parameters.getBatchSize());
        context.put("errorThreshold", formatErrorThreshold(parameters.getErrorThreshold()));
        context.put("jmx",parameters.isJmxMonitoring());

        /*
         * Job metrics
         */
        JobMetrics metrics = jobReport.getMetrics();
        context.put("startTime",formatTime(metrics.getStartTime()));
        context.put("endTime",formatTime(metrics.getEndTime()));
        context.put("duration",formatDuration(metrics.getDuration()));
        context.put("readCount",metrics.getReadCount());
        context.put("writeCount",metrics.getWriteCount());
        context.put("filterCount",metrics.getFilterCount());
        context.put("errorCount",metrics.getErrorCount());
        context.put("customMetrics",metrics.getCustomMetrics());

        /*
         * System properties
         */
        context.put("systemProperties", new TreeMap<>(jobReport.getSystemProperties()));
        // properties are wrapped in a tree map for sorting purpose (repeatable tests)

        template.merge(context, stringWriter);
        return stringWriter.toString();
    }

}

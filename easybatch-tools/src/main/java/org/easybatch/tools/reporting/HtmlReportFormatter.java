package org.easybatch.tools.reporting;

import org.easybatch.core.api.Report;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import java.io.StringWriter;
import java.util.Properties;

/**
 * Format a report into HTML format.
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
public class HtmlReportFormatter implements ReportFormatter<String> {

    /**
     * The template engine to render reports.
     */
    private VelocityEngine velocityEngine;

    public HtmlReportFormatter() {
        Properties properties = new Properties();
        properties.put("resource.loader", "class");
        properties.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine = new VelocityEngine(properties);
        velocityEngine.init();
    }

    /**
     * {@inheritDoc}
     */
    public String formatReport(final Report report) {
        Template template = velocityEngine.getTemplate("/org/easybatch/tools/reporting/HtmlReport.vm");
        StringWriter stringWriter = new StringWriter();
        Context context = new VelocityContext();
        context.put("report", report);
        template.merge(context, stringWriter);
        return stringWriter.toString();
    }

}

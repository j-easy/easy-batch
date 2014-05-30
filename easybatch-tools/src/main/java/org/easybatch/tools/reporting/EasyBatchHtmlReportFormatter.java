package org.easybatch.tools.reporting;

import org.easybatch.core.api.EasyBatchReport;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import java.io.StringWriter;
import java.util.Properties;

/**
 * Format an easy batch report into HTML format.
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
public class EasyBatchHtmlReportFormatter implements EasyBatchReportFormatter<String> {

    /**
     * The template engine to render reports.
     */
    private VelocityEngine velocityEngine;

    public EasyBatchHtmlReportFormatter() {
        Properties properties = new Properties();
        properties.put("resource.loader", "class");
        properties.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine = new VelocityEngine(properties);
        velocityEngine.init();
    }

    /**
     * {@inheritDoc}
     */
    public String formatEasyBatchReport(final EasyBatchReport easyBatchReport) {
        Template template = velocityEngine.getTemplate("/org/easybatch/tools/reporting/easyBatchHtmlReport.vm");
        StringWriter stringWriter = new StringWriter();
        Context context = new VelocityContext();
        context.put("report", easyBatchReport);
        template.merge(context, stringWriter);
        return stringWriter.toString();
    }

}

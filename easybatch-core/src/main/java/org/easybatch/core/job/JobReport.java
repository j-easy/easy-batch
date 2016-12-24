/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.core.job;

import java.io.Serializable;

/**
 * Class holding job reporting data.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JobReport implements Serializable {

    private String jobName;

    private JobParameters parameters;

    private JobMetrics metrics;

    private JobStatus status;

    private Throwable lastError;
    
    private JobReportFormatter<String> formatter;

    public String getJobName() {
        return jobName;
    }

    public JobParameters getParameters() {
        return parameters;
    }

    public JobMetrics getMetrics() {
        return metrics;
    }

    public JobStatus getStatus() {
        return status;
    }

    public Throwable getLastError() {
        return lastError;
    }
    
    public JobReportFormatter<String> getFormatter(){
    	if(this.formatter == null){
    		synchronized (formatter) {
    			if(this.formatter == null){
    				this.formatter = new DefaultJobReportFormatter();
    			}
			}
    	}
    	return this.formatter;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setParameters(JobParameters parameters) {
        this.parameters = parameters;
    }

    public void setMetrics(JobMetrics metrics) {
        this.metrics = metrics;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public void setLastError(Throwable lastError) {
        this.lastError = lastError;
    }
    
    public void setFormatter(JobReportFormatter<String> formatter){
    	this.formatter = formatter;
    }

    @Override
    public String toString() {
        return getFormatter().formatReport(this);
    }
}

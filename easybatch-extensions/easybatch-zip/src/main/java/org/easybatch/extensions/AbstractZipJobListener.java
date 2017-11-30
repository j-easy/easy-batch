/**
 * 
 */
package org.easybatch.extensions;

import org.easybatch.core.job.JobParameters;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.listener.JobListener;

/**
 * 
 * @author Somma Daniele (C307838)
 */
abstract class AbstractZipJobListener implements JobListener {

  @Override
  public final void beforeJobStart(JobParameters jobParameters) {
    decompress();
  }

  @Override
  public final void afterJobEnd(JobReport jobReport) {
    compress();
  }

  public abstract void compress();

  public abstract void decompress();

}
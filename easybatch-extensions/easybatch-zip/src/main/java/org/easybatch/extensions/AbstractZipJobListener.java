package org.easybatch.extensions;

import org.easybatch.core.job.JobParameters;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.listener.JobListener;

/**
 * The base element, of {@link JobListener} family, to implement new
 * compress/decompress components. The method
 * {@link #beforeJobStart(JobParameters)} wrap the {@link #compress()} phase;
 * method {@link #afterJobEnd(JobReport)} wrap the {@link #decompress()} phase.
 *
 * @author Somma Daniele
 */
public abstract class AbstractZipJobListener implements JobListener {

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
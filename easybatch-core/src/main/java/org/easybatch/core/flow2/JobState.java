package org.easybatch.core.flow2;

import org.easybatch.core.job.JobStatus;

/**
 * Created by Sunand on 5/24/2016.
 */
public enum JobState {
  SUCCESS(1, JobStatus.COMPLETED),
  FAILURE(-1, JobStatus.FAILED),
  COMPLETED(0, JobStatus.COMPLETED);

  private int cost;
  private JobStatus status;

  JobState(int cost, JobStatus status) {
    this.cost = cost;
    this.status = status;
  }

  public int getCost() {
    return cost;
  }

  public JobStatus getJobStatus() {
    return status;
  }
}

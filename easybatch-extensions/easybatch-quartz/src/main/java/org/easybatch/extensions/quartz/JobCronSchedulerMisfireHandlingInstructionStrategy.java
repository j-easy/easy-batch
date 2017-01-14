package org.easybatch.extensions.quartz;

public enum JobCronSchedulerMisfireHandlingInstructionStrategy {
    IGNORE_MISFIRES,
    DO_NOTHING,
    FIRE_AND_PROCEED
}

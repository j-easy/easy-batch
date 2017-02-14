package org.easybatch.extensions.quartz;

/**
 * Enumeration of strategies to apply when a quartz cron scheduler misfires a job.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public enum MisfireCronHandlingStrategy {
    IGNORE_MISFIRES,
    DO_NOTHING,
    FIRE_AND_PROCEED
}

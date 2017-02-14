package org.easybatch.extensions.quartz;

/**
 * Enumeration of strategies to apply when a quartz scheduler misfires a job.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public enum MisfireHandlingStrategy {
    FIRE_NOW,
    IGNORE_MISFIRES,
    NEXT_WITH_EXISTING_COUNT,
    NEXT_WITH_REMAINING_COUNT,
    NOW_WITH_EXISTING_COUNT,
    NOW_WITH_REMAINING_COUNT
}

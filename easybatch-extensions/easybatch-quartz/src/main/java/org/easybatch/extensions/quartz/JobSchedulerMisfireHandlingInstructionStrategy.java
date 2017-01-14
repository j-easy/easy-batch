package org.easybatch.extensions.quartz;

public enum JobSchedulerMisfireHandlingInstructionStrategy {
    FIRE_NOW,
    IGNORE_MISFIRES,
    NEXT_WITH_EXISTING_COUNT,
    NEXT_WITH_REMAINING_COUNT,
    NOW_WITH_EXISTING_COUNT,
    NOW_WITH_REMAINING_COUNT
}

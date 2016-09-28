package org.easybatch.core.job;

class RecordTracker {

    private boolean moreRecords = true;

    void noMoreRecords() {
        moreRecords = false;
    }

    boolean moreRecords() {
        return moreRecords;
    }
}

package org.easybatch.core.job;

class ErrorThresholdExceededException extends Exception {

    ErrorThresholdExceededException(String s, Exception e) {
        super(s, e);
    }
}

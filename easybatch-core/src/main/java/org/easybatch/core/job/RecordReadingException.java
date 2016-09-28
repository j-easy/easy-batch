package org.easybatch.core.job;

class RecordReadingException extends Exception {

    RecordReadingException(String s, Exception e) {
        super(s, e);
    }
}

package org.easybatch.core.job;

class RecordWriterOpeningException extends Exception {

    RecordWriterOpeningException(String s, Exception e) {
        super(s, e);
    }
}

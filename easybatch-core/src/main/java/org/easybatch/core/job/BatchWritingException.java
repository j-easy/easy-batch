package org.easybatch.core.job;

class BatchWritingException extends Exception {

    BatchWritingException(String s, Exception e) {
        super(s, e);
    }

}

package io.github.benas.easybatch.tutorials.parallel;

import io.github.benas.easybatch.core.api.AbstractRecordProcessor;
import io.github.benas.easybatch.core.util.StringRecord;

/**
 * A record processor that encrypts record raw text.
 * (to keep it simple, simply reverse the clear text)
 */
public class MessageEncrypter extends AbstractRecordProcessor<StringRecord> {

    @Override
    public void processRecord(final StringRecord record) throws Exception {
        Thread.sleep(1000);//simulating a long encryption algorithm
        System.out.println(new StringBuilder(record.getRawContent()).reverse().toString());
    }

}

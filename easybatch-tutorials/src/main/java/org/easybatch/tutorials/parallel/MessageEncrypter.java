package org.easybatch.tutorials.parallel;

import org.easybatch.core.api.AbstractRecordProcessor;
import org.easybatch.core.util.StringRecord;

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

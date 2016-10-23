package ${packageName};

import org.easybatch.core.processor.RecordProcessor;
import org.easybatch.core.record.StringRecord;

/**
 * A processor that transforms tweets to upper case.
 */
public class TweetProcessor implements RecordProcessor<StringRecord, StringRecord> {

    @Override
    public StringRecord processRecord(StringRecord record) throws Exception {
        return new StringRecord(record.getHeader(), record.getPayload().toUpperCase());
    }

}

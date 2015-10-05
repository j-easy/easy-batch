package ${packageName};

import org.easybatch.core.api.RecordProcessor;
import org.easybatch.core.api.RecordProcessingException;
import org.easybatch.core.record.StringRecord;

/**
 * A processor that prints out tweets to the console.
 */
public class TweetProcessor implements RecordProcessor<StringRecord, StringRecord> {

    @Override
    public StringRecord processRecord(StringRecord record) throws RecordProcessingException {
        System.out.println(record.getPayload());
        return record;
    }

}

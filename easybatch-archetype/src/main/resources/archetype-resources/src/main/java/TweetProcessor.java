package ${packageName};

import org.easybatch.core.processor.RecordProcessingException;
import org.easybatch.core.processor.RecordProcessor;
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

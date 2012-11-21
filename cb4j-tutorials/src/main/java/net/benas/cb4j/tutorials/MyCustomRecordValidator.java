package net.benas.cb4j.tutorials;

import net.benas.cb4j.core.api.FieldValidator;
import net.benas.cb4j.core.impl.DefaultRecordValidatorImpl;
import net.benas.cb4j.core.model.Record;

import java.util.List;
import java.util.Map;

/**
 * A custom validator to implement validation rule that involve multiple fields at the same time
 * @author benas (md.benhassine@gmail.com)
 */
public class MyCustomRecordValidator extends DefaultRecordValidatorImpl {

    public MyCustomRecordValidator(Map<Integer, List<FieldValidator>> fieldValidators) {
        super(fieldValidators);
    }

    @Override
    public String validateRecord(Record record) {

        String error = super.validateRecord(record);
        if (error == null){//no errors after applying declared validators on each field => all fields are valid

            //add custom validation : field 2 content must start with field's 1 content
            final String content1 = record.getFieldContentByIndex(1);
            final String content2 = record.getFieldContentByIndex(2);
            if (!content2.startsWith(content1))
                return "field 2 content [" + content2 + "] must start with field's 1 content [" + content1 + "]";
        }
        return null;
    }
}

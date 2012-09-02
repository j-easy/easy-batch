package net.benas.cb4j.tutorials.orders;

import net.benas.cb4j.core.api.FieldValidator;
import net.benas.cb4j.core.impl.RecordValidatorImpl;
import net.benas.cb4j.core.model.Record;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Mahmoud
 * Date: 14/08/12
 * Time: 01:02
 * To change this template use File | Settings | File Templates.
 */
public class MyCustomRecordValidator extends RecordValidatorImpl {

    public MyCustomRecordValidator(Map<Integer, List<FieldValidator>> fieldValidators) {
        super(fieldValidators);
    }

    @Override
    public String validateRecord(Record record) {
        String error = super.validateRecord(record);
        if (error.isEmpty()){//no errors after applying declared validators on each field => all fields are valid
            //add custom validation here, for example : field 2 content must starts with field 1 content
            final String content1 = record.getFieldByIndex(1).getContent();
            final String content2 = record.getFieldByIndex(2).getContent();
            if (!content2.startsWith(content1))
                return "field 2 content must be a multiple of field 1 content";
        }
        return "";
    }
}

package net.benas.cb4j.tutorials.orders;

import net.benas.cb4j.core.api.RecordMapper;
import net.benas.cb4j.core.api.RecordMappingException;
import net.benas.cb4j.core.model.Record;

/**
 * Created with IntelliJ IDEA.
 * User: Mahmoud
 * Date: 14/08/12
 * Time: 07:28
 * To change this template use File | Settings | File Templates.
 */
public class ReceiptMapper implements RecordMapper<Receipt> {

    public Receipt mapRecord(Record record) throws RecordMappingException {

        Receipt receipt = new Receipt();

        receipt.setStoreCode(Long.valueOf(record.getFieldByIndex(0).getContent()));
        receipt.setProductCode(record.getFieldByIndex(1).getContent());
        receipt.setUnitPrice(Float.parseFloat(record.getFieldByIndex(2).getContent()));
        receipt.setQuantity(Integer.parseInt(record.getFieldByIndex(3).getContent()));

        return receipt;
    }
}

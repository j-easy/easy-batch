package net.benas.cb4j.tutorials.orders;

import net.benas.cb4j.core.api.RecordProcessor;

public class SalesReceiptsProcessor implements RecordProcessor<Receipt> {

	private int sales = 0;

	public int getSales() {
		return sales;
	}


    @Override
    public void preProcessRecord(Receipt typedRecord) {
    }

    @Override
    public void processRecord(Receipt receipt) {
        sales += receipt.getUnitPrice() * receipt.getQuantity();
    }

    @Override
    public void postProcessRecord(Receipt typedRecord) {
    }
}

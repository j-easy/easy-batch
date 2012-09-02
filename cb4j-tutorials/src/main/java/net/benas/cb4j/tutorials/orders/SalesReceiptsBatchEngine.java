package net.benas.cb4j.tutorials.orders;

import net.benas.cb4j.core.config.BatchConfiguration;
import net.benas.cb4j.core.impl.DefaultBatchEngineImpl;

public class SalesReceiptsBatchEngine extends DefaultBatchEngineImpl {
	
	public SalesReceiptsBatchEngine(BatchConfiguration batchConfiguration) {
		super(batchConfiguration);
	}
	
	@Override
	public void shutdown(){
		super.shutdown();// to generate default report
		System.out.println("Total sales = " + ((SalesReceiptsProcessor)recordProcessor).getSales());
	}

}

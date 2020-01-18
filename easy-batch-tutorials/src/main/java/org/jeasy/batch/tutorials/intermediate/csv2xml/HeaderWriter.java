package org.jeasy.batch.tutorials.intermediate.csv2xml;

import java.io.IOException;
import java.io.OutputStreamWriter;

import org.jeasy.batch.core.util.Utils;
import org.jeasy.batch.core.writer.FileRecordWriter;

public class HeaderWriter implements FileRecordWriter.HeaderCallback {

	@Override
	public void writeHeader(OutputStreamWriter writer) throws IOException {
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
		writer.write(Utils.LINE_SEPARATOR);
		writer.write("<tweets>");
	}

}

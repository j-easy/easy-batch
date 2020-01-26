/*
 * The MIT License
 *
 *  Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.jeasy.batch.tutorials.advanced.scanning;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.jeasy.batch.core.listener.RecordWriterListener;
import org.jeasy.batch.core.record.Batch;
import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.core.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScannedRecordListener implements RecordWriterListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScannedRecordListener.class);

	private FileWriter fileWriter;
	private Path skippedRecords;

	public ScannedRecordListener(Path path) throws IOException {
		fileWriter = new FileWriter(path.toFile());
		skippedRecords = path;
	}

	@Override
	public void beforeRecordWriting(Batch batch) {

	}

	@Override
	public void afterRecordWriting(Batch batch) {

	}

	@Override
	public void onRecordWritingException(Batch batch, Throwable throwable) {
		Record record = null;
		try {
			record = batch.iterator().next();
			if (record.getHeader().isScanned()) {
				fileWriter.write(record.getPayload().toString());
				fileWriter.flush();
				fileWriter.write(Utils.LINE_SEPARATOR);
			}
		} catch (IOException e) {
			String errorMessage = String.format("Unable to write skipped record %s to %s",
					record, skippedRecords.toAbsolutePath());
			LOGGER.error(errorMessage, e);
		}
	}
}

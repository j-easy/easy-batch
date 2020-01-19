/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package org.jeasy.batch.json;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.jeasy.batch.core.marshaller.RecordMarshaller;
import org.jeasy.batch.core.record.Record;

import static org.jeasy.batch.core.util.Utils.checkNotNull;

/**
 * Marshals a POJO to Json using <a href="https://www.jcp.org/en/jsr/detail?id=367">JSON-B API</a>.
 *
 * @param <P> Target domain object class.
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JsonRecordMarshaller<P> implements RecordMarshaller<Record<P>, JsonRecord> {

	private Jsonb jsonb;

	/**
	 * Create a new {@link JsonRecordMarshaller}.
	 */
	public JsonRecordMarshaller() {
		jsonb = JsonbBuilder.create();
	}

	/**
	 * Create a new {@link JsonRecordMarshaller}.
	 *
	 * @param jsonb a pre-configured Jsonb instance
	 */
	public JsonRecordMarshaller(final Jsonb jsonb) {
		checkNotNull(jsonb, "jsonb parameter");
		this.jsonb = jsonb;
	}

	@Override
	public JsonRecord processRecord(final Record<P> record) {
		String payload = jsonb.toJson(record.getPayload());
		return new JsonRecord(record.getHeader(), payload);
	}

}

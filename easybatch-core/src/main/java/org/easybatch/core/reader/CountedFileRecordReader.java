/**
 * The MIT License
 *
 *   Copyright (c) 2017, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.easybatch.core.reader;

import org.easybatch.core.record.Record;
import org.easybatch.core.util.Utils;

/**
 * A {@link RecordReader} that reads data from a file and count the number of
 * rows contained.
 * 
 * The type of file managed depend on {@link AbstractFileRecordReader} provided.
 * 
 * @author Somma Daniele
 */
public class CountedFileRecordReader implements RecordReader {

  protected AbstractFileRecordReader delegate;
  private long                       rowsNumber;

  /**
   * Create a new {@link CountedFileRecordReader}
   * 
   * @param delegate
   *          the record reader of specific file type
   */
  public CountedFileRecordReader(AbstractFileRecordReader delegate) {
    super();
    this.delegate = delegate;
    this.rowsNumber = Utils.countRows(this.delegate.file);
  }

  @Override
  public void open() throws Exception {
    delegate.open();
  }

  @Override
  public Record readRecord() throws Exception {
    return delegate.readRecord();
  }

  @Override
  public void close() throws Exception {
    delegate.close();
  }

  public long getRowsNumber() {
    return rowsNumber;
  }

}
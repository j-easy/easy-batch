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
package org.easybatch.extensions.tar;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.easybatch.extensions.AbstractCompressListenerTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Somma Daniele
 */
public class CompressTarListenerTest extends AbstractCompressListenerTest {

  private File out;

  @Before
  public void setUp() throws IOException {
    out = createTempFile("test", ".tar");
  }

  @After
  public void tearDown() {
    if (out != null) {
      out.delete();
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void decompressUnsupportedOperation() {
    new CompressTarListener(null, null).decompress();
  }

  @Override
  protected void compressCommon(File... in) {
    new CompressTarListener(out, in).compress();

    assertTrue(out.exists());
    assertTrue(out.length() > 0);
  }

}
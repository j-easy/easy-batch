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
package org.easybatch.extensions.zip;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.easybatch.extensions.AbstractZipListenerTest;
import org.junit.After;
import org.junit.Test;

/**
 *
 * @author Somma Daniele
 */
public class CompressZipListenerTest extends AbstractZipListenerTest {

  private File out;

  @After
  public void tearDown() {
    if (out != null) {
      out.delete();
    }
  }

  @Test
  public void compressSingleFileInSubDir() throws IOException {
    out = getTempFile("test", ".zip");
    File[] in = { new File(PATH_BASEFILE, "bar/bar.txt") };

    compressCommon(in);
  }

  @Test
  public void compressSingleFolder() throws IOException {
    out = getTempFile("test", ".zip");
    File[] in = { new File(PATH_BASEFILE, "bar") };

    compressCommon(in);
  }

  @Test
  public void compressSingleEmptyFolder() throws IOException {
    out = getTempFile("test", ".zip");
    File[] in = { new File(PATH_BASEFILE, "foo") };

    compressCommon(in);
  }

  @Test
  public void compressRootFolder() throws IOException {
    out = getTempFile("test", ".zip");
    File[] in = { new File(PATH_BASEFILE) };

    compressCommon(in);
  }

  @Test
  public void compressSelectedElement() throws IOException {
    out = getTempFile("test", ".zip");
    File[] in = { new File(PATH_BASEFILE, "bar"), new File(PATH_BASEFILE, "foo"), new File(PATH_BASEFILE, "empty-file.txt"), new File(PATH_BASEFILE, "foo.txt") };

    compressCommon(in);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void decompressUnsupportedOperation() {
    new CompressZipListener(null, null).decompress();
  }

  private void compressCommon(File[] in) {
    new CompressZipListener(in, out).compress();

    assertTrue(out.exists());
    assertTrue(out.length() > 0);
  }

}
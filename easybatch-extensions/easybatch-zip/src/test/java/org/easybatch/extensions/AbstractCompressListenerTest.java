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
package org.easybatch.extensions;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * The base element to implement test for compress components.
 *
 * @author Somma Daniele
 */
public abstract class AbstractCompressListenerTest extends AbstractZipListenerTest {

  protected static final String PATH_BASEFILE = "src/test/resources/org/easybatch/extensions/basefile";

  /**
   * Create a new <code>File</code> instance from the parent pathname
   * {@link #PATH_BASEFILE} and a child pathname string.
   * 
   * <p>
   * If <code>child</parent> is <code>null</code> then the new <code>File</code>
   * instance is created as if by invoking the single-argument <code>File</code>
   * constructor on the given <code>parent</code> pathname string.
   * 
   * @param child
   *          The child pathname string
   * @return An abstract pathname denoting a newly-created test file
   */
  protected static File createBaseTestFile(String child) {
    return (null != child) ? new File(PATH_BASEFILE, child) : new File(PATH_BASEFILE);
  }

  @Test
  public void compressSingleFileInSubDir() throws IOException {
    compressCommon(createBaseTestFile("bar/bar.txt"));
  }

  @Test
  public void compressSingleFolder() throws IOException {
    compressCommon(createBaseTestFile("bar"));
  }

  @Test
  public void compressSingleEmptyFolder() throws IOException {
    compressCommon(createBaseTestFile("foo"));
  }

  @Test
  public void compressRootFolder() throws IOException {
    compressCommon(createBaseTestFile(null));
  }

  @Test
  public void compressSelectedElement() throws IOException {
    File[] in = { createBaseTestFile("bar"), createBaseTestFile("foo"), createBaseTestFile("empty-file.txt"), createBaseTestFile("foo.txt") };
    compressCommon(in);
  }

  protected abstract void compressCommon(File... in);

}
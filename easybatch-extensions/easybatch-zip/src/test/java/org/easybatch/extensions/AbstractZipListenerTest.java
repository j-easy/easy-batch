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

/**
 * The base element to implement test for compress/decompress components.
 *
 * @author Somma Daniele
 */
public abstract class AbstractZipListenerTest {

  /**
   * Creates an empty file in the default temporary-file directory, using the
   * given prefix and suffix to generate its name.
   * 
   * @param prefix
   *          The prefix string to be used in generating the file's name; must
   *          be at least three characters long
   * @param suffix
   *          The prefix string to be used in generating the file's name; must
   *          be at least three characters long
   * @return An abstract pathname denoting a newly-created empty file
   * @throws IOException
   *           If a file could not be created
   */
  protected static File createTempFile(final String prefix, final String suffix) throws IOException {
    File file = File.createTempFile(prefix, suffix);
    file.delete();
    return file;
  }

}
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

/**
 * The base from where start to implement new decompress components. The
 * {@link #compress()} method is pre-implemented to throw a
 * {@link UnsupportedOperationException} if invoked. The final user must provide
 * the specific implementation for {@link #decompress()} method.
 * 
 * The components manage one compressed files in input and decompress it into
 * specified output folder.
 *
 * @author Somma Daniele
 */
public abstract class AbstractDecompressListener extends AbstractZipJobListener {

  protected File in;
  protected File out;

  /**
   * Create a new {@link AbstractDecompressListener}
   * 
   * @param in
   *          {@link File} must to be decompress
   * @param out
   *          {@link File} folder as output of the decompression
   */
  public AbstractDecompressListener(File in, File out) {
    super();
    this.in = in;
    this.out = out;
  }

  /**
   * Operation not supported.
   * 
   * @throws UnsupportedOperationException
   */
  @Override
  public final void compress() {
    throw new UnsupportedOperationException("Unsupported decompress function. Use the right component.");
  }

}
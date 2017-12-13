package org.easybatch.extensions;

import java.io.File;

/**
 * The base from where start to implement new compress components. The
 * {@link #decompress()} method is pre-implemented to throw a
 * {@link UnsupportedOperationException} if invoked. The final user must provide
 * the specific implementation for {@link #compress()} method.
 * 
 * The components manage an arbitrary number of files in input an create as
 * output a single compressed file.
 *
 * @author Somma Daniele
 */
public abstract class AbstractCompressListener extends AbstractZipJobListener {

  protected File[] in;
  protected File   out;

  /**
   * Create a new {@link AbstractCompressListener}
   * 
   * @param in
   *          {@link File}'s must to be compress
   * @param out
   *          {@link File} created as output of the compression
   */
  public AbstractCompressListener(File[] in, File out) {
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
  public final void decompress() {
    throw new UnsupportedOperationException("Unsupported compress function. Use the right component.");
  }

}
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
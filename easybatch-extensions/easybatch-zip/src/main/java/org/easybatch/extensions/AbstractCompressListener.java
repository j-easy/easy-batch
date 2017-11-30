/**
 * 
 */
package org.easybatch.extensions;

import java.nio.file.Path;

/**
 * 
 * @author Somma Daniele (C307838)
 */
public abstract class AbstractCompressListener extends AbstractZipJobListener {

  protected Path   outZip;
  protected Path[] inFile;

  public AbstractCompressListener(Path outZip, Path... inFile) {
    super();
    this.outZip = outZip;
    this.inFile = inFile;
  }

  @Override
  public final void decompress() {
    throw new UnsupportedOperationException("Unsupported compress function. Use the right component.");
  }

}
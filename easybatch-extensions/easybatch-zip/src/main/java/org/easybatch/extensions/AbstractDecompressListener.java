/**
 * 
 */
package org.easybatch.extensions;

import java.nio.file.Path;

/**
 * 
 * @author Somma Daniele (C307838)
 */
public abstract class AbstractDecompressListener extends AbstractZipJobListener {

  protected Path inZip;
  protected Path destDir;

  public AbstractDecompressListener(Path inZip, Path destDir) {
    super();
    this.inZip = inZip;
    this.destDir = destDir;
  }

  @Override
  public final void compress() {
    throw new UnsupportedOperationException("Unsupported decompress function. Use the right component.");
  }

}
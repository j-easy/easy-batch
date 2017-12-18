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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.easybatch.core.listener.JobListener;
import org.easybatch.extensions.AbstractDecompressListener;

/**
 * A {@link JobListener} to decompress files/folders in TAR archive format.
 *
 * @author Somma Daniele
 */
public class DecompressTarListener extends AbstractDecompressListener {

  private static final Logger LOGGER = Logger.getLogger(DecompressTarListener.class.getName());

  /**
   * Create a new {@link DecompressTarListener}
   * 
   * @param in
   *          {@link File} must to be decompress
   * @param out
   *          {@link File} folder as output of the decompression
   */
  public DecompressTarListener(File in, File out) {
    super(in, out);
  }

  @Override
  public void decompress() {
    try {
      decompress(in, out);
    } catch (ArchiveException | IOException e) {
      LOGGER.log(Level.SEVERE, "Error decompress file: " + e.getMessage(), e);
    }
  }

  /**
   * Decompress TAR file into folder.
   * 
   * @param in
   *          file to decompress
   * @param out
   *          folder where decompress
   * @throws ArchiveException
   * @throws IOException
   */
  private static void decompress(final File in, final File out) throws ArchiveException, IOException {
    if (!out.exists()) {
      out.mkdirs();
    }
    try (InputStream is = new FileInputStream(in);
        TarArchiveInputStream ais = (TarArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, is)) {
      decompress(out, ais);
    }
  }

  private static void decompress(final File out, final TarArchiveInputStream ais) throws IOException {
    TarArchiveEntry entry;
    while ((entry = ais.getNextTarEntry()) != null) {
      decompressEntry(out, ais, entry);
    }
  }

  private static void decompressEntry(final File out, final TarArchiveInputStream ais, final TarArchiveEntry entry) throws IOException {
    File of = new File(out, entry.getName());
    if (entry.isDirectory()) {
      of.mkdirs();
    } else {
      try (OutputStream os = new FileOutputStream(of)) {
        IOUtils.copy(ais, os);
      }
    }
  }

}
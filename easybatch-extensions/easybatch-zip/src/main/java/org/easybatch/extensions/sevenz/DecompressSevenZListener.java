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
package org.easybatch.extensions.sevenz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.easybatch.core.listener.JobListener;
import org.easybatch.extensions.AbstractDecompressListener;

/**
 * A {@link JobListener} to decompress files/folders in 7z archive format.
 *
 * @author Somma Daniele
 */
public class DecompressSevenZListener extends AbstractDecompressListener {

  private static final Logger LOGGER = Logger.getLogger(DecompressSevenZListener.class.getName());

  /**
   * Create a new {@link DecompressSevenZListener}
   * 
   * @param in
   *          {@link File} must to be decompress
   * @param out
   *          {@link File} folder as output of the decompression
   */
  public DecompressSevenZListener(File in, File out) {
    super(in, out);
  }

  @Override
  public void decompress() {
    try {
      decompress(in, out);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Error decompress file: " + e.getMessage(), e);
    }
  }

  /**
   * Decompress 7z file into folder.
   * 
   * @param in
   *          file to decompress
   * @param out
   *          folder where decompress
   * @throws IOException
   */
  private static void decompress(final File in, final File out) throws IOException {
    if (!out.exists()) {
      out.mkdirs();
    }
    try (SevenZFile szf = new SevenZFile(in)) {
      decompress(out, szf);
    }
  }

  public static void decompress(final File out, final SevenZFile szf) throws IOException {
    SevenZArchiveEntry entry;
    while ((entry = szf.getNextEntry()) != null) {
      decompressEntry(out, szf, entry);
    }
  }

  private static void decompressEntry(final File out, final SevenZFile szf, final SevenZArchiveEntry entry) throws IOException {
    File of = new File(out, entry.getName());
    if (entry.isDirectory()) {
      of.mkdirs();
    } else {
      try (OutputStream os = new FileOutputStream(of)) {
        byte[] content = new byte[(int) entry.getSize()];
        szf.read(content, 0, content.length);
        os.write(content);
      }
    }
  }

}
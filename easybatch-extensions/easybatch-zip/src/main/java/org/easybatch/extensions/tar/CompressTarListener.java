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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.easybatch.core.listener.JobListener;
import org.easybatch.core.util.Utils;
import org.easybatch.extensions.AbstractCompressListener;
import org.easybatch.extensions.factory.ArchiveEntryFactory;

/**
 * A {@link JobListener} to compress files/folders with the TAR archive format.
 *
 * @author Somma Daniele
 */
public class CompressTarListener extends AbstractCompressListener {

  private static final Logger LOGGER = Logger.getLogger(CompressTarListener.class.getName());

  /**
   * Create a new {@link CompressTarListener}
   * 
   * @param out
   *          {@link File} created as output of the compression
   * @param in
   *          {@link File}'s must to be compress
   */
  public CompressTarListener(File out, File... in) {
    super(out, in);
  }

  @Override
  public void compress() {
    try {
      compress(out, in);
    } catch (IOException | ArchiveException e) {
      LOGGER.log(Level.SEVERE, "Error compress file: " + e.getMessage(), e);
    }
  }

  /**
   * Compress files/folders into tar file.
   * 
   * @param out
   *          zip output file
   * @param in
   *          files/folders to add
   * @throws ArchiveException
   * @throws IOException
   */
  private void compress(final File out, final File... in) throws ArchiveException, IOException {
    try (OutputStream os = new FileOutputStream(out);
        ArchiveOutputStream archive = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, os)) {
      for (File file : in) {
        compress(file, archive);
      }
      archive.finish();
    }
  }

  private void compress(final File in, final ArchiveOutputStream archive) throws ArchiveException, IOException {
    if (in.isDirectory()) {
      String rootDir = in.getName();
      addDirEntry(archive, ArchiveStreamFactory.TAR, rootDir);
      Collection<File> fileList = FileUtils.listFilesAndDirs(in, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
      fileList.remove(in);
      for (File file : fileList) {
        if (file.isDirectory()) {
          addDirEntry(archive, ArchiveStreamFactory.TAR, rootDir + Utils.FILE_SEPARATOR + file.getName());
        } else {
          TarArchiveEntry entry = (TarArchiveEntry) ArchiveEntryFactory.getArchiveEntry(ArchiveStreamFactory.TAR,
              rootDir + Utils.FILE_SEPARATOR + getEntryName(in, file));
          entry.setSize(file.length());
          addFileEntry(archive, entry, file);
        }
      }
    } else {
      TarArchiveEntry entry = (TarArchiveEntry) ArchiveEntryFactory.getArchiveEntry(ArchiveStreamFactory.TAR, in.getName());
      entry.setSize(in.length());
      addFileEntry(archive, entry, in);
    }
  }

}
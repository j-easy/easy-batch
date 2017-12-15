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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.easybatch.core.listener.JobListener;
import org.easybatch.core.util.Utils;
import org.easybatch.extensions.AbstractCompressListener;

/**
 * A {@link JobListener} to compress files/folders with the 7z archive format.
 * https://memorynotfound.com/java-7z-seven-zip-example-compress-decompress-file/
 *
 * @author Somma Daniele
 */
public class CompressSevenZListener extends AbstractCompressListener {

  private static final Logger LOGGER = Logger.getLogger(CompressSevenZListener.class.getName());

  /**
   * Create a new {@link CompressSevenZListener}
   * 
   * @param out
   *          {@link File} created as output of the compression
   * @param in
   *          {@link File}'s must to be compress
   */
  public CompressSevenZListener(File out, File... in) {
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
   * Compress files/folders into 7z file.
   *
   * @param out
   *          7z output file
   * @param in
   *          files/folders to add
   * @throws ArchiveException
   * @throws IOException
   */
  private void compress(final File out, final File... in) throws ArchiveException, IOException {
    try (SevenZOutputFile sevenZOutput = new SevenZOutputFile(out)) {
      for (File file : in) {
        compress(file, sevenZOutput);
      }
    }
  }

  private void compress(final File in, final SevenZOutputFile sevenZOutput) throws ArchiveException, IOException {
    if (in.isDirectory()) {
      String rootDir = in.getName();
      addDirEntry(sevenZOutput, in, rootDir);
      Collection<File> fileList = FileUtils.listFilesAndDirs(in, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
      fileList.remove(in);
      for (File file : fileList) {
        if (file.isDirectory()) {
          addDirEntry(sevenZOutput, file, rootDir + Utils.FILE_SEPARATOR + file.getName());
        } else {
          addFileEntry(sevenZOutput, file, getEntryName(in, file), rootDir);
        }
      }
    } else {
      addFileEntry(sevenZOutput, in);
    }
  }

  /**
   * Add new directory entry into archive.
   * 
   * @param sevenZOutput
   *          where add element
   * @param file
   *          to add into archive
   * @param nameEntry
   *          used as name for the new entry
   * @throws IOException
   */
  private void addDirEntry(final SevenZOutputFile sevenZOutput, final File file, final String nameEntry) throws IOException {
    SevenZArchiveEntry entry = sevenZOutput.createArchiveEntry(file, nameEntry);
    sevenZOutput.putArchiveEntry(entry);
    sevenZOutput.closeArchiveEntry();
  }

  /**
   * Add new file entry into archive with same name of file in input and without
   * root directory.
   * 
   * @param sevenZOutput
   *          where add element
   * @param file
   *          to add into archive
   * @throws IOException
   */
  private void addFileEntry(final SevenZOutputFile sevenZOutput, final File file) throws IOException {
    addFileEntry(sevenZOutput, file, file.getName(), null);
  }

  /**
   * Add new file entry into archive.
   * 
   * @param sevenZOutput
   *          where add element
   * @param file
   *          to add into archive
   * @param nameEntry
   *          used as name for the new entry
   * @param rootDir
   *          if present combined with nameEntry
   * @throws IOException
   */
  private void addFileEntry(final SevenZOutputFile sevenZOutput, final File file, final String nameEntry, final String rootDir) throws IOException {
    SevenZArchiveEntry entry = sevenZOutput.createArchiveEntry(file, rootDir != null ? rootDir + Utils.FILE_SEPARATOR + nameEntry : nameEntry);
    sevenZOutput.putArchiveEntry(entry);
    try (FileInputStream in = new FileInputStream(file)) {
      byte[] b = new byte[1024];
      int count = 0;
      while ((count = in.read(b)) > 0) {
        sevenZOutput.write(b, 0, count);
      }
    }
    sevenZOutput.closeArchiveEntry();
  }

}
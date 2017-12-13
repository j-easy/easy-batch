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
package org.easybatch.extensions.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.easybatch.core.listener.JobListener;
import org.easybatch.core.util.Utils;
import org.easybatch.extensions.AbstractCompressListener;

/**
 * A {@link JobListener} to compress files/folders with the ZIP archive format.
 * 
 * Implementation is inspired from article <a href=
 * "http://www.thinkcode.se/blog/2015/08/21/packaging-a-zip-file-from-java-using-apache-commons-compress">Packaging
 * a zip file from Java using Apache Commons compress</a>
 *
 * @author Somma Daniele
 */
public class CompressZipListener extends AbstractCompressListener {

  private static final Logger LOGGER = Logger.getLogger(CompressZipListener.class.getName());

  /**
   * Create a new {@link CompressZipListener}
   * 
   * @param in
   *          {@link File}'s must to be compress
   * @param out
   *          {@link File} created as output of the compression
   */
  public CompressZipListener(File[] in, File out) {
    super(in, out);
  }

  @Override
  public void compress() {
    try {
      compress(in, out);
    } catch (IOException | ArchiveException e) {
      LOGGER.log(Level.SEVERE, "Error compress file: " + e.getMessage(), e);
    }
  }

  /**
   * Compress files/folders into zip file.
   *
   * @param in
   *          files/folders to add
   * @param out
   *          zip output file
   * @throws ArchiveException
   * @throws IOException
   */
  private void compress(final File[] in, final File out) throws ArchiveException, IOException {
    try (OutputStream os = new FileOutputStream(out);
        ArchiveOutputStream archive = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, os)) {
      for (File file : in) {
        compress(file, archive);
      }
      archive.finish();
    }
  }

  private void compress(final File in, ArchiveOutputStream archive) throws ArchiveException, IOException {
    if (in.isDirectory()) {
      String rootDir = in.getName();
      addDirEntry(archive, rootDir);
      Collection<File> fileList = FileUtils.listFilesAndDirs(in, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
      fileList.remove(in);
      for (File file : fileList) {
        if (file.isDirectory()) {
          addDirEntry(archive, rootDir + Utils.FILE_SEPARATOR + file.getName());
        } else {
          addFileEntry(archive, file, getEntryName(in, file), rootDir);
        }
      }
    } else {
      addFileEntry(archive, in);
    }
  }

  /**
   * Add new directory entry into archive.
   * 
   * @param archive
   *          where add element
   * @param nameEntry
   *          used as name for the new entry
   * @throws IOException
   */
  private void addDirEntry(final ArchiveOutputStream archive, final String nameEntry) throws IOException {
    ZipArchiveEntry entry = new ZipArchiveEntry(nameEntry + Utils.FILE_SEPARATOR);
    archive.putArchiveEntry(entry);
    archive.closeArchiveEntry();
  }

  /**
   * Add new file entry into archive with same name of file in input and without
   * root directory.
   * 
   * @param archive
   *          where add element
   * @param file
   *          to add into archive
   * @throws IOException
   */
  private void addFileEntry(final ArchiveOutputStream archive, final File file) throws IOException {
    addFileEntry(archive, file, file.getName(), null);
  }

  /**
   * Add new file entry into archive.
   * 
   * @param archive
   *          where add element
   * @param file
   *          to add into archive
   * @param nameEntry
   *          used as name for the new entry
   * @param rootDir
   *          if present combined with nameEntry
   * @throws IOException
   */
  private void addFileEntry(final ArchiveOutputStream archive, final File file, final String nameEntry, String rootDir) throws IOException {
    ZipArchiveEntry entry = new ZipArchiveEntry(rootDir != null ? rootDir + Utils.FILE_SEPARATOR + nameEntry : nameEntry);
    archive.putArchiveEntry(entry);
    try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(file))) {
      IOUtils.copy(input, archive);
    }
    archive.closeArchiveEntry();
  }

  /**
   * Remove the leading part of each entry that contains the source directory
   * name
   *
   * @param source
   *          the directory where the file entry is found
   * @param file
   *          the file that is about to be added
   * @return the name of an archive entry
   * @throws IOException
   *           if the io fails
   */
  private String getEntryName(final File source, final File file) throws IOException {
    int index = source.getAbsolutePath().length() + 1;
    String path = file.getCanonicalPath();
    return path.substring(index);
  }

}
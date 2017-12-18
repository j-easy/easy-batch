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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.easybatch.core.util.Utils;
import org.easybatch.extensions.factory.ArchiveEntryFactory;

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
   * @param out
   *          {@link File} created as output of the compression
   * @param in
   *          {@link File}'s must to be compress
   */
  public AbstractCompressListener(File out, File... in) {
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
  protected String getEntryName(final File source, final File file) throws IOException {
    int index = source.getAbsolutePath().length() + 1;
    String path = file.getCanonicalPath();
    return path.substring(index);
  }

  /**
   * Add new directory entry into archive.
   * 
   * @param archive
   *          where add element
   * @param archiverName
   *          the archive name/type
   * @param nameEntry
   *          used as name for the new entry
   * @throws IOException
   */
  protected void addDirEntry(final ArchiveOutputStream archive, final String archiverName, final String nameEntry) throws IOException {
    addDirEntry(archive, ArchiveEntryFactory.getArchiveEntry(archiverName, nameEntry + Utils.FILE_SEPARATOR));
  }

  /**
   * Add new directory entry into archive.
   * 
   * @param archive
   *          where add element
   * @param entry
   *          to add
   * @throws IOException
   */
  protected void addDirEntry(final ArchiveOutputStream archive, final ArchiveEntry entry) throws IOException {
    archive.putArchiveEntry(entry);
    archive.closeArchiveEntry();
  }

  /**
   * Add new file entry into archive with same name of file in input and without
   * root directory.
   *
   * @param archive
   *          where add element
   * @param archiverName
   *          the archive name/type
   * @param file
   *          to add into archive
   * @throws IOException
   */
  protected void addFileEntry(final ArchiveOutputStream archive, final String archiverName, final File file) throws IOException {
    addFileEntry(archive, archiverName, file, file.getName(), null);
  }

  /**
   * Add new file entry into archive.
   *
   * @param archive
   *          where add element
   * @param archiverName
   *          the archive name/type
   * @param file
   *          to add into archive
   * @param nameEntry
   *          used as name for the new entry
   * @param rootDir
   *          if present combined with nameEntry
   * @throws IOException
   */
  protected void addFileEntry(final ArchiveOutputStream archive, final String archiverName, final File file, final String nameEntry, String rootDir)
      throws IOException {
    ArchiveEntry entry = ArchiveEntryFactory.getArchiveEntry(archiverName, rootDir != null ? rootDir + Utils.FILE_SEPARATOR + nameEntry : nameEntry);
    addFileEntry(archive, entry, file);
  }

  /**
   * Add new file entry into archive.
   * 
   * @param archive
   *          where add element
   * @param entry
   *          to add
   * @param file
   *          to add into archive
   * @throws IOException
   */
  protected void addFileEntry(final ArchiveOutputStream archive, final ArchiveEntry entry, final File file) throws IOException {
    archive.putArchiveEntry(entry);
    try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(file))) {
      IOUtils.copy(input, archive);
    }
    archive.closeArchiveEntry();
  }

}
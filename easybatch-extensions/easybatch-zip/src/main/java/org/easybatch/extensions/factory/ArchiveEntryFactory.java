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
package org.easybatch.extensions.factory;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

/**
 * Factory bean that creates {@link ArchiveEntry} instances.
 *
 * @author Somma Daniele
 */
public class ArchiveEntryFactory {

  public static ArchiveEntry getArchiveEntry(final String archiverName, final String nameEntry) {
    if (ArchiveStreamFactory.ZIP.equalsIgnoreCase(archiverName)) {
      return new ZipArchiveEntry(nameEntry);
    } else if (ArchiveStreamFactory.TAR.equalsIgnoreCase(archiverName)) {
      return new TarArchiveEntry(nameEntry);
    } else if (ArchiveStreamFactory.SEVEN_Z.equalsIgnoreCase(archiverName)) {
      final SevenZArchiveEntry entry = new SevenZArchiveEntry();
      entry.setName(nameEntry);
      return entry;
    } else {
      throw new UnsupportedOperationException("Unsupported archive format.");
    }
  }

}
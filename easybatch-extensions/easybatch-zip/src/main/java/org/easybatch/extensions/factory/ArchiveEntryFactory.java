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
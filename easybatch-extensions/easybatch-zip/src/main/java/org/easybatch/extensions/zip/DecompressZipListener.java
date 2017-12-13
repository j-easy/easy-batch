package org.easybatch.extensions.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.easybatch.core.listener.JobListener;
import org.easybatch.extensions.AbstractDecompressListener;

/**
 * A {@link JobListener} to compress files/folders with the ZIP archive format.
 * 
 * Implementation is inspired from article <a href=
 * "http://www.thinkcode.se/blog/2015/08/21/packaging-a-zip-file-from-java-using-apache-commons-compress">Packaging
 * a zip file from Java using Apache Commons compress</a>
 *
 * @author Somma Daniele
 */

/**
 * A {@link JobListener} to decompress files/folders in ZIP archive format.
 * 
 * Implementation is inspired from articles <a href=
 * "https://hubpages.com/technology/Zipping-and-Unzipping-Nested-Directories-in-Java-using-Apache-Commons-Compress">Zipping
 * and Unzipping Nested Directories in Java using Apache Commons Compress (Zip
 * Unzip Files & Folders Recursively)</a> and
 * <a href= "https://gist.github.com/steventzeng-base/4ca4b0aa15ecdd6db097">Java
 * 7 NIO2 Unzip Code</a> and <a href=
 * "https://coderanch.com/t/646263/java/Apache-Commons-Compress-De-Compress">Apache
 * Commons Compress - De Compress Zip File</a>
 *
 * @author Somma Daniele
 */
public class DecompressZipListener extends AbstractDecompressListener {

  private static final Logger LOGGER = Logger.getLogger(DecompressZipListener.class.getName());

  /**
   * Create a new {@link DecompressZipListener}
   * 
   * @param in
   *          {@link File} must to be decompress
   * @param out
   *          {@link File} folder as output of the decompression
   */
  public DecompressZipListener(File in, File out) {
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
   * Decompress ZIP file into folder.
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
    try (InputStream is = new FileInputStream(in); ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, is)) {
      decompress(out, ais);
    }
  }

  private static void decompress(final File out, ArchiveInputStream ais) throws IOException {
    ZipEntry entry = null;
    while ((entry = (ZipArchiveEntry) ais.getNextEntry()) != null) {
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

}
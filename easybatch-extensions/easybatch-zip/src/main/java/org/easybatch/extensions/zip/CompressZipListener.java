/**
 * 
 */
package org.easybatch.extensions.zip;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.easybatch.extensions.AbstractCompressListener;

/**
 * Based on:
 * https://docs.oracle.com/javase/7/docs/technotes/guides/io/fsp/zipfilesystemprovider.html
 * http://fahdshariff.blogspot.it/2011/08/java-7-working-with-zip-files.html
 * https://stackoverflow.com/questions/14733496/is-it-possible-to-create-a-new-zip-file-using-the-java-filesystem
 * 
 * @author Somma Daniele (C307838)
 */
public class CompressZipListener extends AbstractCompressListener {

  private static final Logger              LOGGER = Logger.getLogger(DecompressZipListener.class.getName());

  private static final Map<String, String> ENV    = new HashMap<>();
  static {
    ENV.put("create", "true");
  }

  public CompressZipListener(Path outZip, Path... inFile) {
    super(outZip, inFile);
  }

  @Override
  public void compress() {
    try {
      URI uri = URI.create("jar:" + outZip.toUri().toString().replaceAll(" ", "%2520"));
      try (FileSystem zipfs = FileSystems.newFileSystem(uri, ENV)) {
        // copy the files into the zip file...
        for (Path externalTxtFile : inFile) {
          Path pathInZipfile = zipfs.getPath(File.separator + externalTxtFile.getFileName());
          Files.copy(externalTxtFile, pathInZipfile, StandardCopyOption.REPLACE_EXISTING);
        }
      }
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Error compress file: " + e.getMessage(), e);
    }
  }

}
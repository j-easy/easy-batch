/**
 * 
 */
package org.easybatch.extensions.zip;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.easybatch.extensions.AbstractDecompressListener;

/**
 * 
 * @author Somma Daniele (C307838)
 */
public class DecompressZipListener extends AbstractDecompressListener {

  private static final Logger              LOGGER = Logger.getLogger(DecompressZipListener.class.getName());

  private static final Map<String, String> ENV    = new HashMap<>();

  public DecompressZipListener(Path inZip, Path destDir) {
    super(inZip, destDir);
  }

  @Override
  public void decompress() {
    // implement decompression with zip technologies...
    try {
      if (Files.notExists(destDir)) {
        System.out.println(destDir + " does not exist. Creating...");
        Files.createDirectories(destDir);
      }

      URI uri = URI.create("jar:" + inZip.toUri().toString().replaceAll(" ", "%2520"));
      // try (FileSystem zipfs = FileSystems.newFileSystem(uri, ENV))

      try (FileSystem zipFileSystem = FileSystems.newFileSystem(uri, ENV)) {
        final Path root = zipFileSystem.getPath("/");

        // walk the zip file tree and copy files to the destination
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            final Path destFile = Paths.get(destDir.toString(), file.toString());
            System.out.printf("Extracting file %s to %s\n", file, destFile);
            Files.copy(file, destFile, StandardCopyOption.REPLACE_EXISTING);
            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            final Path dirToCreate = Paths.get(destDir.toString(), dir.toString());
            if (Files.notExists(dirToCreate)) {
              System.out.printf("Creating directory %s\n", dirToCreate);
              Files.createDirectory(dirToCreate);
            }
            return FileVisitResult.CONTINUE;
          }
        });
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // https://gist.github.com/steventzeng-base/4ca4b0aa15ecdd6db097
  public static void unzip(final Path zipFile, final Path decryptTo) {
    try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {
      ZipEntry entry;
      while ((entry = zipInputStream.getNextEntry()) != null) {
        LOGGER.log(Level.INFO, "entry name = {0}", entry.getName());
        final Path toPath = decryptTo.resolve(entry.getName());
        if (entry.isDirectory()) {
          Files.createDirectory(toPath);
        } else {
          Files.copy(zipInputStream, toPath);
        }
      }
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }

}
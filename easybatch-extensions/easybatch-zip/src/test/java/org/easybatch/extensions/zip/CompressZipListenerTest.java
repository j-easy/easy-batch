/**
 * 
 */
package org.easybatch.extensions.zip;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Somma Daniele (C307838)
 */
public class CompressZipListenerTest {

  private static Path         outZip;
  private static Path[]       inFile;
  private CompressZipListener czl;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    outZip = File.createTempFile("test", ".zip").toPath();
    Files.deleteIfExists(outZip);

    inFile = new Path[] { Paths.get("src/test/resources/bar.txt"), Paths.get("src/test/resources/empty-file.txt"), Paths.get("src/test/resources/foo.txt") };
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    Files.deleteIfExists(outZip);
  }

  @Before
  public void setUp() throws Exception {
    czl = new CompressZipListener(outZip, inFile);
  }

  @Test
  public void testCompress() throws IOException {
    czl.compress();

    assertTrue(Files.exists(outZip));
    assertTrue(Files.size(outZip) > 0);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testDecompress() {
    czl.decompress();
  }

}
package org.easybatch.extensions.zip;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.easybatch.extensions.AbstractZipListenerTest;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Somma Daniele
 */
public class DecompressZipListenerTest extends AbstractZipListenerTest {

  private static final String PATH_ZIPFILE = "src/test/resources/org/easybatch/extensions/zip";

  @Before
  public void setup() {
    if (OUT_FOLDER.exists()) {
      FileUtils.deleteQuietly(OUT_FOLDER);
    }
  }

  @Test
  public void decompressSingleFileInSubDir() throws IOException {
    File in = new File(PATH_ZIPFILE, "test_singlefileinsubdir.zip");

    decompressCommon(in);
  }

  @Test
  public void decompressSelectedElement() throws IOException {
    File in = new File(PATH_ZIPFILE, "test_selected.zip");

    decompressCommon(in);
  }

  @Test
  public void decompressSingleFolder() throws IOException {
    File in = new File(PATH_ZIPFILE, "test_singlefolder.zip");

    decompressCommon(in);
  }

  @Test
  public void decompressSingleEmptyFolder() throws IOException {
    File in = new File(PATH_ZIPFILE, "test_emptyfolder.zip");
    new DecompressZipListener(in, OUT_FOLDER).decompress();

    assertTrue(OUT_FOLDER.exists());
    assertTrue(FileUtils.sizeOf(OUT_FOLDER) == 0);
  }

  @Test
  public void decompressRootFolder() throws IOException {
    File in = new File(PATH_ZIPFILE, "test_basefile.zip");

    decompressCommon(in);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCompressUnsupportedOperation() {
    new DecompressZipListener(null, null).compress();
  }

  private void decompressCommon(File in) {
    new DecompressZipListener(in, OUT_FOLDER).decompress();

    assertTrue(OUT_FOLDER.exists());
    assertTrue(FileUtils.sizeOf(OUT_FOLDER) > 0);
  }

}
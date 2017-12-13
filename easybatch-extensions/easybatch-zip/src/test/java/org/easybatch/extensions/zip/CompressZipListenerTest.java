package org.easybatch.extensions.zip;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.easybatch.extensions.AbstractZipListenerTest;
import org.junit.After;
import org.junit.Test;

/**
 *
 * @author Somma Daniele
 */
public class CompressZipListenerTest extends AbstractZipListenerTest {

  private File out;

  @After
  public void tearDown() {
    if (out != null) {
      out.delete();
    }
  }

  @Test
  public void compressSingleFileInSubDir() throws IOException {
    out = getTempFile("test", ".zip");
    File[] in = { new File(PATH_BASEFILE, "bar/bar.txt") };

    compressCommon(in);
  }

  @Test
  public void compressSingleFolder() throws IOException {
    out = getTempFile("test", ".zip");
    File[] in = { new File(PATH_BASEFILE, "bar") };

    compressCommon(in);
  }

  @Test
  public void compressSingleEmptyFolder() throws IOException {
    out = getTempFile("test", ".zip");
    File[] in = { new File(PATH_BASEFILE, "foo") };

    compressCommon(in);
  }

  @Test
  public void compressRootFolder() throws IOException {
    out = getTempFile("test", ".zip");
    File[] in = { new File(PATH_BASEFILE) };

    compressCommon(in);
  }

  @Test
  public void compressSelectedElement() throws IOException {
    out = getTempFile("test", ".zip");
    File[] in = { new File(PATH_BASEFILE, "bar"), new File(PATH_BASEFILE, "foo"), new File(PATH_BASEFILE, "empty-file.txt"), new File(PATH_BASEFILE, "foo.txt") };

    compressCommon(in);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void decompressUnsupportedOperation() {
    new CompressZipListener(null, null).decompress();
  }

  private void compressCommon(File[] in) {
    new CompressZipListener(in, out).compress();

    assertTrue(out.exists());
    assertTrue(out.length() > 0);
  }

}
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
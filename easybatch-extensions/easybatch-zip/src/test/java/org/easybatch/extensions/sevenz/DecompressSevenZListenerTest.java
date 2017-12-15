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
package org.easybatch.extensions.sevenz;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.easybatch.extensions.AbstractDecompressListenerTest;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Somma Daniele
 */
public class DecompressSevenZListenerTest extends AbstractDecompressListenerTest {

  protected static final String PATH_SEVENZFILE = "src/test/resources/org/easybatch/extensions/7z";

  @Before
  public void setup() {
    if (OUT_FOLDER.exists()) {
      FileUtils.deleteQuietly(OUT_FOLDER);
    }
  }

  @Test
  public void decompressSingleFileInSubDir() throws IOException {
    decompressCommon(createArchiveTestFile(PATH_SEVENZFILE, "test_singlefileinsubdir.7z"));
  }

  @Test
  public void decompressSelectedElement() throws IOException {
    decompressCommon(createArchiveTestFile(PATH_SEVENZFILE, "test_selected.7z"));
  }

  @Test
  public void decompressSingleFolder() throws IOException {
    decompressCommon(createArchiveTestFile(PATH_SEVENZFILE, "test_singlefolder.7z"));
  }

  @Test
  public void decompressSingleEmptyFolder() throws IOException {
    decompressCommonEqualTo(createArchiveTestFile(PATH_SEVENZFILE, "test_emptyfolder.7z"), 0);
  }

  @Test
  public void decompressRootFolder() throws IOException {
    decompressCommon(createArchiveTestFile(PATH_SEVENZFILE, "test_basefile.7z"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCompressUnsupportedOperation() {
    new DecompressSevenZListener(null, null).compress();
  }

  private void decompressCommon(File in) {
    new DecompressSevenZListener(in, OUT_FOLDER).decompress();

    assertTrue(OUT_FOLDER.exists());
    assertTrue(FileUtils.sizeOf(OUT_FOLDER) > 0);
  }

  private void decompressCommonEqualTo(File in, long size) {
    new DecompressSevenZListener(in, OUT_FOLDER).decompress();

    assertTrue(OUT_FOLDER.exists());
    assertTrue(FileUtils.sizeOf(OUT_FOLDER) == size);
  }

}
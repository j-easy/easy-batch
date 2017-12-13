package org.easybatch.extensions;

import java.io.File;
import java.io.IOException;

import org.easybatch.core.util.Utils;

/**
 * The base element to implement test for compress/decompress components.
 *
 * @author Somma Daniele
 */
public abstract class AbstractZipListenerTest {

  protected static final String PATH_BASEFILE = "src/test/resources/org/easybatch/extensions/basefile";
  protected static final File   OUT_FOLDER    = new File(Utils.JAVA_IO_TMPDIR, "TEMPDECOMPRESS");

  /**
   * Creates an empty file in the default temporary-file directory, using the
   * given prefix and suffix to generate its name.
   * 
   * @param prefix
   *          The prefix string to be used in generating the file's name; must
   *          be at least three characters long
   * @param suffix
   *          The prefix string to be used in generating the file's name; must
   *          be at least three characters long
   * @return An abstract pathname denoting a newly-created empty file
   * @throws IOException
   *           If a file could not be created
   */
  protected static File getTempFile(final String prefix, final String suffix) throws IOException {
    File file = File.createTempFile(prefix, suffix);
    file.delete();
    return file;
  }

}
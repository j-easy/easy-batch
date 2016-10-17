package org.easybatch.extensions.stream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Charles Fleury
 * @since 17/10/16.
 */
public class PathRecordReaderTest {

  private PathRecordReader pathRecordReader;

  private Path emptyDirectory;

  @Before
  public void setUp() throws Exception {
    emptyDirectory = Files.createTempDirectory("easybatch_test");
  }

  @Test
  public void whenDirectoryIsNotEmpty_thenThereShouldBeANextRecordToRead() throws Exception {
    pathRecordReader = new PathRecordReader(Paths.get("src/main/java/org/easybatch/extensions/stream"));
    pathRecordReader.open();
    assertThat(pathRecordReader.readRecord()).isNotNull();
  }

  @Test
  public void whenDirectoryIsNotEmpty_thenThereShouldBeANextRecordToRead_recursive() throws Exception {
    pathRecordReader = new PathRecordReader(Paths.get("src/main/java/org/easybatch/extensions"), true);
    pathRecordReader.open();
    assertThat(pathRecordReader.readRecord()).isNotNull();
  }

  @Test
  public void whenDirectoryIsNotEmptyButDirs_thenThereShouldBeNoNextRecordToRead_not_recursive() throws Exception {
    pathRecordReader = new PathRecordReader(Paths.get("src/main/java/org/easybatch/extensions"), false);
    pathRecordReader.open();
    assertThat(pathRecordReader.readRecord()).isNull();
  }

  @Test
  public void whenDirectoryIsEmpty_thenThereShouldBeNoNextRecordToRead() throws Exception {
    pathRecordReader = new PathRecordReader(emptyDirectory);
    pathRecordReader.open();
    assertThat(pathRecordReader.readRecord()).isNull();
  }

  @Test(expected = IllegalArgumentException.class)
  public void whenDirectoryDoesNotExist_thenShouldThrowAnIllegalArgumentException() throws Exception {
    pathRecordReader = new PathRecordReader(Paths.get("src/main/java/ImSureThisDirectoryDoesNotExist"));
    pathRecordReader.open();
  }

  @After
  public void tearDown() throws Exception {
    pathRecordReader.close();
    Files.delete(emptyDirectory);
  }


}
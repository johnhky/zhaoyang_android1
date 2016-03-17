package io.ganguo.library.util;

import java.io.File;
import java.io.IOException;

import io.ganguo.library.ApplicationTest;
import io.ganguo.library.Config;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by Tony on 10/4/15.
 */
public class FilesTest extends ApplicationTest {

    private Logger logger = LoggerFactory.getLogger(FilesTest.class);

    public void testWriteAndRead() throws IOException {
        String testContent1 = "test content.";
        File file1 = new File(Config.getDataPath(), "test");
        Files.write(file1, testContent1);

        String testContent2 = "test content.";
        String testContentAppend = "append content.";
        File file2 = new File(Config.getDataPath(), "test2");
        Files.write(file2, testContent2);
        Files.write(file2, testContentAppend, true);

        assertEquals(testContent1, Files.readString(file1));
        assertEquals(testContent2 + testContentAppend, Files.readString(file2));

        logger.d(Files.readString(file1));
        logger.d(Files.readString(file2));
    }

    public void testMove() throws IOException {
        File from = new File(Config.getDataPath(), "test_mv_from");
        File to = new File(Config.getDataPath(), "test_mv_to");
        Files.write(from, "test content.");
        Files.move(from, to);

        assertFalse(Files.checkFileExists(from.getAbsolutePath()));
        assertTrue(Files.checkFileExists(to.getAbsolutePath()));
    }

    public void testCopy() throws IOException {
        File from = new File(Config.getDataPath(), "test_cp_from");
        File to = new File(Config.getDataPath(), "test_cp_to");
        Files.write(from, "test content.");
        Files.copy(from, to);

        assertEquals(Files.readString(from), Files.readString(to));
    }

    public void testAsset() throws IOException {
        File to = new File(Config.getDataPath(), "assert_test");
        Files.copyAssetFile(getContext(), "test", to);
    }

    public void testFormatSize() {
        logger.i("formatSize:" + Files.formatSize(1000000));
    }

    public void testExtension() throws IOException {
        File file1 = new File(Config.getDataPath(), "test.txt");
        Files.write(file1, "test content.");

        assertEquals(Files.getExtension(file1), "txt");

        File file2 = new File(Config.getDataPath(), "test");
        Files.write(file2, "test content.");

        assertFalse(Strings.isEquals(Files.getExtension(file2), "txt"));
    }

//    public void testDelete() {
//        File file = Config.getDataPath();
//        Files.deleteFiles(file);
//
//        assertFalse(file.exists());
//    }
}

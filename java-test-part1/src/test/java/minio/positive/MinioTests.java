package minio.positive;

import net.bugreaper.modules.filehelper.FileHelper;
import net.bugreaper.modules.minio.Minio;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static net.bugreaper.core.allurereporter.AllureReporter.addStepAttachment;
import static net.bugreaper.core.filereaders.ResourcesFileReader.createResourceFileWithSize;
import static net.bugreaper.core.filereaders.ResourcesFileReader.readResourceFile;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Tag("minio")
@Epic("[minio]")
@Feature("[minio] Tests as is")
class MinioTests {

    private final Minio minio = new Minio();

    private static final String TEST_FILE = "data/test_file_1.txt";
    private static final String TEST_FILE_NO_DIR = "test_file_1.txt";
    private static final String TEST_FILE_LINES = "data/test_file_with_lines.txt";

    @Test
    @Description("""
            Show Minio config summary (return String & print log INFO)""")
    void checkSummary() {
        addStepAttachment("Minio info", minio.getConfigSummary());
    }

    @Test
    @Description("""
            Test create bucket -> upload objects tu bucket (and subfolder) -> read from bucket
            -> download from bucket -> remove object -> delete empty bucket
            with asserts on some steps""")
    void testE2E() {
        var bucket = "e2e-bucket";

        minio.createBucket(bucket);
        minio.seeBucketExists(bucket);

        minio.cleanBucket(bucket);
        minio.seeBucketIsEmpty(bucket);

        minio.uploadFileToBucket(bucket, TEST_FILE);
        minio.uploadFileToBucket(bucket, TEST_FILE_LINES, "some_new_dir/object.txt");

        minio.seeBucketIsNotEmpty(bucket);
        minio.seeObjectsCountIsGreaterThan(bucket, 1);
        minio.seeObjectsCountIsExactly(bucket, 2);
        minio.seeObjectsCountIsLessThan(bucket, 3);

        minio.getObjectsList(bucket)
                .seeListHasExactlyCount(2)
                .seeListAnyEquals(TEST_FILE_NO_DIR)
                .seeListAnyContains("some_new_dir/object.txt");

        minio.deleteFilledBucket(bucket);

        minio.seeBucketDoesNotExist(bucket);
    }

    @Test
    @Description("""
            Test create bucket -> upload object and share it than download & check contains of object""")
    void testObjectSharingDownload() {
        var bucket = "shared-bucket";
        var object = "some_new_dir/object.txt";


        minio.createBucket(bucket);
        minio.cleanBucket(bucket);
        minio.uploadFileToBucket(bucket, TEST_FILE_LINES, "some_new_dir/object.txt");

        String link = minio.shareObjectInBucket(bucket, object);

        minio.downloadObjectBySharedLink(link, "temp/my_shared_file.txt");

        FileHelper fileHelper = new FileHelper();

        fileHelper.seeFileContainString(TEST_FILE_LINES, readResourceFile("temp/my_shared_file.txt"));

        //or without helper
        assertEquals(readResourceFile(TEST_FILE_LINES), readResourceFile("temp/my_shared_file.txt"), "Shared object correct");
    }

    @Test
    @Description("""
            Test create bucket -> upload object and share it than download & check contains of object""")
    void testObjectSharingRead() {
        var bucket = "shared-bucket2";
        var object = "some_new_dir/object2.txt";


        minio.createBucket(bucket);
        minio.cleanBucket(bucket);
        minio.uploadFileToBucket(bucket, TEST_FILE, "some_new_dir/object2.txt");

        String link = minio.shareObjectInBucket(bucket, object);

        //or without helper
        assertEquals(readResourceFile(TEST_FILE), minio.getObjectBySharedLink(link), "Shared object correct");
    }

    @Test
    @Description("""
            Test create bucket -> upload object(177 bytes) and check it""")
    void testObjectSizeCheck() {
        var bucket = "test2-bucket";
        var object = "object2.txt";

        String fileName = "temp/test_size_1.txt";
        long expectedBytes = 177;


        minio.createBucket(bucket);
        minio.cleanBucket(bucket);

        createResourceFileWithSize(fileName, expectedBytes);

        minio.uploadFileToBucket(bucket, fileName, object);

        minio.seeObjectExists(bucket, object);

        minio.seeObjectSizeExactly(bucket, object, expectedBytes);
        minio.seeObjectSizeIsGreaterThan(bucket, object, expectedBytes - 1);
        minio.seeObjectSizeIsLessThan(bucket, object, expectedBytes + 1);

    }

    @Test
    @Description("""
            Test create bucket -> upload object -> check exist -> delete object -> check not exist""")
    void testObjectCheck() {
        var bucket = "test3-bucket";
        var object = "object2.txt";

        minio.createBucket(bucket);
        minio.cleanBucket(bucket);

        minio.uploadFileToBucket(bucket, TEST_FILE, object);
        minio.seeObjectExists(bucket, object);

        minio.deleteObjectFromBucket(bucket, object);
        minio.seeObjectDoesNotExist(bucket, object);
    }

}

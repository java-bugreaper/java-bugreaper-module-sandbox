package minio.positive;

import io.bugreaper.minio.MinioSetupAsIs;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.bugreaper.core.assertable.stringlist.ListOperators.*;
import static io.bugreaper.core.filereaders.ResourcesFileReader.createResourceFileWithSize;
import static io.bugreaper.core.filereaders.ResourcesFileReader.readResourceFile;
import static helper.ApiExtractor.extractBody;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Tag("minio")
@Epic("[minio] Tests as is")
class MinioTests {

    private final MinioSetupAsIs minio = new MinioSetupAsIs();

    private static final String TEST_FILE = "data/test_file_1.txt";
    private static final String TEST_FILE_NO_DIR = "test_file_1.txt";
    private static final String TEST_FILE_LINES = "data/test_file_with_lines.txt";

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
        minio.seeCountObjectsInBucketGreater(bucket, 1);
        minio.seeCountObjectsInBucketExactly(bucket, 2);
        minio.seeCountObjectsInBucketLess(bucket, 3);

        minio.getObjectsList(bucket)
                .testInLIst(elementsCountInList(2))
                .testInLIst(stringEqualsInList(TEST_FILE_NO_DIR))
                .testInLIst(stringContainsInList("some_new_dir/object.txt"));

        minio.deleteFilledBucket(bucket);

        minio.seeBucketNotExists(bucket);
    }

    @Test
    @Description("""
            Test create bucket -> upload object and share it (check contains of object)""")
    void testObjectSharing() {
        var bucket = "shared-bucket";
        var object = "some_new_dir/object.txt";


        minio.createBucket(bucket);
        minio.cleanBucket(bucket);
        minio.uploadFileToBucket(bucket, TEST_FILE_LINES, "some_new_dir/object.txt");

        String url = minio.shareObjectInBucket(bucket, object);

        String content = extractBody(url);

        //TODO
        //better do with API module(in future)
        assertEquals(readResourceFile(TEST_FILE_LINES), content, "Shared object correct");
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
        minio.seeObjectSizeGreater(bucket, object, expectedBytes - 1);
        minio.seeObjectSizeLess(bucket, object, expectedBytes + 1);

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
        minio.seeObjectNotExists(bucket, object);
    }

}

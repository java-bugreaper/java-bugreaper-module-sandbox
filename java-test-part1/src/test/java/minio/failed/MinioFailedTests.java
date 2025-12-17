package minio.failed;

import net.bugreaper.modules.minio.Minio;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static net.bugreaper.core.filereaders.ResourcesFileReader.createResourceFileWithSize;


@Tag("failed")
@Epic("[minio-failed] Expected failed minio tests")
class MinioFailedTests {

    private final Minio minio = new Minio();

    private static final String TEST_BUCKET = "my-failed-bucket";


    @Test
    @Description("""
    Test create bucket -> check object exists failed""")
    void testObjectExistsFailed() {
        var bucket = "no-object-bucket";

        minio.createBucket(bucket);
        minio.seeObjectExists(bucket, "some_object");

    }


    @Test
    @Description("""
    Check object exists failed because bucket not exists""")
    void testObjectExistsNoBucketFailed() {
        var bucket = "not-existing-bucket";

        minio.seeObjectExists(bucket, "some_object");
    }

    @Test
    @Description("""
    Exception on large upload file size set:100""")
    void testObjectUploadSizeFailed() {
        Minio minioUploadSize = minio.withMaxUploadSize(100);

        minioUploadSize.createBucket(TEST_BUCKET);

        String fileName = "temp/test_size_1.txt";
        long expectedBytes = 101;

        createResourceFileWithSize(fileName, expectedBytes);

        minioUploadSize.uploadFileToBucket(TEST_BUCKET, fileName, "some_name");
    }

    @Test
    @Description("""
    Exception on large download file size set:50""")
    void testObjectDownloadSizeFailed() {
        Minio minioDownloadSize = minio.withMaxDownloadObjectSize(50);

        minioDownloadSize.createBucket(TEST_BUCKET);

        String fileName = "temp/test_size_1.txt";
        long expectedBytes = 51;

        createResourceFileWithSize(fileName, expectedBytes);

        minioDownloadSize.uploadFileToBucket(TEST_BUCKET, fileName, "some_name");
        minioDownloadSize.downloadObjectFromBucket(TEST_BUCKET, "some_name", "mwFile");
    }

    @Test
    @Description("""
    Failed on check object size less""")
    void testObjectCheckSizeFailed() {
        minio.createBucket(TEST_BUCKET);

        String fileName = "temp/test_size_1.txt";
        long expectedBytes = 51;

        createResourceFileWithSize(fileName, expectedBytes);
        minio.uploadFileToBucket(TEST_BUCKET, fileName, "some_name");
        minio.seeObjectSizeIsLessThan(TEST_BUCKET, "some_name", 20);
    }

}

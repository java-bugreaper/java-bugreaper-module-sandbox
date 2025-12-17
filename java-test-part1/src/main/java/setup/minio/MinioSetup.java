package setup.minio;

import net.bugreaper.core.assertable.AssertableStringList;
import net.bugreaper.modules.minio.Minio;

public class MinioSetup {

    private static MinioSetup instance;
    private final Minio minio;

    public MinioSetup() {
        this.minio = new Minio();
    }

    public static MinioSetup getInstance() {
        if (instance == null) {
            instance = new MinioSetup();
        }

        return instance;
    }

    protected static final String TEST_BUCKET = "my-bucket";


    public void createMyBucket() {
        minio.createBucket(TEST_BUCKET);
    }

    public void uploadFileToMyBucket(String filePath, String objectName) {
        minio.uploadFileToBucket(TEST_BUCKET, filePath, objectName);
    }

    public void seeMyBucketIsNotEmpty() {
        minio.seeBucketIsNotEmpty(TEST_BUCKET);
    }

    public void seeMyBucketIsEmpty() {
        minio.seeBucketIsEmpty(TEST_BUCKET);
    }

    public void seeMyObjectExists(String objectName) {
        minio.seeObjectExists(TEST_BUCKET, objectName);
    }

    public void seeMyObjectSizeGreater(String object, long size) {
        minio.seeObjectSizeIsGreaterThan(TEST_BUCKET, object, size);
    }

    public void downloadObjectFromMyBucket(String object, String filePath) {
        minio.downloadObjectFromBucket(TEST_BUCKET, object, filePath);
    }

    public void cleanMyBucket() {
        minio.cleanBucket(TEST_BUCKET);
    }

    public AssertableStringList getMyObjectsList() {
        return minio.getObjectsList(TEST_BUCKET);
    }

}

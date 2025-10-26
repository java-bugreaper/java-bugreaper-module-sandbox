package io.bugreaper.minio;

import io.bugreaper.ProjectConfig;
import io.bugreaper.core.assertable.AssertableStringList;
import io.bugreaper.modules.minio.Minio;
import org.aeonbits.owner.ConfigFactory;

public class MinioSetup {

    private static MinioSetup instance;
    private final Minio minio;

    final ProjectConfig conf = ConfigFactory.create(ProjectConfig.class, System.getProperties());


    public MinioSetup() {
        this.minio = new Minio(
                conf.minioUrl(),
                conf.minioPort(),
                conf.minioUsername(),
                conf.minioPassword());
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
        minio.seeObjectSizeGreater(TEST_BUCKET, object, size);
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

package minio.positive;

import io.qameta.allure.Description;
import minio.TestMinioSetup;
import org.junit.jupiter.api.Test;


class MyMinioTests extends TestMinioSetup {

    @Test
    @Description("""
            Upload objects to bucket -> check bucket not empty -> download from bucket
            -> upload from bucket -> check object size""")
    void testOnMyBucket() {

        myMinio.uploadFileToMyBucket(TEST_FILE, "test-obj");
        myMinio.downloadObjectFromMyBucket("test-obj", "temp/test_myFile.txt");

        myMinio.seeMyBucketIsNotEmpty();
        myMinio.seeMyObjectSizeGreater("test-obj", 5);

    }

    @Test
    @Description("""
             Check bucket empty -> upload objects to bucket -> check object exists -> clean bucket
            -> heck bucket empty -> check object size""")
    void testOnMyBucket2() {

        myMinio.seeMyBucketIsEmpty();

        myMinio.uploadFileToMyBucket(TEST_FILE, "test-obj");
        myMinio.seeMyObjectExists("test-obj");

        myMinio.getMyObjectsList()
                .seeListHasExactlyCount(1)
                .seeListAnyContains("test");

        myMinio.cleanMyBucket();
        myMinio.seeMyBucketIsEmpty();
    }


}

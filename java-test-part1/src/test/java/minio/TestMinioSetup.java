package minio;

import setup.minio.MinioSetup;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;


@Tag("minio")
@Epic("[minio]")
@Feature("[minio] Tests with pre setup")
public abstract class TestMinioSetup {

    protected static final MinioSetup myMinio = MinioSetup.getInstance();
    protected static final String TEST_FILE = "data/test_file_1.txt";


    @BeforeAll
    public static void createBucket() {
        myMinio.createMyBucket();
    }

    @BeforeEach
    @Step("Clean bucket")
    public void clean() {
        myMinio.cleanMyBucket();
    }

    @AfterEach
    @Step("Check object list")
    public void getLogs() {
        myMinio.getMyObjectsList();
    }

}

package rest.failed;

import net.bugreaper.modules.mocks.MocksApi;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import setup.api.ApiHelper;
import setup.mocks.MockCreate;
import setup.mocks.MockVerify;


@Tag("failed")
@Epic("[rest-failed] Expected failed Api & Mocks tests")
public abstract class TestsSetupFallen {

    //separated objects for create & verify mocks
    // you can hold them in one
    protected final MockCreate mockCreate = new MockCreate();
    protected final MockVerify mockVerify = new MockVerify();

    // on enchanted report (also cam be set in config 'enchanted-report: true')
    //in tests usually not necessary to crete multiple Verify with different config - it`s just example
    protected final MockVerify mockVerifyEnchanted = (MockVerify) new MockVerify().setEnchantedReport(true);

    protected final MocksApi mocksApi = MocksApi.getInstance();

    protected final ApiHelper api = ApiHelper.getInstance();


    @BeforeEach
    @Step("Clear mocks")
    public void clean() {
        mocksApi.resetMocks();
    }

    @AfterEach
    @Step("Logs after tests")
    public void getLogs() {
        mocksApi.showBaseRequestsList();
        mocksApi.showMockLogs();
    }
}

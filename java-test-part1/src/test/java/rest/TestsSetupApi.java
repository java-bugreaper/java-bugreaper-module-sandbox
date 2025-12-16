package rest;

import io.bugreaper.modules.api.Api;
import io.bugreaper.modules.mocks.MocksApi;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import setup.api.ApiHelper;
import setup.mocks.MockCreate;
import setup.mocks.MockVerify;


@Tag("rest")

@Epic("[rest] Tests for API & mocks")
@Feature("[rest] Tests for API & mocks")

@TestMethodOrder(MethodOrderer.MethodName.class)
public abstract class TestsSetupApi {


    protected final MockCreate mockCreate = new MockCreate();
    protected final MockVerify mockVerify = new MockVerify();
    protected final MocksApi mockCustomApi = new MocksApi();
    protected final ApiHelper api = ApiHelper.getInstance();

    @BeforeEach
    @Step("Reset mocks")
    public void clean() {
        mockCustomApi.resetMocks();
    }

    @AfterEach
    @Step("Logs after tests")
    public void getLogs() {
        mockCustomApi.showBaseRequestsList();
        mockCustomApi.showMockLogs();
    }

}

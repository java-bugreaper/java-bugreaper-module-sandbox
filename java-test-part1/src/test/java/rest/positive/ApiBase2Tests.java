package rest.positive;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import net.bugreaper.modules.api.Api;
import net.bugreaper.modules.mocks.MocksApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static net.bugreaper.core.allurereporter.AllureReporter.reporter;

@Tag("rest")
@Epic("[rest] Tests for API & mocks")
@Feature("[rest] Api Headers & Query-params global/specific")
class ApiBase2Tests {

    protected final MocksApi mockApi = new MocksApi();

    @BeforeEach
    @Step("Reset mocks")
    public void clean() {
        mockApi.resetMocks();
        mockApi.createMock("""
            {
              "httpRequest": {
              },
              "httpResponse": {
                "statusCode": 200,
                "body": {
                  "result": "ok"
                }
              }
            }""");
    }

    @AfterEach
    @Step("Logs after tests")
    public void getLogs() {
        mockApi.showMockRequests();
        mockApi.showMockLogs();
    }

    @Test
    @Description("""
            Auth configurations""")
    void checkAuth() {
        final Api apiHead = new Api();

        reporter("No Auth by config");
        apiHead.sendGet("/test-get");

        reporter("Set global Base Auth");
        apiHead.setBasicAuth("user", "pass").sendGet("/test-get");

        reporter("Set global Bearer Auth (Base will be ignored)");
        apiHead.setBearerAuth("my-token").sendGet("/test-get");

        reporter("Clean: no Auth");
        apiHead.setNoAuth().sendGet("/test-get");
    }


    @Test
    @Description("""
            Headers global & specific request setup""")
    void checkHeaders() {
        final Api apiHead = new Api();

        reporter("No headers");
        apiHead.sendGet("/test-get");

        reporter("Set global headers");
        apiHead.setHeaders(Map.of("par1", "data1")).sendGet("/test-get");

        reporter("Overwrite global headers");
        apiHead.setHeaders(Map.of("par_new1", "data_new1")).sendGet("/test-get");

        reporter("Add global headers");
        apiHead.setHeader("par_new2", "data_new2").sendGet("/test-get");

        reporter("Specific headers (use for next request ignore global)");
        apiHead.requestHeaders(Map.of("spec1", "data_spec1")).sendGet("/test-get");

        reporter("Rollback to global after specific");
        apiHead.sendGet("/test-get");

        reporter("Specific headers multi (use for next request ignore global)");
        apiHead.requestHeader("spec1", "data_spec1").requestHeader("spec2", "data_spec2").sendGet("/test-get");

        reporter("Clean: no headers");
        apiHead.cleanSetHeaders().sendGet("/test-get");
    }

    @Test
    @Description("""
            Params global & specific request setup""")
    void checkParams() {
        final Api apiParams = new Api();

        reporter("No params");
        apiParams.sendGet("/test-get");

        reporter("Set global params");
        apiParams.setQueryParams(Map.of("par1", "data1")).sendGet("/test-get");

        reporter("Overwrite global params");
        apiParams.setQueryParams(Map.of("par_new1", "data_new1")).sendGet("/test-get");

        reporter("Specific params (use for next request ignore global)");
        apiParams.requestQueryParams(Map.of("spec1", "data_spec1")).sendGet("/test-get");

        reporter("Rollback to global after specific");
        apiParams.sendGet("/test-get");

        reporter("Clean: no params");
        apiParams.cleanSetQueryParams().sendGet("/test-get");
    }

}

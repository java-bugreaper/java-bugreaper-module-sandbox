package rest.positive;

import io.bugreaper.modules.api.Api;
import io.bugreaper.modules.mocks.MocksApi;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.bugreaper.core.allurereporter.AllureReporter.addStepAttachment;
import static io.bugreaper.core.mappers.StringMappers.stringMapper;

@Tag("rest")
@Epic("[rest] Tests for API & mocks")
@Feature("[rest] Api & Mocks tests as is")
class ApiBaseTests {

    // simple use -> get all setup from config use methods as is
    final Api api = new Api();
    final MocksApi mocksApi = new MocksApi();

    final String endpoint = "/rest/test";
    final String body = """
                                {"id": 1}""";

    @Test
    @Description("""
            Show API & mocks config summary (return String & print log INFO)""")
    void checkSummary() {
        addStepAttachment("Api info", api.getConfigSummary());
        addStepAttachment("Mocks info", mocksApi.getConfigSummary());
    }

    @Step("(MOCK)[VERIFY] method: {method}, endpoint: {endpoint}")
    private void verifyMockMethod(String method, String endpoint, int receivedCount) {

        mocksApi.verifyMock(stringMapper("""
                        {
                            "httpRequest": {
                                "method": "${method}",
                                "path": "${endpoint}"
                            },
                            "times": {
                                "atLeast": ${receivedCount},
                                "atMost": ${receivedCount}
                            }
                        }""",
                Map.of(
                        "method", method,
                        "endpoint", endpoint,
                        "receivedCount", receivedCount
                )));
    }

    @Step("(MOCK)[VERIFY] method: {method}, endpoint: {endpoint}")
    public void verifyMockMethodBody(String method, String endpoint, String body, int receivedCount) {

        mocksApi.verifyMock(stringMapper("""
                        {
                            "httpRequest": {
                                "method": "${method}",
                                "path": "${endpoint}",
                                "body": ${body}
                            },
                            "times": {
                                "atLeast": ${receivedCount},
                                "atMost": ${receivedCount}
                            }
                        }""",
                Map.of(
                        "method", method,
                        "endpoint", endpoint,
                        "body", body,
                        "receivedCount", receivedCount
                )));
    }


    @BeforeEach
    @Step("Reset mocks")
    void mocksPrepare() {
        mocksApi.resetMocks();
        mocksApi.createMock("""
            {
              "httpRequest": {
              },
              "httpResponse": {
                "statusCode": 200,
                "body": {
                  "result": "ok"
                }
              },
              "priority": -1
            }""");
    }

    @Test
    void testGet() {
        api.sendGet(endpoint)
                .seeResponseCodeIsSuccessful();

        verifyMockMethod("GET", endpoint, 1);
    }

    @Test
    void testPostNoBody() {
        api.withLogging(true).sendPost(endpoint)
                .seeResponseCodeIs(200);

        verifyMockMethod("POST", endpoint, 1);
    }

    @Test
    void testPostBody() {
        api.sendPost(endpoint, body)
                .seeResponseCodeIs(200);

        verifyMockMethodBody("POST", endpoint, body, 1);
    }

    @Test
    void testDeleteNoBody() {
        api.sendDelete(endpoint)
                .seeResponseCodeIsSuccessful();

        verifyMockMethod("DELETE", endpoint, 1);
    }

    @Test
    void testDeleteBody() {
        api.sendDelete(endpoint,
                        """
                                {"id": 1}""")
                .seeResponseCodeIs(200);

        verifyMockMethodBody("DELETE", endpoint, body, 1);
    }

    @Test
    void testPutNoBody() {
        api.sendPut(endpoint)
                .seeResponseCodeIs(200);

        verifyMockMethod("PUT", endpoint, 1);
    }

    @Test
    void testPutBody() {
        api.sendPut(endpoint, body)
                .seeResponseCodeIsSuccessful();

        verifyMockMethodBody("PUT", endpoint, body, 1);
    }

    @Test
    void testPatchBody() {
        api.sendPatch(endpoint, body)
                .seeResponseCodeIs(200);

        verifyMockMethodBody("PATCH", endpoint, body, 1);
    }

    @Test
    void testOptions() {
        api.sendOptions(endpoint)
                .seeResponseCodeIs(200);

        verifyMockMethod("OPTIONS", endpoint, 1);
    }

    @Test
    void testHead() {
        api.sendHead(endpoint)
                .seeResponseCodeIs(200);

        verifyMockMethod("HEAD", endpoint, 1);
    }

}

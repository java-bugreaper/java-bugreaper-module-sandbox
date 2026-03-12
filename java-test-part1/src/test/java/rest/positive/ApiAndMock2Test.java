package rest.positive;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;
import rest.TestsSetupApi;

import static net.bugreaper.core.allurereporter.AllureReporter.reporter;


@Feature("[rest] API & Mock checks 2")
class ApiAndMock2Test extends TestsSetupApi {

    @Test
    @Description("""
            Create GET mock 401
            -> GET request check code 401
            -> Verify GET request not contain Auth""")
    void checkGet_mock_request_verifyNoAth() {

        final var id = "15";

        mockCreate.getUser_error(id, 401);

        api.getUserNoAuth(id)
                .seeResponseCodeIs(401);

        mockCustomApi.verifyMockCustom(
                "verify GET no auth",
                "data/mocks/verify/get_no_auth.json");
    }

    @Test
    @Description("""
            Create POST mock
            -> POST request
            -> Verify POST request Exactly""")
    void checkPost_mock_request_verifyExactly() {

        final var id = "1";
        final var name = "Alex";
        final var email = "alex@com.com";

        mockCreate.postUser(
                id,
                200,
                name,
                email);

        api.postUser(id, name, email)
                .seeResponseCodeIs(200);

        mockVerify.postUserExactly(id, name, email, 1);
    }

    @Test
    @Description("""
            Create POST mock from file
            -> POST request hardcode
            -> Verify POST request from file""")
    void checkPost_mock_request_verify_fromFile() {

        mockCustomApi.createMockCustom(
                "create POST mock",
                "data/mocks/expect/post_ex_1.json");

        api.postUser(
                        "1551",
                        """
                                {
                                    "name": "John",
                                    "email": "john@com.com"
                                }""")
                .seeResponseCodeIs(200);

        mockCustomApi.verifyMockCustom(
                "verify POST mock",
                "data/mocks/verify/post_v_1.json");
    }

    @Test
    @Description("""
            Create POST mock with error code
            -> POST request
            -> Verify POST request""")
    void checkPost_withError() {

        final var id = "1";
        final var name = "Alex";
        final var email = "alex@com.com";

        mockCreate.postUser_error(id, 500);

        api.postUser(id, name, email)
                .seeResponseCodeIs(500);

        mockVerify.postUser(id, 1);
    }

    @Test
    @Description("""
            Example of getBaseRequestsList() method report""")
    void checkGetMockRequestsList() {

        final var id = "1";
        final var name = "Alex";
        final var email = "alex@com.com";

        mockCreate.postUser_error(".*.", 200);
        mockCreate.getUser_error(".*.", 200);

        api.postUser(id, name, email)
                .seeResponseCodeIs(200);
        api.getUser(id)
                .seeResponseCodeIs(200);
        api.postUser(id, name, email)
                .seeResponseCodeIs(200);

        mockCustomApi.assertAllMocksCount(3);

        mockCustomApi.showBaseRequestsList();
    }

    @Test
    @Description("""
            Example of getBaseRequestsList() method report empty""")
    void checkGetMockRequestsList_empty() {

        mockCustomApi.assertAllMocksCount(0);

        mockCustomApi.showBaseRequestsList();
    }

    @Test
    @Description("""
           Grab data from response body""")
    void grabFromBody() {

        mockCreate.getUser_error(".*.", 500);

        String message = api.getUser("1")
                .seeResponseCodeIs(500)
                .grabStringFromResponseByPath("error");

        reporter("Grab String: " + message);

        String messageId = api.getUser("2")
                .seeResponseCodeIs(500)
                .grabStringFromResponseByPath("errorId");

        reporter("Grab like String: " + messageId);

        var messageIdInt= api.getUser("2")
                .seeResponseCodeIs(500)
                .grabDataFromResponseByPath("errorId");

        reporter("Grab like Integer (convert to String or concatenate for report): " + messageIdInt);
    }

    @Test
    @Description("""
            several requests
            -> verifyMockSequence pass""")
    void verifyMockSequenceError() {

        final var id = "1";
        final var name = "Alex";
        final var email = "alex@com.com";

        mockCreate.postUser(id, 200, name, email);
        mockCreate.getUser(id, 200, name, email);

        api.postUser(id, name, email)
                .seeResponseCodeIs(200);
        api.getUser(id)
                .seeResponseCodeIs(200);

        mockVerify.customVerifyMockSequence(
                """
                {
                   "httpRequests":[
                      {
                         "method":"POST"
                      },
                      {
                         "method":"GET"
                      }
                   ]
                }""");
    }

}

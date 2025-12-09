package rest.failed;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

@Feature("Basic failed tests")
class ApiFailedTest extends TestsSetupFallen {
    
    @Test
    @Description("""
            Create mock expectation with multiple keys
            -> check keys (two wrong & one miss)""")
    void apiResponseJson_checkBody_severalKeysFailed() {
    mockVerify.withLogging(true);
        mocksApi.createMockCustom(
                "create GET mock with many params",
                "data/mocks/expect/get_ex_many_keys_types.json");

        api.getUser("1583")
                .seeResponseCodeIs(200)
                .seeResponseContainsJson("""
                        {
                              "name": "Anna",
                              "email": "anna@mail.ua",
                              "age": 77,
                              "fee": 100.99,
                              "is_active": true,
                              "city": "Kyiv"
                        }
                        """);
    }

    @Test
    @Description("""
            Create mock expectation with multiple keys
            -> check schema failed""")
    void apiResponseJson_checkSchemaFailed() {

        mocksApi.createMockCustom(
                "create GET mock with many params",
                "data/mocks/expect/get_ex_many_keys_types.json");

        api.getUser("1583")
                .seeResponseCodeIs(200)
                .seeResponseMatchesJsonSchema(Path.of("data/schema/get_many_keys_wrong.json"));
    }

    @Test
    @Description("""
            Create GET mock
            -> Get request
            -> Verify GET request failed""")
    void mockCreate_request_verifyRequestFailed() {

        final var id = "1";
        final var name = "Alex";
        final var email = "alex@com.com";

        mockCreate.getUser(
                id,
                200,
                name,
                email);

        api.getUser(id)
                .seeResponseCodeIs(200);

        mockVerify.getUser(id, 2);
    }

    @Test
    @Description("""
            Create GET mock
            -> GET request check response contains body strict order failed""")
    void check_bodyJsonContainsStrictOrderFailed() {

        mocksApi.createMockCustom(
                "create GET mock with many params",
                "data/mocks/expect/get_ex_array_strict.json");

        api.getUser("1584")
                .seeResponseCodeIs(200)
                .seeResponseContainsJsonStrictOrder("""
                        {
                           "id": "1",
                           "list":[
                             {
                               "num": 2
                             },
                             {
                               "num": 1
                             }
                           ]
                        }""");
    }

    @Test
    @Description("""
            Create POST mock
            -> POST request
            -> Verify POST failed by body""")
    void verifyError_byBody() {

        final var id = "1";
        final var name = "Alex";
        final var email = "alex@com.com";

        mockCreate.postUser_error(id, 200);

        api.postUser(id, name, email)
                .seeResponseCodeIsSuccessful();

        mockVerify.postUserBody(id, "Wrong", email, 1);
    }

    @Test
    @Description("""
            Create POST mock
            -> POST request
            -> Verify POST failed by count""")
    void verifyError_byCount() {

        final var id = "1";
        final var name = "Alex";
        final var email = "alex@com.com";

        mockCreate.postUser_error(id, 200);

        api.postUser(id, name, email)
                .seeResponseCodeIs(200);

        mockVerify.postUser(id, 3);
    }

    @Test
    @Description("""
            Create POST mock
            -> POST request
            -> Verify POST failed by count with awaiting""")
    void verifyError_byCountWithAwaiting() {

        final var id = "1";
        final var name = "Alex";
        final var email = "alex@com.com";

        mockCreate.postUser_error(id, 200);

        api.postUser(id, name, email)
                .seeResponseCodeIs(200);

        mockVerify.postUserAwaiting(id, 3);
    }

    @Test
    @Description("""
            Create POST mock
            -> POST request
            -> Verify POST failed by method""")
    void verifyError_byMethod() {

        final var id = "1";
        final var name = "Alex";
        final var email = "alex@com.com";

        mockCreate.postUser_error(id, 200);

        api.postUser(id, name, email)
                .seeResponseCodeIs(200);

        mockVerify.getUser(id, 1);
    }

    @Test
    @Description("""
            Create POST mock
            -> POST request
            -> Verify POST failed by baseCount""")
    void verifyError_byBaseCount() {

        final var id = "1";
        final var name = "Alex";
        final var email = "alex@com.com";

        mockCreate.postUser_error(id, 200);

        api.postUser(id, name, email)
                .seeResponseCodeIs(200);

        mocksApi.assertAllMocksCount(2);
    }

    @Test
    @Description("""
            Create POST mock
            -> POST request
            -> Verify POST failed by baseCount (requests not exists)""")
    void verifyError_byBaseCountNotExist() {
        mocksApi.assertAllMocksCount(2);
    }


    @Test
    @Description("""
            several requests
            -> verifyMockSequence failed""")
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
                                 "method":"GET"
                              },
                              {
                                 "method":"POST"
                              }
                           ]
                        }""");
    }

}

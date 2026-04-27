package rest.failed;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;


@Feature("Mocks verify failed tests with enchanted report")
class MocksEnchantedFailedTest extends TestsSetupFallen {


    
    @Test
    @Description("Mock verify failed by body")
    void enchantedVerifyError_byBodyContains() {

        final var id = "1";
        final var name = "Alex";
        final var email = "alex@com.com";

        mockCreate.postUser_error(id, 200);

        api.postUser(id, name, email)
                .seeResponseCodeIs(200);

        mockVerifyEnchanted.postUserBody(id, "Wrong", email, 1);
    }

    @Test
    @Description("Mock verify failed by count with condition (not full support in version 2.3.0), body & headers check skipped")
    void enchantedVerifyError_byConditionCountWithSkipped() {

        final var id = "1";
        final var name = "Alex";
        final var email = "alex@com.com";

        mockCreate.postUser_error(id, 200);

        api.postUser(id, name, email)
                .seeResponseCodeIs(200);

        mockVerifyEnchanted.postUser(id, 3);
    }

    @Test
    @Description("Mock verify failed by method")
    void enchantedVerifyError_byMethod() {

        final var id = "1";
        final var name = "Alex";
        final var email = "alex@com.com";

        mockCreate.postUser_error(id, 200);

        api.postUser(id, name, email)
                .seeResponseCodeIs(200);

        mockVerifyEnchanted.getUser(id, 1);
    }

    @Test
    @Description("Mock verify failed by method and path")
    void enchantedVerifyError_byMethodAndPath() {

        final var name = "Alex";
        final var email = "alex@com.com";

        mockCreate.universalMock();

        api.postUser("2", name, email)
                .seeResponseCodeIs(200);

        api.getUser("2")
                .seeResponseCodeIs(200);

        mockVerifyEnchanted.getUser("1", 1);
    }

    @Test
    @Description("""
            Create POST mock
            -> POST request
            -> Verify POST failed by baseCount (requests not exists)""")
    void enchantedVerifyError_byBaseCountNotExist() {
        mocksApi.assertAllMocksCount(2);
    }

    @Test
    @Description("Mock verify failed by baseCount Exactly (from=to)")
    void enchantedVerifyError_byBaseCountExactly() {

        final var id = "1";
        final var name = "Alex";
        final var email = "alex@com.com";

        mockCreate.postUser_error(id, 200);

        api.postUser(id, name, email)
                .seeResponseCodeIs(200);

        mocksApi.assertAllMocksCount(2);
    }

    @Test
    @Description("Mock verify failed by baseCount From To")
    void enchantedVerifyError_byBaseCountFromTo() {

        final var id = "1";
        final var name = "Alex";
        final var email = "alex@com.com";

        mockCreate.postUser_error(id, 200);

        api.postUser(id, name, email)
                .seeResponseCodeIs(200);

        mocksApi.assertAllMocksCount(2, 3);
    }

    @Test
    @Description("Mock verify failed by baseCount From")
    void enchantedVerifyError_byBaseCountFrom() {

        final var id = "1";
        final var name = "Alex";
        final var email = "alex@com.com";

        mockCreate.postUser_error(id, 200);

        api.postUser(id, name, email)
                .seeResponseCodeIs(200);

        mockVerifyEnchanted.mockVerify("""
                {
                  "httpRequest": {
                  },
                  "times": {
                    "atLeast": 2
                  }
                }""");
    }

    @Test
    @Description("Mock verify failed by baseCount To")
    void enchantedVerifyError_byBaseCountTo() {

        final var id = "1";
        final var name = "Alex";
        final var email = "alex@com.com";

        mockCreate.postUser_error(id, 200);

        api.postUser(id, name, email)
                .seeResponseCodeIs(200);
        api.postUser(id, name, email)
                .seeResponseCodeIs(200);

        mockVerifyEnchanted.mockVerify("""
                {
                  "httpRequest": {
                  },
                  "times": {
                    "atMost": 1
                  }
                }""");
    }

    @Test
    @Description("Mock verify failed by baseCount From To validate (from>to)")
    void enchantedVerifyError_byBaseCountFromToValidate() {

        final var id = "1";
        final var name = "Alex";
        final var email = "alex@com.com";

        mockCreate.postUser_error(id, 200);

        api.postUser(id, name, email)
                .seeResponseCodeIs(200);

        mocksApi.assertAllMocksCount(4, 3);
    }

    @Test
    @Description("Mock verify failed by method and path")
    void enchantedVerifyError_byHeaders() {

        final var email = "response@com.com";

        mockCreate.universalMock();

        api.postUser("1", "Tommy", email)
                .seeResponseCodeIs(200);

        api.getUser("1")
                .seeResponseCodeIs(200);

        api.postUserHeaders("2", "Alex", email)
                .seeResponseCodeIs(200);

        mockVerifyEnchanted.mockVerify("""
                {
                  "httpRequest": {
                    "method": "POST",
                    "path": "/user/2",
                    "headers": {
                        "some_header" : ["expected_wrong_header"]
                    },
                    "body" : {
                          "name": "Alex"
                        }
                  },
                  "times": {
                    "atLeast": 1,
                    "atMost": 1
                  }
                }""");
    }


    @Test
    @Description("Mock verify failed by method and path")
    void enchantedVerifyError_byQueryParams() {

        final var email = "response@com.com";

        mockCreate.universalMock();

        api.postUser("1", "Tommy", email)
                .seeResponseCodeIs(200);

        api.getUserWithQueryParam("1", "test", "one")
                .seeResponseCodeIs(200);

        api.getUserWithQueryParam("1", "test1", "single")
                .seeResponseCodeIs(200);

        mockVerifyEnchanted.mockVerify("""
                {
                  "httpRequest": {
                    "method": "GET",
                    "path": "/user/1",
                    "queryStringParameters": {
                                "test" : ["single"]
                    }
                  },
                  "times": {
                    "atLeast": 1,
                    "atMost": 1
                  }
                }""");
    }

    @Test
    @Description("Mock verify failed by STRICT body")
    void enchantedVerifyError_byStrictBody() {

        final var email = "response@com.com";

        mockCreate.universalMock();

        api.postUser("1", "Tommy", email)
                .seeResponseCodeIs(200);

        api.getUser("1")
                .seeResponseCodeIs(200);

        api.postUserHeaders("2", "Alex", email)
                .seeResponseCodeIs(200);

        mockVerifyEnchanted.mockVerify("""
                {
                  "httpRequest": {
                    "method": "POST",
                    "path": "/user/2",
                    "body" : {
                         "type": "JSON",
                         "json": {
                              "name": "Alex"
                          },
                         "matchType": "STRICT"
                    }
                  },
                  "times": {
                    "atLeast": 1,
                    "atMost": 1
                  }
                }""");
    }

    @Test
    @Description("Mock verify failed by JSON schema")
    void enchantedVerifyError_byJsonSchema() {

        final var email = "response@com.com";

        mockCreate.universalMock();

        api.postUser("1", "Tommy", email)
                .seeResponseCodeIs(200);


        api.postUser("2", "Tommy", email)
                .seeResponseCodeIs(200);

        api.getUser("1")
                .seeResponseCodeIs(200);


        mockVerifyEnchanted.mockVerify("""
                {
                    "httpRequest": {
                        "method": "POST",
                        "body": {
                            "jsonSchema": {
                                "type": "object",
                                "properties": {
                                     "id": {
                                     "type": "integer"
                                     }
                                },
                                "additionalProperties": false
                            }
                        }
                    },
                    "times": {
                        "atLeast": 1,
                        "atMost": 1
                    }
                }""");
    }

}

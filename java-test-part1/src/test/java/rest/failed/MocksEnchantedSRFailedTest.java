package rest.failed;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;


@Feature("Mocks verify failed tests with enchanted report (Summary report)")
class MocksEnchantedSRFailedTest extends TestsSetupFallen {

    @Test
    @Description("Mock verify failed by JSON schema: for summary table report")
    void enchantedVerifyError_byJsonSchemaMoreOne() {

        final var email = "response@com.com";

        mockCreate.universalMock();
        mockCreate.testPostXml("some_xml", 200);

        api.postUser("1", "Tommy", email)
                .seeResponseCodeIs(200);

        api.postUserNoJson("6",
                        """
                                {"id_wrong": 6}""")
                .seeResponseCodeIs(200);

        //valid
        api.postUser("6",
                        """
                                {"id": 6}""")
                .seeResponseCodeIs(200);

        api.postUserString("some_string")
                .seeResponseCodeIs(200);

        api.postUserXML("some_XML")
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
                        "atLeast": 2,
                        "atMost": 2
                    }
                }""");
    }

    @Test
    @Description("Mock verify failed by JSON STRICT (atLeast/atMost): for summary table report")
    void enchantedVerifyError_byJsonStrictMoreOneToFrom() {

        final var email = "response@com.com";

        mockCreate.universalMock();
        mockCreate.testPostXml("some_xml", 200);

        api.postUser("1", "Tommy", email)
                .seeResponseCodeIs(200);

        api.postUserNoJson("5",
                        """
                                {"id": 5}""")
                .seeResponseCodeIs(200);

        //valid
        api.postUser("6",
                        """
                                {"id": 6}""")
                .seeResponseCodeIs(200);

        api.postUser("6",
                        """
                                {
                                "id": 6,
                                "text": "test"
                                }""")
                .seeResponseCodeIs(200);

        api.postUserString("some_string")
                .seeResponseCodeIs(200);

        api.postUserXML("some_XML")
                .seeResponseCodeIs(200);


        api.getUser("1")
                .seeResponseCodeIs(200);


        mockVerifyEnchanted.mockVerify("""
                {
                  "httpRequest": {
                    "method": "POST",
                    "path": "/user/6",
                    "body" : {
                         "type": "JSON",
                         "json": {
                            "id": 6
                         },
                         "matchType": "STRICT"
                    }
                  },
                  "times": {
                    "atLeast": 2,
                    "atMost": 3
                  }
                }""");
    }

    @Test
    @Description("Mock verify failed by JSON Contains(atLeast): for summary table report")
    void enchantedVerifyError_byJsonNotStrictMoreOneFrom() {

        final var email = "response@com.com";

        mockCreate.universalMock();
        mockCreate.testPostXml("some_xml", 200);

        api.postUser("1", "Tommy", email)
                .seeResponseCodeIs(200);

        api.postUserNoJson("5",
                        """
                                {"id": 5}""")
                .seeResponseCodeIs(200);

        //valid
        api.postUser("6",
                        """
                                {"id": 6}""")
                .seeResponseCodeIs(200);

        api.postUser("6",
                        """
                                {
                                "id": 6,
                                "text": "test"
                                }""")
                .seeResponseCodeIs(200);

        api.postUserString("some_string")
                .seeResponseCodeIs(200);

        api.postUserXML("some_XML")
                .seeResponseCodeIs(200);


        api.getUser("1")
                .seeResponseCodeIs(200);


        mockVerifyEnchanted.mockVerify("""
                {
                  "httpRequest": {
                    "method": "POST",
                    "path": "/user/6",
                    "body" : {
                       "id": 6
                    }
                  },
                  "times": {
                    "atLeast": 3
                  }
                }""");
    }

    @Test
    @Description("Mock verify failed by JSON Contains(atMost): for summary table report")
    void enchantedVerifyError_byJsonNotStrictMoreOneTo() {

        final var email = "response@com.com";

        mockCreate.universalMock();
        mockCreate.testPostXml("some_xml", 200);

        api.postUser("1", "Tommy", email)
                .seeResponseCodeIs(200);

        api.postUserNoJson("5",
                        """
                                {"id": 5}""")
                .seeResponseCodeIs(200);

        //valid
        api.postUser("6",
                        """
                                {"id": 6}""")
                .seeResponseCodeIs(200);

        api.postUser("6",
                        """
                                {
                                "id": 6,
                                "text": "test"
                                }""")
                .seeResponseCodeIs(200);

        api.postUserString("some_string")
                .seeResponseCodeIs(200);

        api.postUserXML("some_XML")
                .seeResponseCodeIs(200);


        api.getUser("1")
                .seeResponseCodeIs(200);


        mockVerifyEnchanted.mockVerify("""
                {
                  "httpRequest": {
                    "method": "POST",
                    "path": "/user/6",
                    "body" : {
                       "id": 6
                    }
                  },
                  "times": {
                    "atMost": 1
                  }
                }""");
    }


    @Test
    @Description("Mock verify failed by JSON Contains(default count): for summary table report")
    void enchantedVerifyError_byJsonNotStrictMoreOneDefault() {

        mockCreate.universalMock();

        //valid
        api.postUser("6",
                        """
                                {"id": 6}""")
                .seeResponseCodeIs(200);

        api.postUser("6",
                        """
                                {
                                "id": 6,
                                "text": "test"
                                }""")
                .seeResponseCodeIs(200);

        mockVerifyEnchanted.mockVerify("""
                {
                  "httpRequest": {
                    "method": "POST",
                    "path": "/user/6",
                    "body" : {
                       "id": 6
                    }
                  }
                }""");
    }

    @Test
    @Description("Mock verify failed by Method: for summary table report")
    void enchantedVerifyError_byMethodOnly() {

        final var email = "response@com.com";

        mockCreate.universalMock();
        mockCreate.testPostXml("some_xml", 200);

        api.postUser("1", "Tommy", email)
                .seeResponseCodeIs(200);

        api.postUserNoJson("5",
                        """
                                {"id": 5}""")
                .seeResponseCodeIs(200);

        //valid
        api.postUser("6",
                        """
                                {"id": 6}""")
                .seeResponseCodeIs(200);

        api.postUser("6",
                        """
                                {
                                "id": 6,
                                "text": "test"
                                }""")
                .seeResponseCodeIs(200);

        api.postUserString("some_string")
                .seeResponseCodeIs(200);

        api.postUserXML("some_XML")
                .seeResponseCodeIs(200);


        api.getUser("1")
                .seeResponseCodeIs(200);


        mockVerifyEnchanted.mockVerify("""
                {
                  "httpRequest": {
                    "method": "POST"
                  },
                  "times": {
                    "atLeast": 3,
                    "atMost": 3
                  }
                }""");
    }

    @Test
    @Description("Mock verify failed by Method & Headers: for summary table report")
    void enchantedVerifyError_byMethodHeaders() {

        final var email = "response@com.com";

        mockCreate.universalMock();
        mockCreate.testPostXml("some_xml", 200);

        api.postUser("1", "Tommy", email)
                .seeResponseCodeIs(200);

        api.postUserNoJson("5",
                        """
                                {"id": 5}""")
                .seeResponseCodeIs(200);

        //valid
        api.postUser("6",
                        """
                                {"id": 6}""")
                .seeResponseCodeIs(200);

        api.postUser("6",
                        """
                                {
                                "id": 6,
                                "text": "test"
                                }""")
                .seeResponseCodeIs(200);

        api.postUserString("some_string")
                .seeResponseCodeIs(200);

        api.postUserXML("some_XML")
                .seeResponseCodeIs(200);


        api.getUser("1")
                .seeResponseCodeIs(200);


        mockVerifyEnchanted.mockVerify("""
                {
                  "httpRequest": {
                    "method": "POST",
                    "headers": {
                        "Content-Type" : ["application/json"]
                    },
                  },
                  "times": {
                    "atLeast": 3,
                    "atMost": 3
                  }
                }""");
    }


    @Test
    @Description("Mock verify failed by All: for summary table report")
    void enchantedVerifyError_byAll() {

        final var email = "response@com.com";

        mockCreate.universalMock();
        mockCreate.testPostXml("some_xml", 200);

        api.postUser("1", "Tommy", email)
                .seeResponseCodeIs(200);

        api.postUserNoJson("5",
                        """
                                {"id": 5}""")
                .seeResponseCodeIs(200);

        //valid
        api.postUser("6",
                        """
                                {"id": 6}""")
                .seeResponseCodeIs(200);

        api.postUser("6",
                        """
                                {
                                "id": 6,
                                "text": "test"
                                }""")
                .seeResponseCodeIs(200);

        api.postUserString("some_string")
                .seeResponseCodeIs(200);

        api.postUserXML("some_XML")
                .seeResponseCodeIs(200);


        api.getUser("1")
                .seeResponseCodeIs(200);


        mockVerifyEnchanted.mockVerify("""
                {
                  "httpRequest": {
                    "method": "POST",
                    "path": "/user/6",
                    "body" : {
                       "id": 6
                    },
                    "headers": {
                        "Content-Type" : ["application/json"]
                    },
                  },
                  "times": {
                    "atLeast": 1,
                    "atMost": 1
                  }
                }""");
    }

}

package rest.positive;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;
import rest.TestsSetupApi;

import java.nio.file.Path;

import static org.hamcrest.Matchers.*;


@Feature("[rest] API & Mock checks 1")
class ApiAndMock1Test extends TestsSetupApi {


    @Test
    @Description("""
            Create GET mock
            -> Get request check response body fields
            -> (can use matchers from org.hamcrest.Matchers)
            -> Verify GET request""")
    void checkGet_mock_request_fieldsCheck_verify() {

        final var id = "1";
        final var name = "Alex";
        final var email = "alex@com.com";

        mockCreate.getUser(
                id,
                200,
                name,
                email);

        api.getUser(id)
                .seeResponseCodeIsSuccessful()
                .seeResponseBodyFieldMatch("name", is(name))
                .seeResponseBodyFieldMatch("email", is(email))
                .seeResponseBodyFieldMatch("email", endsWithIgnoringCase("COM"));

        mockVerify.getUser(id, 1);
        mockCustomApi.assertAllMocksCount(1);
    }

    @Test
    @Description("""
            Create GET mock
            -> GET request check response body full(except one key) & schema exactly""")
    void checkGet_mock_request_bodyFullCheck_schemaCheck() {

        mockCustomApi.createMockCustom(
                "create GET mock with many params",
                "data/mocks/expect/get_ex_many_keys_types.json");

        api.getUser("1583")
                .seeResponseCodeIs(200)
                .seeResponseContainsJson("""
                        {
                              "name": "Anna",
                              "email": "anna@com.com",
                              "age": 33,
                              "fee": 100.99,
                              "is_active": true
                        }
                        """)
                .seeResponseMatchesJsonSchema(Path.of("data/schema/get_many_keys.json"));
    }

    @Test
    @Description("""
            Create GET mock
            -> GET request check response body full(equal)""")
    void checkGet_mock_request_bodyEqualCheck() {

        mockCustomApi.createMockCustom(
                "create GET mock with many params",
                "data/mocks/expect/get_ex_many_keys_types.json");

        api.getUser("1583")
                .seeResponseCodeIs(200)
                .seeResponseExactlyMatchJson("""
                        {
                              "name": "Anna",
                              "email": "anna@com.com",
                              "age": 33,
                              "fee": 100.99,
                              "is_active": true,
                              "note": null
                        }
                        """);
    }

    @Test
    @Description("""
            Create GET mock
            -> GET request check response contains body strict order""")
    void check_bodyJsonContainsStrictOrder() {

        mockCustomApi.createMockCustom(
                "create GET mock with many params",
                "data/mocks/expect/get_ex_array_strict.json");

        api.getUser("1584")
                .seeResponseCodeIs(200)
                .seeResponseContainsJsonStrictOrder("""
                        {
                           "id": "1",
                           "list":[
                             {
                               "num": 1
                             },
                             {
                               "num": 2
                             }
                           ]
                        }""");
    }


    @Test
    @Description("""
            Create GET mock
            -> GET request check response equal body not strict order""")
    void check_bodyJsonEqualNoStrictOrder() {

        mockCustomApi.createMockCustom(
                "create GET mock with many params",
                "data/mocks/expect/get_ex_array_strict.json");

        api.getUser("1584")
                .seeResponseCodeIsSuccessful()
                .seeResponseExactlyMatchJsonIgnoringOrder("""
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
            Create GET mock
            -> GET request check response body from file""")
    void checkGet_mock_request_bodyContainsCheckFromFile() {

        mockCustomApi.createMockCustom(
                "create GET mock with many params",
                "data/mocks/expect/get_ex_many_keys_types.json");

        api.getUser("1583")
                .seeResponseCodeIs(200)
                .seeResponseContainsJson(Path.of("data/api/get_many_keys_check.json"));
    }

    @Test
    @Description("""
            Create GET mock with headers
            -> GET request check headers""")
    void checkGet_mock_request_headerCheck() {

        mockCustomApi.createMockCustom(
                "create GET with headers in response",
                "data/mocks/expect/get_headers.json");

        api.getUser("1")
                .seeResponseCodeIs(200)
                .seeResponseHeaderMatch("some_header_1",  is("test1"))
                .seeResponseHeaderMatch("some_header_2",  is("head/test2"))
                .seeResponseHeaderMatch("some_header_1",  containsString("test"));
    }
}

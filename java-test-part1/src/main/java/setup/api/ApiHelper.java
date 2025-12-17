package setup.api;

import net.bugreaper.modules.api.Api;
import net.bugreaper.modules.api.assertable.AssertableResponse;
import io.qameta.allure.Step;

import java.util.Map;

import static net.bugreaper.core.mappers.StringMappers.stringMapper;
import static io.restassured.http.ContentType.*;


public class ApiHelper extends Api{

    // This setup just for example of wrapped methods if you don`t want to use as is
    // in real tests no need to change content type or auth in every request
    // if it`s necessary: create multiple objects with different cases (no Auth, other content type)
    // or just clean type before each test

    private final Api apiHelper;
    private final Api apiHelperForAuth;
    private final Api apiHelperXml;
    private final Api apiHelperText;

    private static ApiHelper INSTANCE;

    private ApiHelper() {
        this.apiHelper = new Api(); //default JSON
        this.apiHelperForAuth = new Api(); //for different Auth cases
        this.apiHelperXml = new Api().withContentTypeXml(); //For XML
        this.apiHelperText = new Api().withContentType(TEXT); //For txt
    }

    public static ApiHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ApiHelper();
        }

        return INSTANCE;
    }

    @Step("POST Test task start")
    public AssertableResponse taskTestStart(String user, String pass) {

        return apiHelperForAuth.setBasicAuth(user, pass)
                .sendPost("task/test/start");
    }

    @Step("GET user API")
    public AssertableResponse getUser(String userId) {

        return apiHelper
                .sendGet("user/" + userId);
    }

    @Step("GET user API")
    public AssertableResponse getUserWithAuth(String user, String pass, String userId) {

        return apiHelperForAuth.setBasicAuth(user, pass)
                .sendGet("user/" + userId);
    }

    @Step("GET user API")
    public AssertableResponse getUserNoAuth(String userId) {

        return apiHelper
                .sendGet("user/" + userId);
    }


    @Step("POST user API")
    public AssertableResponse postUser(String userId, String body) {

        return apiHelper.setHeader("Content-Type", "application/json; charset=utf-8")
                .sendPost("user/" + userId, body);

    }

    @Step("POST user API no JSON header")
    public AssertableResponse postUserNoJson(String userId, String body) {

        return apiHelper
                .sendPost("user/" + userId, body);
    }

    @Step("POST user API string")
    public AssertableResponse postUserString(String body) {

        return apiHelperText
                .sendPost("user-string", body);
    }

    @Step("POST user API XML")
    public AssertableResponse postUserXML(String data) {

        return apiHelperXml
                .sendPost("user-string", "<response> <key>" + data + "</key> </response>");
    }

    @Step("POST user API")
    public AssertableResponse postUser(String userId, String name, String email) {

        return apiHelper
                .sendPost("user/" + userId,
                        stringMapper("""
                                        {
                                            "name": "${name}",
                                            "email": "${email}"
                                        }""",
                                Map.of(
                                        "name", name,
                                        "email", email
                                )));
    }

    @Step("POST user API")
    public AssertableResponse postUserHeaders(String userId, String name, String email) {

        return apiHelper
                .setHeader("some_header", "actual_header")
                .sendPost("user/" + userId,
                        stringMapper("""
                                        {
                                            "name": "${name}",
                                            "email": "${email}"
                                        }""",
                                Map.of(
                                        "name", name,
                                        "email", email
                                )));

    }

    @Step("GET XML pet API")
    public AssertableResponse getPet(String petId) {

        return apiHelperXml
                .sendGet("pet/" + petId);
    }

}

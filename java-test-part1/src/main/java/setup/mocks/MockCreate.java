package setup.mocks;

import io.bugreaper.modules.mocks.MocksApi;
import io.qameta.allure.Step;

import java.util.Map;

import static io.bugreaper.core.mappers.StringMappers.stringMapper;


public class MockCreate {

    //No extend - create & use wrapped methods
    //but can`t use basic methods in tests

    final MocksApi mocksApi = new MocksApi();

    @Step("[MOCK-CREATE]: universal mock")
    public void universalMock() {

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

    public void testPostXml(String data, int statusCode) {

        mocksApi.createMock(stringMapper("""
                {
                    "httpRequest": {
                        "method": "POST",
                        "path": "/user-xml",
                        "body": {
                            "type": "XML",
                            "xml": "<response> <key>${data}</key> </response>"
                        }
                    },
                    "httpResponse": {
                        "statusCode": ${statusCode},
                        "headers": {
                            "content-type": [
                                "application/xml"
                            ]
                        },
                        "body": "<response><id>285</id><status>ok</status></response>"
                    }
                }""",
                Map.of(
                        "data", data,
                        "statusCode", statusCode
                )));
    }

    @Step("[MOCK-CREATE]: 'getUser' [GET /user/{userId}]")
    public void getUser(String userId, int statusCode, String name, String email) {

        mocksApi.createMock(stringMapper("""
                        {
                            "httpRequest": {
                                "method": "GET",
                                "path": "/user/${userId}"
                            },
                            "httpResponse": {
                                "statusCode": ${statusCode},
                                "body": {
                                    "name": "${name}",
                                    "email": "${email}"
                                }
                            }
                        }""",
                Map.of(
                        "userId", userId,
                        "statusCode", statusCode,
                        "name", name,
                        "email", email
                )));
    }

    @Step("[MOCK-CREATE]: 'postUser' [GET /user/{userId}] with error code:{statusCode}")
    public void getUser_error(String userId, int statusCode) {

        mocksApi.createMock(stringMapper("""
                        {
                            "httpRequest": {
                                "method": "GET",
                                "path": "/user/${userId}"
                            },
                            "httpResponse": {
                                "statusCode": ${statusCode},
                                "body": {
                                    "error": "Dummy Get error"
                                }
                            }
                        }""",
                Map.of(
                        "userId", userId,
                        "statusCode", statusCode
                )));
    }

    @Step("[MOCK-CREATE]: 'postUser' [POST /user/{userId}]")
    public void postUser(String userId, int statusCode, String name, String email) {

        mocksApi.createMock(stringMapper("""
                        {
                            "httpRequest": {
                                "method": "POST",
                                "path": "/user/${userId}"
                            },
                            "httpResponse": {
                                "statusCode": ${statusCode},
                                "body": {
                                    "name": "${name}",
                                    "email": "${email}"
                                }
                            }
                        }""",
                Map.of(
                        "userId", userId,
                        "statusCode", statusCode,
                        "name", name,
                        "email", email
                )));
    }

    @Step("[MOCK-CREATE]: 'postUser' [POST /user/{userId}] with error code:{statusCode}")
    public void postUser_error(String userId, int statusCode) {

        mocksApi.createMock(stringMapper("""
                        {
                            "httpRequest": {
                                "method": "POST",
                                "path": "/user/${userId}"
                            },
                            "httpResponse": {
                                "statusCode": ${statusCode},
                                "body": {
                                    "error": "Dummy error"
                                }
                            }
                        }""",
                Map.of(
                        "userId", userId,
                        "statusCode", statusCode
                )));
    }

    @Step("[MOCK-CREATE]: 'postTask' [POST api/admin/task/test/start]")
    public void postTask(String userId, int statusCode, String name, String email) {

        mocksApi.createMock(stringMapper("""
                        {
                            "httpRequest": {
                                "method": "POST",
                                "path": "/api/admin/task/test/start"
                            },
                            "httpResponse": {
                                "statusCode": ${statusCode},
                                "body": "ok"
                            }
                        }""",
                Map.of(
                        "userId", userId,
                        "statusCode", statusCode,
                        "name", name,
                        "email", email
                )));
    }

    @Step("[MOCK-CREATE]: 'getPetXml' [GET /pet/{petId}]}")
    public void getPetXml(String petId,
                          int statusCode,
                          String name,
                          int age,
                          String price) {

        mocksApi.createMock(stringMapper("""
                        {
                            "httpRequest": {
                                "method": "GET",
                                "path": "/pet/${petId}"
                            },
                            "httpResponse": {
                                "statusCode": ${statusCode},
                                "headers": {
                                    "content-type": ["text/xml"]
                                },
                                "body": {
                                    "type" : "XML",
                                    "xml" : "<pet><name>${name}</name><age>${age}</age><price>${price}</price></pet>"
                                }
                            }
                        }""",
                Map.of(
                        "petId", petId,
                        "statusCode", statusCode,
                        "name", name,
                        "age", age,
                        "price", price
                )));
    }

}

package setup.mocks;

import io.bugreaper.modules.mocks.MocksApi;
import io.qameta.allure.Param;
import io.qameta.allure.Step;

import java.util.Map;

import static io.bugreaper.core.mappers.StringMappers.stringMapper;
import static io.qameta.allure.model.Parameter.Mode.HIDDEN;

public class MockVerify extends MocksApi {

    //Extend - create & use wrapped methods
    //can use basic methods in tests

    @Step("[MOCK-VERIFY]: custom mockVerify")
    public void mockVerify(@Param(mode = HIDDEN) String mockVerify) {
        verifyMock(mockVerify);
    }

    @Step("[MOCK-VERIFY]: custom mockVerify")
    public void customVerifyMockSequence(String mockVerify) {
        verifyMockSequence(mockVerify);
    }

    @Step("[MOCK-VERIFY]: getUser [GET /user/{userId}]) {receivedCount} times")
    public void getUser(String userId, int receivedCount) {

        verifyMock(stringMapper("""
                        {
                            "httpRequest": {
                                "method": "GET",
                                "path": "/user/${userId}",
                            },
                            "times": {
                                "atLeast": ${receivedCount},
                                "atMost": ${receivedCount}
                            }
                        }""",
                Map.of(
                        "userId", userId,
                        "receivedCount", receivedCount
                )));
    }

    @Step("[MOCK-VERIFY]: postUser Exactly [POST /user/{userId}]) {receivedCount} times")
    public void postUserExactly(String userId, String name, String email, int receivedCount) {

        verifyMock(stringMapper("""
                        {
                            "httpRequest": {
                                "method": "POST",
                                "path": "/user/${userId}",
                                "body": {
                                    "type": "JSON",
                                    "json": {
                                        "name": "${name}",
                                        "email": "${email}"
                                    },
                                    "matchType": "STRICT"
                                }
                            },
                            "times": {
                                "atLeast": ${receivedCount},
                                "atMost": ${receivedCount}
                            }
                        }""",
                Map.of(
                        "userId", userId,
                        "name", name,
                        "email", email,
                        "receivedCount", receivedCount
                )));
    }

    @Step("[MOCK-VERIFY]: postUser [POST /user/{userId}]) {receivedCount} times")
    public void postUser(String userId, int receivedCount) {

        verifyMock(stringMapper("""
                        {
                            "httpRequest": {
                                "method": "POST",
                                "path": "/user/${userId}"
                            },
                            "times": {
                                "atLeast": ${receivedCount},
                                "atMost": ${receivedCount}
                            }
                        }""",
                Map.of(
                        "userId", userId,
                        "receivedCount", receivedCount
                )));
    }

    @Step("[MOCK-VERIFY]: postUser [POST /user/{userId}]) {receivedCount} times with awaiting")
    public void postUserAwaiting(String userId, int receivedCount) {

        verifyMockWithAwait(stringMapper("""
                        {
                            "httpRequest": {
                                "method": "POST",
                                "path": "/user/${userId}"
                            },
                            "times": {
                                "atLeast": ${receivedCount},
                                "atMost": ${receivedCount}
                            }
                        }""",
                Map.of(
                        "userId", userId,
                        "receivedCount", receivedCount
                )));
    }

    @Step("[MOCK-VERIFY]: postUser [POST /user/{userId}]) with body {receivedCount} times")
    public void postUserBody(String userId, String name, String email, int receivedCount) {

        verifyMock(stringMapper("""
                        {
                            "httpRequest": {
                                "method": "POST",
                                "path": "/user/${userId}",
                                "body": {
                                    "name": "${name}",
                                    "email": "${email}"
                                }
                            },
                            "times": {
                                "atLeast": ${receivedCount},
                                "atMost": ${receivedCount}
                            }
                        }""",
                Map.of(
                        "userId", userId,
                        "name", name,
                        "email", email,
                        "receivedCount", receivedCount
                )));
    }

}

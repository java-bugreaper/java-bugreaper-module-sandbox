package redis.positive;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import redis.RedisSetup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Feature("Redis type: stream")
class RedisStreamTests extends RedisSetup {


    @BeforeEach
    void clean() {
        redisStream.cleanAll();
    }

    @Test
    void checkTypeTest() {

        redisStream.addRecord("users_stream",
                """
                        {"value": "user1"}""");

        redisStream.seeKeyExists("users_stream");
        redisStream.seeKeyTypeIs("users_stream", "stream");

        redisStream.addRecord("users_stream2:admins",
                """
                        {"value": "test2"}""");
        assertTrue(redisStream.getKeyExistsStatus("users_stream2:admins"));
        assertEquals("stream", redisStream.getKeyType("users_stream2:admins"));

        //not a key - directory
        assertEquals("none", redisStream.getKeyType("users_stream2"));
    }


    @Test
    void createDeleteCountTests() {

        redisStream.addRecord("users_stream:admins",
                """
                        {"value": "admin1"}""");
        redisStream.addRecord("users_stream:admins",
                """
                        {"value": "admin2"}""");
        redisStream.addRecord("users_stream:admins",
                """
                        {"value": "admin1"}""");

        redisStream.addRecord("users_stream:developers",
                """
                        {"value": "developer1"}""");

        assertEquals(3, redisStream.getRecordsCountByKey("users_stream:admins"));
        assertEquals(2, redisStream.getKeysCountByPattern("users_stream:*"));


        redisStream.seeRecordsCountByKeyIsExactly("users_stream:admins", 3);
        redisStream.seeRecordsCountByKeyIsExactly("users_stream:developers", 1);

        redisStream.seeKeysCountByPatternIsExactly("users_stream:*", 2);
        redisStream.seeKeysCountByPatternIsGreaterThan("users_stream:*", 1);
        redisStream.seeKeysCountByPatternIsLessThan("users_stream:*", 3);

        redisStream.seeKeysCountByPatternIsExactly("*", 2);
        redisStream.seeKeysCountByPatternIsExactly("users_stream:admins", 1);

        redisStream.deleteKeysByPattern("users_stream:admins");
        redisStream.seeRecordsCountByKeyIsExactly("users_stream:admins", 0);
        redisStream.seeRecordsCountByKeyIsExactly("users_stream:developers", 1);

        redisStream.deleteKeysByPattern("users_stream:*");
        redisStream.seeRecordsCountByKeyIsExactly("users_stream:developers", 0);
    }


    @Test
    void ttlTests() {

        redisStream.seeRecordsCountByKeyIsExactly("users_stream:ttl", 0);

        redisStream.addRecord("users_stream:ttl",
                """
                        {"value": "test1"}""");


        redisStream.addRecord("users_stream:ttl",
                """
                        {"value": "test2"}""", 600);

        redisStream.seeTtlIsGreaterThan("users_stream:ttl", 500);
        redisStream.seeTtlIsLessThan("users_stream:ttl", 601);
    }

    @Test
    void grabStreamRecordsTests() {


        redisStream.addRecord("users_stream:grab", """
                {
                  "id": 2,
                  "name": "John"
                }
                """, 500);
        redisStream.addRecord("users_stream:grab", """
                {
                  "id": 1,
                  "name": "Alex"
                }
                """);


        redisStream.grabRecordsByKey("users_stream:grab").
                seeListHasExactlyCount(2)
                .seeListAnyContainsJson("""
                        {
                          "name": "Alex"
                        }
                        """)
                .seeListAnyEqualsJson("""
                        {
                          "id": "1",
                          "name": "Alex"
                        }
                        """);
    }

}

package redis.positive;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.RedisSetup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Feature("Redis type: hash")
class RedisHashTests extends RedisSetup {


    @BeforeEach
    void clean() {
        redisHash.cleanAll();
    }


    @Test
    void checkTypeTest() {

        redisHash.addRecord("users_hash:1", "test1");
        redisHash.addRecord("users_hash:two", "test2");

        redisHash.seeKeyExists("users_hash");
        redisHash.seeKeyTypeIs("users_hash", "hash");


        redisHash.addRecord("users_hash:admins:2", "test2");
        assertTrue(redisHash.getKeyExistsStatus("users_hash:admins"));
        assertEquals("hash", redisHash.getKeyType("users_hash:admins"));

        //not a key - value
        assertEquals("none", redisHash.getKeyType("users_hash:admins:2"));
    }

    @Test
    void ttlTests() {

        redisHash.addRecord("users_hash:ttl", "test1");

        redisHash.addRecord("users_hash:ttl", "test1", 600);

        redisHash.seeTtlIsGreaterThan("users_hash", 500);
        redisHash.seeTtlIsLessThan("users_hash", 601);

    }

    @Test
    void createDeleteCountTests() {

        redisHash.addRecord("users_hash:admins:1", "admin1");
        redisHash.addRecord("users_hash:admins:2", "admin2");
        redisHash.addRecord("users_hash:admins:3", "admin3");

        redisHash.addRecord("users_hash:developers:1", "developer1");

        assertEquals(3, redisHash.getRecordsCountByKey("users_hash:admins"));

        assertEquals(2, redisHash.getKeysCountByPattern("users_hash:*"));

        redisHash.seeRecordsCountByKeyIsExactly("users_hash:admins", 3);
        redisHash.seeRecordsCountByKeyIsExactly("users_hash:developers", 1);

        redisHash.seeKeysCountByPatternIsExactly("users_hash:*", 2);
        redisHash.seeKeysCountByPatternIsGreaterThan("users_hash:*", 1);
        redisHash.seeKeysCountByPatternIsLessThan("users_hash:*", 3);

        redisHash.seeKeysCountByPatternIsExactly("*", 2);
        redisHash.seeKeysCountByPatternIsExactly("users_hash:admins", 1);

        redisHash.deleteKeysByPattern("users_hash:admins");
        redisHash.seeRecordsCountByKeyIsExactly("users_hash:admins", 0);
        redisHash.seeRecordsCountByKeyIsExactly("users_hash:developers", 1);

        redisHash.deleteKeysByPattern("users_hash:*");
        redisHash.seeRecordsCountByKeyIsExactly("users_hash:developers", 0);
    }

    @Test
    void grabHashRecordsTests() {

        redisHash.addRecord("users:1", "test1", 800);
        redisHash.addRecord("users:2", """
                {
                  "id": 1,
                  "name": "Alex"
                }
                """);

        redisHash.grabRecordsByKey("users").
                seeListHasExactlyCount(2)
                .seeListAnyEquals("test1")
                .seeListAnyContainsJson("""
                        {
                          "name": "Alex"
                        }
                        """);

    }
}

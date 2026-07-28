package redis.positive;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.RedisSetup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Feature("Redis type: set")
class RedisSetTests extends RedisSetup {


    @BeforeEach
    void clean() {
        redisSet.cleanAll();
    }


    @Test
    void checkTypeTest() {

        redisSet.addRecord("users_set", "test1");

        redisSet.seeKeyExists("users_set");
        redisSet.seeKeyTypeIs("users_set", "set");

        redisSet.addRecord("users_set2:admins", "test2");
        assertTrue(redisSet.getKeyExistsStatus("users_set2:admins"));
        assertEquals("set", redisSet.getKeyType("users_set2:admins"));

        //not a key - directory
        assertEquals("none", redisSet.getKeyType("users_set2"));
    }


    @Test
    void createDeleteCountTests() {

        redisSet.addRecord("users_set:admins", "admin1");
        redisSet.addRecord("users_set:admins", "admin2");
        redisSet.addRecord("users_set:admins", "admin1"); //duplicate ignored
        redisSet.addRecord("users_set:admins", "admin3");

        redisSet.addRecord("users_set:developers", "developer1");

        assertEquals(3, redisSet.getRecordsCountByKey("users_set:admins"));
        assertEquals(2, redisSet.getKeysCountByPattern("users_set:*"));


        redisSet.seeRecordsCountByKeyIsExactly("users_set:admins", 3);
        redisSet.seeRecordsCountByKeyIsExactly("users_set:developers", 1);

        redisSet.seeKeysCountByPatternIsExactly("users_set:*", 2);
        redisSet.seeKeysCountByPatternIsGreaterThan("users_set:*", 1);
        redisSet.seeKeysCountByPatternIsLessThan("users_set:*", 3);

        redisSet.seeKeysCountByPatternIsExactly("*", 2);
        redisSet.seeKeysCountByPatternIsExactly("users_set:admins", 1);

        redisSet.deleteKeysByPattern("users_set:admins");
        redisSet.seeRecordsCountByKeyIsExactly("users_set:admins", 0);
        redisSet.seeRecordsCountByKeyIsExactly("users_set:developers", 1);

        redisSet.deleteKeysByPattern("users_set:*");
        redisSet.seeRecordsCountByKeyIsExactly("users_set:developers", 0);
    }


    @Test
    void ttlTests() {

        redisSet.seeRecordsCountByKeyIsExactly("users_set:ttl", 0);

        redisSet.addRecord("users_set:ttl", "test1");

        redisSet.addRecord("users_set:ttl", "test2", 600);

        redisSet.seeTtlIsGreaterThan("users_set:ttl", 500);
        redisSet.seeTtlIsLessThan("users_set:ttl", 601);
    }

    @Test
    void grabRecordsTests() {


        redisSet.addRecord("users_set:grab", "test1");
        redisSet.addRecord("users_set:grab", """
                {
                  "id": 1,
                  "name": "Alex"
                }
                """);


        redisSet.grabRecordsByKey("users_set:grab").
                seeListHasExactlyCount(2)
                .seeListAnyEquals("test1")
                .seeListAnyContainsJson("""
                        {
                          "name": "Alex"
                        }
                        """);
    }
}

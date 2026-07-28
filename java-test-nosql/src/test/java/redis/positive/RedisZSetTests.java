package redis.positive;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.RedisSetup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Feature("Redis type: zset")
class RedisZSetTests extends RedisSetup {


    @BeforeEach
    void clean() {
        redisZSet.cleanAll();
    }

    @Test
    void checkTypeTest() {

        redisZSet.addRecord("users_zset:110", "test1");
        redisZSet.addRecord("users_zset:200.55", "test2");

        redisZSet.seeKeyExists("users_zset");
        redisZSet.seeKeyTypeIs("users_zset", "zset");


        redisZSet.addRecord("users_zset:admins:2", "test2");
        assertTrue(redisZSet.getKeyExistsStatus("users_zset:admins"));
        assertEquals("zset", redisZSet.getKeyType("users_zset:admins"));

        //not a key - value
        assertEquals("none", redisZSet.getKeyType("users_zset:admins:2"));
    }

    @Test
    void ttlTests() {

        redisZSet.addRecord("users_zset:1", "test1");

        redisZSet.addRecord("users_zset:2", "test1", 600);

        redisZSet.seeTtlIsGreaterThan("users_zset", 500);
        redisZSet.seeTtlIsLessThan("users_zset", 601);

    }

    @Test
    void createDeleteCountTests() {

        redisZSet.addRecord("users_zset:admins:1", "admin1");
        redisZSet.addRecord("users_zset:admins:2", "admin2");
        redisZSet.addRecord("users_zset:admins:3", "admin3");

        redisZSet.addRecord("users_zset:developers:1", "developer1");

        assertEquals(3, redisZSet.getRecordsCountByKey("users_zset:admins"));

        assertEquals(2, redisZSet.getKeysCountByPattern("users_zset:*"));

        redisZSet.seeRecordsCountByKeyIsExactly("users_zset:admins", 3);
        redisZSet.seeRecordsCountByKeyIsExactly("users_zset:developers", 1);

        redisZSet.seeKeysCountByPatternIsExactly("users_zset:*", 2);
        redisZSet.seeKeysCountByPatternIsGreaterThan("users_zset:*", 1);
        redisZSet.seeKeysCountByPatternIsLessThan("users_zset:*", 3);

        redisZSet.seeKeysCountByPatternIsExactly("*", 2);
        redisZSet.seeKeysCountByPatternIsExactly("users_zset:admins", 1);

        redisZSet.deleteKeysByPattern("users_zset:admins");
        redisZSet.seeRecordsCountByKeyIsExactly("users_zset:admins", 0);
        redisZSet.seeRecordsCountByKeyIsExactly("users_zset:developers", 1);

        redisZSet.deleteKeysByPattern("users_zset:*");
        redisZSet.seeRecordsCountByKeyIsExactly("users_zset:developers", 0);
    }

    @Test
    void grabZSetRecordsTests() {

        redisZSet.addRecord("users:200", "test1", 800);
        redisZSet.addRecord("users:100", """
                {
                  "id": 1,
                  "name": "Alex"
                }
                """);

        redisZSet.grabRecordsByKey("users").
                seeListHasExactlyCount(2)
                .seeListAnyEquals("test1")
                .seeListAnyContainsJson("""
                        {
                          "name": "Alex"
                        }
                        """);
    }
    
}

package redis.positive;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.RedisSetup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Feature("Redis type: string")
class RedisStringTests extends RedisSetup {


    @BeforeEach
    void clean() {
        redisString.cleanAll();
    }


    @Test
    void checkTypeTest() {

        redisString.addRecord("users:1", "test1");

        redisString.seeKeyExists("users:1");
        redisString.seeKeyTypeIs("users:1", "string");

        redisString.addRecord("users:admins:2", "test2");
        assertTrue(redisString.getKeyExistsStatus("users:admins:2"));
        assertEquals("string", redisString.getKeyType("users:admins:2"));

        //not a key - directory
        assertEquals("none", redisString.getKeyType("users:admins"));
    }


    @Test
    void createDeleteCountTests() {

        redisString.addRecord("users:admins:1", "admin1");
        redisString.addRecord("users:admins:" + 2, "admin2");
        redisString.addRecord("users:admins:" + 3, "admin3");

        redisString.addRecord("users:developers:1", "developer1");

        assertEquals(1, redisString.getRecordsCountByKey("users:admins:1"));
        assertEquals(3, redisString.getKeysCountByPattern("users:admins:*"));


        redisString.seeRecordsCountByKeyIsExactly("users:admins:1", 1);
        redisString.seeRecordsCountByKeyIsExactly("users:admins:1", 1);
        redisString.seeRecordsCountByKeyIsExactly("users:developers:1", 1);

        redisString.seeKeysCountByPatternIsExactly("users:*", 4);
        redisString.seeKeysCountByPatternIsGreaterThan("users:*", 3);
        redisString.seeKeysCountByPatternIsLessThan("users:*", 5);


        redisString.seeKeysCountByPatternIsExactly("*", 4);
        redisString.seeKeysCountByPatternIsExactly("users:admins:*", 3);
        redisString.seeKeysCountByPatternIsExactly("users:admins:1", 1);

        redisString.deleteKeysByPattern("users:admins:1");
        


        redisString.seeKeysCountByPatternIsExactly("users:admins:*", 2);

        redisString.deleteKeysByPattern("users:admins:*");
        redisString.seeKeysCountByPatternIsExactly("users:admins:*", 0);
        redisString.seeRecordsCountByKeyIsExactly("users:developers:1", 1);
        

        redisString.deleteKeysByPattern("users:*");

        redisString.seeRecordsCountByKeyIsExactly("users:developers:1", 0);

    }


    @Test
    void ttlTests() {

        redisString.addRecord("users:ttl:1", "test1");

        redisString.addRecord("users:ttl:2", "test1", 600);

        redisString.seeTtlIsGreaterThan("users:ttl:2", 500);
        redisString.seeTtlIsLessThan("users:ttl:2", 601);
    }

    @Test
    void grabRecordsTests() {


        redisString.addRecord("users:grab:1", "test1");
        redisString.addRecord("users:grab:2", """
                {
                  "id": 1,
                  "name": "Alex"
                }
                """);


        redisString.grabRecordsByKey("users:grab:1").
                seeListHasExactlyCount(1)
                .seeListAnyEquals("test1");
        
    }
}

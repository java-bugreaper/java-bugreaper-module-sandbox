package redis.positive;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.RedisSetup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Feature("Redis type: list")
class RedisListTests extends RedisSetup {


    @BeforeEach
    void clean() {
        redisList.cleanAll();
    }


    @Test
    void checkTypeTest() {

        redisList.addRecord("users_list", "test1");

        redisList.seeKeyExists("users_list");
        redisList.seeKeyTypeIs("users_list", "list");

        redisList.addRecord("users_list2:admins", "test2");
        assertTrue(redisList.getKeyExistsStatus("users_list2:admins"));
        assertEquals("list", redisList.getKeyType("users_list2:admins"));

        //not a key - directory
        assertEquals("none", redisList.getKeyType("users_list2"));
    }


    @Test
    void createDeleteCountTests() {

        redisList.addRecord("users_list:admins", "admin1");
        redisList.addRecord("users_list:admins", "admin2");
        redisList.addRecord("users_list:admins", "admin3");

        redisList.addRecord("users_list:developers", "developer1");

        assertEquals(3, redisList.getRecordsCountByKey("users_list:admins"));
        assertEquals(2, redisList.getKeysCountByPattern("users_list:*"));


        redisList.seeRecordsCountByKeyIsExactly("users_list:admins", 3);
        redisList.seeRecordsCountByKeyIsExactly("users_list:developers", 1);

        redisList.seeKeysCountByPatternIsExactly("users_list:*", 2);
        redisList.seeKeysCountByPatternIsGreaterThan("users_list:*", 1);
        redisList.seeKeysCountByPatternIsLessThan("users_list:*", 3);

        redisList.seeKeysCountByPatternIsExactly("*", 2);
        redisList.seeKeysCountByPatternIsExactly("users_list:admins", 1);

        redisList.deleteKeysByPattern("users_list:admins");
        redisList.seeRecordsCountByKeyIsExactly("users_list:admins", 0);
        redisList.seeRecordsCountByKeyIsExactly("users_list:developers", 1);

        redisList.deleteKeysByPattern("users_list:*");
        redisList.seeRecordsCountByKeyIsExactly("users_list:developers", 0);
    }


    @Test
    void ttlTests() {

        redisList.seeRecordsCountByKeyIsExactly("users_list:ttl", 0);

        redisList.addRecord("users_list:ttl", "test1");

        redisList.addRecord("users_list:ttl", "test1", 600);

        redisList.seeTtlIsGreaterThan("users_list:ttl", 500);
        redisList.seeTtlIsLessThan("users_list:ttl", 601);
    }

    @Test
    void grabRecordsTests() {


        redisList.addRecord("users_list:grab", "test1");
        redisList.addRecord("users_list:grab", """
                {
                  "id": 1,
                  "name": "Alex"
                }
                """);


        redisList.grabRecordsByKey("users_list:grab").
                seeListHasExactlyCount(2)
                .seeListAnyEquals("test1")
                .seeListAnyContainsJson("""
                        {
                          "name": "Alex"
                        }
                        """);
    }

    @Test
    void createAndCheckRecordInt2Test() {

        redisList.addRecord("users_list:55", "test55_1");
        redisList.addRecord("users_list:55", "test55_2");
        redisList.addRecord("users_list:56", "test56_1");

        redisList.seeRecordsCountByKeyIsExactly("users_list:55", 2);
        redisList.seeKeysCountByPatternIsExactly("users_list:*", 2);

        redisList.deleteKeysByPattern("users_list:55");
        redisList.seeRecordsCountByKeyIsExactly("users_list:55", 0);
        redisList.seeRecordsCountByKeyIsExactly("users_list:56", 1);
    }


}

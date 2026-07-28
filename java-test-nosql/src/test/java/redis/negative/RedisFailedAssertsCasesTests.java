package redis.negative;

import io.qameta.allure.Feature;
import net.bugreaper.core.exceptions.JsonMappersException;
import net.bugreaper.modules.redis.exceptions.RedisHelperException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.RedisSetup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Feature("Redis failed asserts cases")
class RedisFailedAssertsCasesTests extends RedisSetup {


    @BeforeEach
    void clean() {
        redisList.cleanAll();
    }

    @Test
    void seeKeyTypeIsAssertTest() {
        redisString.addRecord("wrong_type:1", "admin1");

        assertThrows(AssertionError.class, () ->
                redisList.seeKeyTypeIs("wrong_type:1", "list"));
    }

    @Test
    void seeTtlAssertTest() {
        redisString.addRecord("ttl:1", "admin1", 600);

        assertThrows(AssertionError.class, () ->
                redisString.seeTtlIsGreaterThan("ttl:1", 800));

        assertThrows(AssertionError.class, () ->
                redisString.seeTtlIsLessThan("ttl:1", 500));
    }

    @Test
    void seeKeysCountByPatternIsLessThanTest() {

        redisString.addRecord("count:1", "admin1");
        redisString.addRecord("count:2", "admin1");

        assertThrows(AssertionError.class, () ->
                redisString.seeKeysCountByPatternIsLessThan("count:*", 2));

    }

}

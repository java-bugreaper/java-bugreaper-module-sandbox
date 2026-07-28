package redis.negative;

import io.qameta.allure.Feature;
import net.bugreaper.core.exceptions.JsonMappersException;
import net.bugreaper.modules.redis.exceptions.RedisHelperException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.RedisSetup;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Feature("Redis negative cases")
class RedisNegativeCasesTests extends RedisSetup {


    @BeforeEach
    void clean() {
        redisList.cleanAll();
    }

    @Test
    void grabWrongTypesTest() {
        redisString.addRecord("multiple:admins", "admin-string");

        assertThrows(AssertionError.class, () ->
                redisHash.grabRecordsByKey("multiple:admins"));
    }


    @Test
    void grabNotExistingKeysTest() {

        assertThrows(AssertionError.class, () ->
                redisHash.grabRecordsByKey("notExist"));

    }

    @Test
    void zSetWrongScoreTests() {

        assertThrows(RedisHelperException.class, () ->
                redisZSet.addRecord("users:n100", "admin1"));

    }

    @Test
    void streamNotJsonTests() {

        assertThrows(JsonMappersException.class, () ->
                redisStream.addRecord("stream_key", "admin1"));

    }

    @Test
    void hashNoKeyIdTests() {

        assertThrows(RedisHelperException.class, () ->
                redisHash.addRecord("only_key", "admin1"));

    }

    @Test
    void getTtlNoTtlTests() {

        redisList.addRecord("no-ttl:admins", "admin1");

        assertThrows(RedisHelperException.class, () ->
                redisList.seeTtlIsLessThan("no-ttl:admins", 500));

    }

}

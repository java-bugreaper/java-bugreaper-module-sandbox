package redis;

import io.qameta.allure.Epic;
import net.bugreaper.modules.redis.Redis;
import org.junit.jupiter.api.Tag;


@Tag("nosql")
@Epic("[redis] Tests for redis")
public class RedisSetup {

    // unique instance for every type
    protected Redis redisList = Redis.getInstance("list");
    protected Redis redisHash = Redis.getInstance("hash");
    protected Redis redisString = Redis.getInstance("string");

}

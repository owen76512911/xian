package info.xiancloud.redis.jedis_pool;

import info.xiancloud.cache.redis.Cache;
import info.xiancloud.cache.redis.Redis;
import info.xiancloud.plugin.support.cache.api.CacheListUtil;
import info.xiancloud.plugin.thread_pool.ThreadPoolManager;
import info.xiancloud.plugin.util.LOG;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class JedisPoolTest {

    @Before
    public void initialize() {

    }

    @After
    public void finish() {

    }

    @Test
    public void numActive() {
        ThreadPoolManager.execute(() -> {
            for (int i = 0; i < 500; i++) {
//                ThreadPoolManager.scheduleWithFixedDelay(() -> {
//                    CacheListUtil.length("LIST_API");
//                }, 10);

                CacheListUtil.length("LIST_API");
            }
        });

        ThreadPoolManager.scheduleWithFixedDelay(() -> {
            Map<String, Cache> CACHE = Redis.unmodifiableCache();
            for (Map.Entry<String, Cache> entry : CACHE.entrySet()) {
                LOG.info(entry.getValue().getNumActive());
            }
        }, 5 * 100);

        try {
            Thread.sleep(10 * 60 * 1000);
        } catch (Exception e) {

        }
    }

}

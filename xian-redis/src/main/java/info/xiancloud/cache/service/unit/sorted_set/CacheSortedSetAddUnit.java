package info.xiancloud.cache.service.unit.sorted_set;

import info.xiancloud.cache.redis.Redis;
import info.xiancloud.cache.redis.util.FormatUtil;
import info.xiancloud.cache.service.CacheGroup;
import info.xiancloud.plugin.*;
import info.xiancloud.plugin.message.UnitResponse;
import info.xiancloud.plugin.message.UnitRequest;
import info.xiancloud.plugin.support.cache.CacheConfigBean;

/**
 * Sorted Set Add
 * <p>
 * http://doc.redisfans.com/sorted_set/zadd.html
 *
 * @author John_zero
 */
public class CacheSortedSetAddUnit implements Unit {
    @Override
    public String getName() {
        return "cacheSortedSetAdd";
    }

    @Override
    public Group getGroup() {
        return CacheGroup.singleton;
    }

    @Override
    public UnitMeta getMeta() {
        return UnitMeta.create("Sorted Set Add").setPublic(false);
    }

    @Override
    public Input getInput() {
        return new Input()
                .add("key", String.class, "", REQUIRED)
                .add("score", Long.class, "", REQUIRED)
                .add("member", Object.class, "", REQUIRED)
                .add("cacheConfig", CacheConfigBean.class, "", NOT_REQUIRED);
    }

    @Override
    public UnitResponse execute(UnitRequest msg) {
        String key = msg.get("key", String.class);
        Long score = (Long) msg.getArgMap().get("score");
        Object member = msg.getArgMap().get("member");
        CacheConfigBean cacheConfigBean = msg.get("cacheConfig", CacheConfigBean.class);

        try {
            Long result = Redis.call(cacheConfigBean, jedis -> jedis.zadd(key, score, FormatUtil.formatValue(member)));
            return UnitResponse.success(result);
        } catch (Throwable e) {
            return UnitResponse.exception(e);
        }
    }

}

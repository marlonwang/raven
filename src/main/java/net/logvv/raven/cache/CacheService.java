package net.logvv.raven.cache;

import net.logvv.raven.common.constant.CacheKeyPrefixConst;
import net.logvv.raven.push.hwpush.huawei.common.AccessToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * CacheService(对缓存的增删改查)
 * 
 * @author lvyongwen
 * @date 2015年9月15日 下午12:58:42
 * @version [1.0, 2015年9月15日]
 * @since version 1.0
 * @see [相关类/方法]
 * 
 * redisCache1101
 *  0：位标识工厂序号：工厂主要用于区分 主机、端口、库等
 *  1：位标识模板序号：模板主要用于区分 事务、缓存序列化等
 *  2、3：位标识缓存序号：缓存时间：前缀等
 * 
 * redisCache1101 :缓存授权token —— 24h
 */
@Service
public class CacheService
{
    @Resource(name = "commonCacheManager")
    private RedisCacheManager commonCacheManager;

    /** 缓存huawei accessToken */
    @CachePut(value="redisCache1101", keyGenerator = "cacheKeyGenerator")
    public AccessToken cacheHuaweiAK(AccessToken ak, String prefixConst)
    {
        return ak;
    }

    /** 查询huawei accessToken */
    public AccessToken getHuaweiAK()
    {
        Cache redisCache1101 = commonCacheManager.getCache("redisCache1101");
        @SuppressWarnings("unchecked")
        RedisTemplate<String, AccessToken> template = (RedisTemplate<String, AccessToken>)redisCache1101.getNativeCache();
        Set<String> keys = template.keys(CacheKeyPrefixConst.HW_ACCESS_TOKEN);

        if (null == keys || keys.isEmpty())
        {
            return null;
        }

        return redisCache1101.get(keys.iterator().next(), AccessToken.class);
    }

    /** 清除huawei accessToken */
    @CacheEvict(value = "redisCache1101", keyGenerator = "cacheKeyGenerator")
    public void evictHuaweiAK(String prefixConst)
    {
    }

}

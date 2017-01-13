package net.logvv.raven.cache;

/**
 * Author: wangwei
 * Created on 2017/1/13 10:33.
 */
import java.lang.reflect.Method;
import java.util.Locale;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Service;

@Service
public class CacheKeyGenerator implements KeyGenerator
{
    /** 根据传入参数自动拼接cache 存储key */
    @Override
    public Object generate(Object manager, Method method, Object... params)
    {
        if (ArrayUtils.isEmpty(params)){
            return null;
        }
        // 移除对象参数
        int i = 0;
        if (params.length > 1)
        {
            if(params[0] instanceof Integer || params[0] instanceof String || params[0] instanceof Float
                    || params[0] instanceof Double || params[0] instanceof Character || params[0] instanceof Byte
                    || params[0] instanceof Boolean || params[0] instanceof Short || params[0] instanceof Long )
            {
                i = 0; // do nothing
            }else {
                i = 1;
            }
        }
        StringBuffer sb = new StringBuffer();
        for (; i < params.length; i++)
        {
            sb.append(params[i]);
            if ((params.length - i) > 1){
                sb.append('_');
            }
        }

        return sb.toString().toUpperCase(Locale.US);
    }

    /** 生成缓存key */
    public static Object generate(Object... params)
    {
        if (ArrayUtils.isEmpty(params))
        {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < params.length; i++)
        {
            sb.append(params[i]);
            if ((params.length - i) > 1){
                sb.append('_');
            }
        }
        // key全部采用大写
        return sb.toString().toUpperCase(Locale.US);
    }
}


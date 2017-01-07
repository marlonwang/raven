package net.logvv.raven.utils;

import net.logvv.raven.common.constant.DateConstants;
import net.logvv.raven.common.exception.RestInvocationException;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * rest客户端工具类<br>
 * @author fanyaowu
 * @data 2014年7月11日
 * @version 1.0.0
 *
 */
public final class RestServiceUtils
{
    
    private RestServiceUtils()
    {
        
    }
    
    // spring的rest客户端模板
    private static final RestTemplate restTemplate = new RestTemplate();
    
    /**
     * 通过url和参数发起http的get的请求，适合单个参数的场景<br>
     * @return T
     * @Author fanyaowu
     * @data 2014年7月11日
     * @exception
     * @version
     *
     */
    public static <T> T doGet(String restPath, Class<T> responseType, String paramKey, String paramValue)
        throws RestInvocationException
    {
        
        try
        {
            StringBuffer sb = new StringBuffer(DateConstants.DEFAULT_STRINGBUFFER_LENGTH);
            
            sb.append(restPath).append('?').append(paramKey).append('=').append(paramValue);
            
            return restTemplate.getForObject(sb.toString(), responseType);
            
        }
        catch (Exception e)
        {
            throw new RestInvocationException("Failed to request get url: " + restPath, e);
        }
    }
    
    /**
     * 通过urlhttp的get的请求，适合无参的场景<br>
     * @return T
     * @Author fanyaowu
     * @data 2014年7月11日
     * @exception
     * @version
     *
     */
    public static <T> T doGet(String restPath, Class<T> responseType)
        throws RestInvocationException
    {
        
        try
        {
            return restTemplate.getForObject(restPath, responseType);
            
        }
        catch (Exception e)
        {
            
            throw new RestInvocationException("Failed to request get url: " + restPath, e);
        }
    }
    
    /**
     * 通过urlhttp的get的请求，适合无参的场景<br>
     * @param restPath
     * @return T
     * @Author fanyaowu
     * @data 2014年7月11日
     * @exception
     * @version
     *
     */
    public static <T> T doGet(String restPath, Class<T> responseType, MediaType mediaType)
        throws RestInvocationException
    {
        
        try
        {
            ClientHttpRequestInterceptor requestInterceptor = new AcceptHeaderHttpRequestInterceptor(mediaType);
            
            restTemplate.setInterceptors(Collections.singletonList(requestInterceptor));
            
            return restTemplate.getForObject(restPath, responseType);
            
        }
        catch (Exception e)
        {
            throw new RestInvocationException("Failed to request get url: " + restPath, e);
        }
    }
    
    /**
     * 通过url和参数发起http的get的请求，适合多个参数的场景<br>
     * @return T
     * @Author fanyaowu
     * @data 2014年7月11日
     * @exception
     * @version
     *
     */
    public static <T> T doGet(String restPath, Class<T> responseType, Map<String, String> requestParamMap)
        throws RestInvocationException
    {
        try
        {
            StringBuffer sb = new StringBuffer(DateConstants.DEFAULT_STRINGBUFFER_LENGTH);
            
            sb.append(restPath).append('?');
            
            // 遍历参数填充http 请求键值对
            for (Iterator<String> it = requestParamMap.keySet().iterator(); it.hasNext();)
            {
                String paramKey = it.next();
                
                String paramValue = requestParamMap.get(paramKey);
                
                sb.append(paramKey).append('=').append(paramValue);
                
                if (it.hasNext())
                {
                    sb.append('&');
                }
                
            }
            
            return restTemplate.getForObject(sb.toString(), responseType);
            
        }
        catch (Exception e)
        {
            
            throw new RestInvocationException("Failed to request get url: " + restPath, e);
        }
        
    }
    
    /**
     * 提交post请求<br>
     *
     * @param restPath
     * @param requestParam
     * @param responseType
     * @return T
     * @Author fanyaowu
     * @data 2014年7月11日
     * @exception
     * @version
     *
     */
    public static <T> T doPost(String restPath, Object requestParam, Class<T> responseType)
        throws RestInvocationException
    {
        try
        {
            return restTemplate.postForObject(restPath, requestParam, responseType);
        }
        catch (Exception e)
        {
            throw new RestInvocationException("Failed to invoke rest: " + restPath, e);
        }
        
    }
    
    /**
     * http的put请求方法<br>
     * @param restPath
     * @param requestParam
     * @throws RestInvocationException void
     * @Author fanyaowu
     * @data 2014年7月11日
     * @exception
     * @version
     *
     */
    public static void doPut(String restPath, Object requestParam)
        throws RestInvocationException
    {
        try
        {
            
            restTemplate.put(restPath, requestParam);
            
        }
        catch (Exception e)
        {
            
            throw new RestInvocationException("Failed to request get url: " + restPath, e);
        }
    }
    
    /**
     * 调用http的删除方法<br>
     * @param restPath
     * @throws RestInvocationException void
     * @Author fanyaowu
     * @data 2014年7月11日
     * @exception
     * @version
     *
     */
    public static void doDel(String restPath)
        throws RestInvocationException
    {
        try
        {
            
            restTemplate.delete(restPath);
            
        }
        catch (Exception e)
        {
            
            throw new RestInvocationException("Failed to request get url: " + restPath, e);
        }
    }
    
    public static void doDelete(String restPath, Map<String, String> params)
        throws RestInvocationException
    {
        try
        {
            StringBuffer sb = new StringBuffer(DateConstants.DEFAULT_STRINGBUFFER_LENGTH);
            // 1. 添加？号
            if (params.size() > 0)
            {
                sb.append(restPath).append("?");
            }
            else
            {
                doDel(restPath);
                return;
            }
            // 拼接URI
            for (Iterator<String> it = params.keySet().iterator(); it.hasNext();)
            {
                String key = it.next();
                // 2. 添加参数
                sb.append(key).append("=").append(params.get(key));
                // 3. 判断是否是最后 一个
                
                if (it.hasNext())
                {
                    sb.append("&");
                }
            }
            restTemplate.delete(sb.toString());
        }
        catch (Exception e)
        {
            throw new RestInvocationException("Failed to request get url: " + restPath, e);
        }
        
    }
    
    public static void doPut(String restPath, Map<String, String> params)
        throws RestInvocationException
    {
        
        StringBuffer urlTemplate = new StringBuffer(DateConstants.DEFAULT_STRINGBUFFER_LENGTH);
        urlTemplate.append(restPath);
        try
        {
            if (params.size() > 0)
            {
                urlTemplate.append('?');
                for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext();)
                {
                    String key = iterator.next();
                    urlTemplate.append(key).append('=').append(params.get(key));
                    
                    if (iterator.hasNext())
                    {
                        urlTemplate.append('&');
                    }
                }
            }
            restTemplate.put(new URI(urlTemplate.toString()), null);
        }
        catch (Exception e)
        {
            throw new RestInvocationException("Failed to request get url: " + restPath, e);
        }
        
    }
    
}

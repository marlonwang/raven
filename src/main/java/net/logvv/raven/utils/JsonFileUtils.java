package net.logvv.raven.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonFileUtils
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonFileUtils.class);
    
    // 从给定位置读取Json文件
    /**
     * readJson(这里用一句话描述这个方法的作用)
     * (这里描述这个方法适用条件 – 可选)
     * @param path
     * @return
     * @return String  返回类型 
     * @author 1enny
     * @date 2016年3月1日 下午5:59:47 
     * @version  [1.0, 2016年3月1日]
     * @since  version 1.0
    */
    public static JsonNode readJson(String path)
    {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        
        if (inputStream == null)
        {
            return null;
        }
        
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        
        String tempString = null;
        String laststr = "";
        try
        {
            while ((tempString = bufferedReader.readLine()) != null)
            {
                laststr += tempString;
            }
        }
        catch (IOException e)
        {
            LOGGER.error("Read json file occur exception !");
        }
        finally
        {
            try
            {
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return JsonUtils.json2node(laststr);
    }
}

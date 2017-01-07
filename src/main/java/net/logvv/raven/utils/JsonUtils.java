package net.logvv.raven.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * 
 * json工具类<br>
 * 取消日志打印，造成垃圾日志大量堆积
 * @author fanyaowu
 * @data 2014年7月9日
 * @version 1.0.0
 *
 */
public final class JsonUtils
{

	// 私有构造
	private JsonUtils()
	{

	}

	private static ObjectMapper objectMapper = null;
	
	static
	{
		// 将objectMapper 设置为全局静态缓存，提高调用效率
		objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
	}

	
	

	/**
	 * 
	 * 供外部调用OjbectMapper对象<br>
	 *
	 * @return ObjectMapper
	 * @Author fanyaowu
	 * @data 2014年7月9日
	 * @exception
	 * @version
	 *
	 */
	public static ObjectMapper getObjectMapper()
	{

		return objectMapper;
	}

	/**
	 * 
	 * 将对象转为angular json串<br>
	 *
	 * @param obj,string
	 * @return String
	 * @Author wangbingbing
	 * @data 2014年11月10日
	 * @exception
	 * @version
	 *
	 */
	public static String obj2angular(Object obj,String callback){
		
		String result = obj2json(obj);
		
		if(callback !=null && callback.length() > 0 && result!=null){
			return callback + "(" + result + ")";
		}
		
		return result;
	}
	
	/**
	 * 
	 * 将对象转为json串<br>
	 *
	 * @param obj
	 * @return String
	 * @Author fanyaowu
	 * @data 2014年7月9日
	 * @exception
	 * @version
	 *
	 */
	public static String obj2json(Object obj)
	{

		if (obj == null)
		{
			return null;
		}

		String jsonResult = null;

		try
		{
			jsonResult = objectMapper.writeValueAsString(obj);
		}
		catch (JsonProcessingException e)
		{

		}

		return jsonResult;
	}

	/**
	 * 
	 * 从json串中获取某key对应的值<br>
	 *
	 * @param jsonSrc
	 * @param jsonKey
	 * @return
	 * String
	 * @Author fanyaowu
	 * @data 2014年7月16日
	 * @exception 
	 * @version
	 *
	 */
	public static String getJsonValue(String jsonSrc, String jsonKey)
	{
		if (StringUtils.isEmpty(jsonSrc) || StringUtils.isEmpty(jsonKey))
		{
			return null;
		}
		JsonNode node = JsonUtils.json2obj(jsonSrc, JsonNode.class);
		
		if(node == null)
		{
			return null;
		}

		// 获取jsonKey数据
		JsonNode dataNode = node.get(jsonKey);

		if (null == dataNode)
		{
			return null;
		}

		return dataNode.toString();
	}
	
	/**
	 * 
	 * 从json串中获取某key对应的值<br>
	 *
	 * @param jsonSrc
	 * @param jsonKey
	 * @return
	 * String
	 * @Author fanyaowu
	 * @data 2014年7月16日
	 * @exception 
	 * @version
	 *
	 */
	public static boolean getJsonAsBool(String jsonSrc, String jsonKey)
	{
		JsonNode node = JsonUtils.json2obj(jsonSrc, JsonNode.class);

		// 获取jsonKey数据
		JsonNode dataNode = node.get(jsonKey);

		return dataNode.asBoolean();
	}

	/**
	 * 
	 * 将json串转为对象<br>
	 *
	 * @param jsonStr
	 * @param clazz
	 * @return T
	 * @Author fanyaowu
	 * @data 2014年7月9日
	 * @exception
	 * @version
	 *
	 */
	public static <T> T json2obj(String jsonStr, Class<T> clazz)
	{
		if (StringUtils.isEmpty(jsonStr))
		{
			return null;
		}

		T t = null;

		try
		{
			t = objectMapper.readValue(jsonStr, clazz);
		}
		catch (IOException e)
		{

		}
		return t;
	}

	/**
	 * 
	 * 将一般对象转为jsonNode<br>
	 *
	 * @param obj
	 * @return JsonNode
	 * @Author fanyaowu
	 * @data 2014年7月9日
	 * @exception
	 * @version
	 *
	 */
	public static JsonNode obj2node(Object obj)
	{

		if (null == obj)
		{
			return null;
		}

		JsonNode node = null;

		try
		{
			node = objectMapper.readTree(obj2json(obj));
		}
		catch (IOException e)
		{
		}

		return node;
	}
	
	public static <T> T obj2T(Object obj, Class<T> clazz)
	{

		if (null == obj)
		{
			return null;
		}

		T t = null;

		try
		{
			t = objectMapper.readValue(obj2json(obj), clazz);
		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}

		return t;
	}

	/**
	 * 
	 * 将jsonNode转为一般对象<br>
	 *
	 * @param node
	 * @param clazz
	 * @return Object
	 * @Author fanyaowu
	 * @data 2014年7月9日
	 * @exception
	 * @version
	 *
	 */
	public static <T> T node2obj(JsonNode node, Class<T> clazz)
	{
		if (null == node)
		{
			return null;
		}

		return json2obj(obj2json(node), clazz);
	}

	/**
	 * 
	 * 将json串转为map对象<br>
	 *
	 * @param jsonStr
	 * @param clazz
	 * @return
	 * Map<String,T>
	 * @Author fanyaowu
	 * @data 2014年7月9日
	 * @exception 
	 * @version
	 *
	 */
	public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz)
	{

		// 入參校驗
		if (StringUtils.isEmpty(jsonStr))
		{
			return null;
		}

		Map<String, Map<String, Object>> map = null;
		try
		{
			map = objectMapper.readValue(jsonStr,
					new TypeReference<Map<String, T>>()
					{
					});
		}
		catch (IOException e)
		{

		}

		// 非空校验
		if (CollectionUtils.isEmpty(map))
		{
			return null;
		}

		Map<String, T> result = new HashMap<String, T>();
		for (Entry<String, Map<String, Object>> entry : map.entrySet())
		{
			result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
		}
		return result;
	}

	/**
	 * 
	 * jsonnode 转list对象<br>
	 *
	 * @param jsonNode
	 * @param clazz
	 * @return
	 * List<T>
	 * @Author fanyaowu
	 * @data 2014年7月19日
	 * @exception 
	 * @version
	 *
	 */
	public static <T> List<T> node2list(JsonNode jsonNode, Class<T> clazz)
	{
		if (null == jsonNode || !jsonNode.isArray())
		{
			return null;
		}

		List<T> tList = new ArrayList<>();

		for (Iterator<JsonNode> nodeIt = jsonNode.iterator(); nodeIt.hasNext();)
		{
			JsonNode node = nodeIt.next();
			T t = node2obj(node, clazz);

			tList.add(t);
		}

		return tList;
	}

	/**
	 * 
	 * 字符串转map，并且map的value为list<br>
	 *
	 * @param jsonStr
	 * @param clazz
	 * @return
	 * Map<String,List<T>>
	 * @Author fanyaowu
	 * @data 2014年7月19日
	 * @exception 
	 * @version
	 *
	 */
	public static <T> Map<String, List<T>> json2mapWithList(String jsonStr, Class<T> clazz)
	{

		// 入參校驗
		if (StringUtils.isEmpty(jsonStr))
		{
			return null;
		}

		Map<String, Map<String, List<Object>>> map = null;
		try
		{
			map = objectMapper.readValue(jsonStr,
					new TypeReference<Map<String, List<T>>>()
					{
					});
		}
		catch (IOException e)
		{

		}

		// 非空校验
		if (CollectionUtils.isEmpty(map))
		{
			return null;
		}

		Map<String, List<T>> result = new HashMap<>();

		for (Entry<String, Map<String, List<Object>>> entry : map.entrySet())
		{

			List<T> tList = new ArrayList<>();

			@SuppressWarnings("unchecked")
			List<Object> obList = (List<Object>) entry.getValue();

			for (Object o : obList)
			{
				T t = objectMapper.convertValue(o, clazz);
				tList.add(t);
			}

			result.put(entry.getKey(), tList);

		}
		return result;
	}

	/**
	 * 
	 * 将json串转为list对象<br>
	 *
	 * @param jsonStr
	 * @param clazz
	 * @return
	 * List<T>
	 * @Author fanyaowu
	 * @data 2014年7月9日
	 * @exception 
	 * @version
	 *
	 */
	public static <T> List<T> json2list(String jsonStr, Class<T> clazz)
	{

		// 入參校驗
		if (StringUtils.isEmpty(jsonStr))
		{
			return null;
		}

		List<Map<String, Object>> list = null;
		try
		{
			list = objectMapper.readValue(jsonStr,
					new TypeReference<List<T>>()
					{
					});
		}
		catch (IOException e)
		{

		}

		// 非空校验
		if (CollectionUtils.isEmpty(list))
		{
			return null;
		}

		List<T> result = new ArrayList<T>();
		for (Map<String, Object> map : list)
		{
			result.add(map2pojo(map, clazz));
		}
		return result;
	}

	/**
	 * map convert to javaBean
	 */
	private static <T> T map2pojo(@SuppressWarnings("rawtypes")
	Map map, Class<T> clazz)
	{
		return objectMapper.convertValue(map, clazz);
	}
	
	/**
	 * 基本数据类型转换成list
	 * @param jsonStr
	 * @param clazz
	 * @return
	 * List<T>
	 * @Author chenxiaojin
	 * @date 2014年11月5日
	 * @exception 
	 * @version
	 */
	public static <T> List<T> simpleJson2list(String jsonStr, Class<T> clazz)
	{
		try
		{
			List<T> list = objectMapper.readValue(jsonStr, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
			return list;
		}
		catch (JsonParseException e)
		{
			return null;
		}
		catch (JsonMappingException e)
		{
			return null;
		}
		catch (IOException e)
		{
			return null;
		}
	}
	
	/**
	 * 通过过滤器将对象转换成json
	 * @param serializer
	 * @param obj
	 * @return
	 * String
	 * @Author chenxiaojin
	 * @date 2014年11月25日
	 * @exception 
	 * @version
	 */
	public static JsonNode obj2NodeByFilter(FilterPropsSerializer filterSerializer, Object obj)
	{
		ObjectMapper objMapper = new ObjectMapper();
		
		SimpleModule simpleModule = new SimpleModule();
		// 添加需要过滤的Class
		for (Class<?> clz : filterSerializer.getSerializerClasses())
		{
			simpleModule.addSerializer(clz, filterSerializer);
		}
		// 注册模块
		objMapper.registerModule(simpleModule);
		objMapper.setDateFormat(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
		try
		{
			String result = objMapper.writeValueAsString(obj);
			return objMapper.readTree(result);
		}
		catch (IOException e)
		{
			return null;
		}
	}
	
	/**
	 * json字符转为JsonNode对象<br>
	 * @param json
	 * @return
	 * JsonNode
	 * @Author chenxiaojin
	 * @date 2015年1月29日
	 * @exception 
	 * @version
	 */
	public static JsonNode json2node(String json)
	{
		try
		{
			return objectMapper.readTree(json);
		}
		catch (IOException e)
		{
			return null;
		}
	}
}

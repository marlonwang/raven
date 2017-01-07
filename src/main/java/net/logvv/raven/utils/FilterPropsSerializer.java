package net.logvv.raven.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Jackson 自定义返回字段工具类 <br>
 * @author chenxiaojin
 * @date 2014年11月25日
 * @version 1.0.0
 */
public class FilterPropsSerializer extends JsonSerializer<Object>
{
	/** 需要忽略的属性 **/
	private Map<String, List<String>> ignoreProperties = new HashMap<String, List<String>>(10);
	
	/** 需要生成的属性  **/
	private Map<String, List<String>> serializeProperties = new HashMap<String, List<String>>(10);
	
	/** 序列化的类 , 即需要参与属性过滤的类 **/
	private List<Class<?>> serializerClasses = new ArrayList<Class<?>>(5);
	
	/** 过滤类型, IGNORE: 忽略不需要生成JSON的 属性, SERIALIZE:只生成指定属性的JSON */
	public enum FilterType
	{
		IGNORE, SERIALIZE
	}
	
	/** 过滤类型, 默认为忽略  */
	private FilterType filterType = FilterType.IGNORE;
	
	public void serialize(Object obj, JsonGenerator jgen,
			SerializerProvider provder) throws IOException,
			JsonProcessingException
	{
		// 开始处理, 必须要有开始, 否则生成数据有问题
		jgen.writeStartObject();
		
		// 类属性名称
		Field[] fields = obj.getClass().getDeclaredFields();
		// 类名称
		String className = obj.getClass().getName();
		// 根据过滤类型来生成json, IGNORE: 忽略指定属性生成json
		if (FilterType.IGNORE == this.filterType)
		{
			// 忽略指定属性
			List<String> filterPropList = this.ignoreProperties.get(className);
			boolean isNeedFilter = CollectionUtils.isEmpty(filterPropList) ? false : true;
			for (Field field : fields)
			{
				String name = field.getName();
				if (isNeedFilter && filterPropList.contains(name))
				{
					continue;
				}
				Object value = null;
				try
				{
					field.setAccessible(true);
					value = field.get(obj);
				}
				catch (IllegalArgumentException | IllegalAccessException e)
				{
				}
				jgen.writeObjectField(name, value);
			}
		}
		else
		{
			// 只生成指定属性的json
			List<String> filterPropList = this.serializeProperties.get(className);
			boolean isNeedFilter = CollectionUtils.isEmpty(filterPropList) ? false : true;
			for (Field field : fields)
			{
				String name = field.getName();
				if (isNeedFilter && filterPropList.contains(name))
				{
					Object value = null;
					try
					{
						field.setAccessible(true);
						value = field.get(obj);
					}
					catch (IllegalArgumentException | IllegalAccessException e)
					{
					}
					jgen.writeObjectField(name, value);
				}
			}
		}

		// 需要处理结束, 否则生成有问题
		jgen.writeEndObject();
	}
	
	/**
	 * 累加忽略属性值<br>
	 * @param clz 属性所属的类
	 * @param ignoreProps 需要忽略的属性数组
	 * void
	 * @Author chenxiaojin
	 * @date 2014年11月25日
	 * @exception 
	 * @version
	 */
	public void addIgnoreProperties(Class<?> clz, String... ignoreProps)
	{
		serializerClasses.add(clz);
		this.ignoreProperties.put(clz.getName(), Arrays.asList(ignoreProps));
	}

	/**
	 * 添加需要生成json的属性<br>
	 * @param clz 生成json的属性的所属类
	 * @param serializeProperties 生成json属性的属性列表
	 * void
	 * @Author chenxiaojin
	 * @date 2014年11月25日
	 * @exception 
	 * @version
	 */
	public void addSerializeProperties(Class<?> clz, String... serializeProperties)
	{
		serializerClasses.add(clz);
		this.serializeProperties.put(clz.getName(), Arrays.asList(serializeProperties));
	}

	/**
	 * 获取需要序列化的Class<br>
	 * @return
	 * List<Class<?>>
	 * @Author chenxiaojin
	 * @date 2014年11月25日
	 * @exception 
	 * @version
	 */
	public List<Class<?>> getSerializerClasses()
	{
		return serializerClasses;
	}

	public FilterType getFilterType()
	{
		return filterType;
	}

	/**
	 * 设置过滤类型<br>
	 * @param filterType
	 * void
	 * @Author chenxiaojin
	 * @date 2014年11月25日
	 * @exception 
	 * @version
	 */
	public void setFilterType(FilterType filterType)
	{
		this.filterType = filterType;
	}
	
	/**
	 * 清除过滤属性<br>
	 * void
	 * @Author chenxiaojin
	 * @date 2014年11月25日
	 * @exception 
	 * @version
	 */
	public void clearFilterProps()
	{
		this.ignoreProperties.clear();
		this.serializeProperties.clear();
	}
}

package net.logvv.raven.common.model;

import java.util.Iterator;
import java.util.Map;

public class QrCode {
	// 二维码类型
	private QrType type;
	// 扩展参数
	private Map<String,Object> extraParam;
	
	public QrType getType() {
		return type;
	}
	public void setType(QrType type) {
		this.type = type;
	}
	public Map<String,Object> getExtraParam() {
		return extraParam;
	}
	public void setExtraParam(Map<String,Object> extraParam) {
		this.extraParam = extraParam;
	}
	
	/**
	 * 二维码类型 商家、店铺、订单 
	 * MERCHANT、STORE、ORDER
	 * */
	public enum QrType {
		STORE, MERCHANT, ORDER
	}
	
	public String toString ()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("{\"type\":")
			.append("\"")
			.append(type)
			.append("\"")
			.append(",")
			.append("\"extraParam\":{");
		
		Iterator<String> it = extraParam.keySet().iterator();
		int size = extraParam.size();
		int i = 0;
		while (it.hasNext()) 
		{
			++i;
			String key = (String) it.next();
			sb.append("\"")
				.append(key)
				.append("\"")
				.append(":");
			if(extraParam.get(key) instanceof String)
			{
				sb.append("\"")
					.append(extraParam.get(key))
					.append("\"");
			}else {
				sb.append(extraParam.get(key));
			}
				
			if(i < size)
			{
				sb.append(",");
			}
						
		}
		sb.append("}}");
		
		return sb.toString();
	}
}

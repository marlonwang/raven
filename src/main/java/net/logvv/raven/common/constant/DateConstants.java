package net.logvv.raven.common.constant;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public interface DateConstants {
	
	// 默认的stringbuffer 长度，stringbuffer 的默认长度是16，增长策略是每次增加16字节，so可以一次性申请100个字节，提交效率
	int DEFAULT_STRINGBUFFER_LENGTH = 100;
	
	/** 响应状态: STATUS_OK = 1 */
	static final int DB_INSERT_OK = 1;
	
	/** 默认的时间格式为"yyyy-MM-dd HH:mm:ss"，若后续有其他需求，则可以另行添加 */
	static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/** 格式 "yyyy/MM/dd HH:mm:ss" */
	static final String DEFAULT_DATE_FORMAT_2 = "yyyy/MM/dd HH:mm:ss";
	
	/** 格式 "yyyy-MM-dd" */
	static final String DATE_FORMAT = "yyyy-MM-dd";
	
	static final String DATE_FORMAT_2 = "yyyy/MM/dd";
	
	/** 格式 "yyyy-MM" */
	static final String MONTH_FORMAT = "yyyy-MM";
	
	static final String MONTH_FORMAT_2 = "yyyy/MM";
	
	// 默认的时间格式为"yyyy/MM/dd HH:mm:ss"，若后续有其他需求，则可以另行添加
	static DateTimeFormatter defaultFormatter = DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT);
	
	// 当前日期标签，很多类似日志文件需要打时间标签
	static final String DATE_MARK = "yyyyMMdd";
	
	// 默认的时间格式
	//private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
	static final String HOUR_MINUTE = "HH:mm";
	
	/** 星期一 ~ 星期五  1 */
	int MONDAY = 1;
	int TUESDAY =2;
	int WEDNESDAY = 3;
	int THURSDAY = 4;
	int FRIDAY = 5;
	int ALLWEEK = 0;
	
	// 公司名称
	static final String COMPANY_NAME = "30days-tech";
	
}

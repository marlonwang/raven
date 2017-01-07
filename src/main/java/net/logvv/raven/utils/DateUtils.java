package net.logvv.raven.utils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * 
 * 时间工具类，负责提供所有的时间方法，可根据需要自行添加方法<br>
 * 本身依赖于joda，无多线程并发影响<br>
 * 
 * @author: fanyaowu
 * @data: 2014年7月7日
 * @version 1.0.0
 *
 */
public final class DateUtils {
	
	/**
	 * 
	 * 私有构造方法，工具类不需要外部构造
	 *
	 * @Constructors 
	 * @Author: fanyaowu
	 * @data: 2014年7月7日
	 * @version
	 *
	 */
	private DateUtils() {
		super();
	}

	// 默认的时间格式为"yyyy-MM-dd HH:mm:ss"，若后续有其他需求，则可以另行添加
	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private static final String DATA_FORMAT = "yyyy/MM/dd";
	
	private static final String MONTH_FORMAT = "yyyy/MM";
	
	// 默认的时间格式为"yyyy-MM-dd HH:mm:ss"，若后续有其他需求，则可以另行添加
	private static DateTimeFormatter defaultFormatter = DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT);
	
	// 当前日期标签，很多类似日志文件需要打时间标签
	private static final String DATE_MARK = "yyyyMMdd";
	
	// 默认的时间格式
	//private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
	
	private static final String HOUR_MINUTE = "HH:mm";
	
	/**
	 * 
	 * 获取当前时间，返回的格式为"yyyy-MM-dd HH:mm:ss"<br>
	 *
	 * @param: 
	 * @return: String
	 * @Author: fanyaowu
	 * @data: 2014年7月7日
	 * @exception 
	 * @version
	 *
	 */
	public static String getCurrentTimeStr()
	{
		return DateTime.now().toString(DEFAULT_DATE_FORMAT);
	}
	
	/**
	 * 
	 * 获取数据库的timestamp时间格式<br>
	 *
	 * @return
	 * Timestamp
	 * @Author fanyaowu
	 * @data 2014年7月31日
	 * @exception 
	 * @version
	 *
	 */
	public static Timestamp getCurrentTimestamp()
	{
		return new Timestamp(System.currentTimeMillis());
	}
	
	public static Timestamp getTimestamp(Date date)
	{
		return new Timestamp(date.getTime());
	}
	
	public static Timestamp convertTimestampFromStr(String dateStr)
	{
		if(StringUtils.isEmpty(dateStr))
		{
			return null;
		}
		
		DateTime dt = parseDate(dateStr);
		
		return new Timestamp(dt.getMillis());
		
	}
	
	/**
	 * 
	 * 获取当前的日期标签<br>
	 *
	 * @return
	 * String
	 * @Author fanyaowu
	 * @data 2014年7月16日
	 * @exception 
	 * @version
	 *
	 */
	public static String getCurrentDateMark()
	{
		return DateTime.now().toString(DATE_MARK);
	}
	
	/**
	 * 
	 * 获取当前时间，返回的类型为long，精确到毫秒<br>
	 *
	 * @return: long
	 * @Author: fanyaowu
	 * @data: 2014年7月7日
	 * @exception 
	 * @version
	 *
	 */
	public static long getCurrentTime()
	{
		return System.currentTimeMillis();
	}
	
	/**
	 * 
	 * 将时间字符串按照指定格式转为DateTime类型<br>
	 *
	 * @param source
	 * @param format
	 * @return
	 * DateTime
	 * @Author fanyaowu
	 * @data 2014年7月7日
	 * @exception 
	 * @version
	 *
	 */
	public static DateTime parseDate(String source,String format)
	{
		DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern(format);
		
		return DateTime.parse(source, dateTimeFormat);
	}
	
	public static DateTime parseOnlyDate(String source)
	{
		DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern(DATA_FORMAT);
		
		return DateTime.parse(source, dateTimeFormat);
	}
	
	public static DateTime parseOnlyMonth(String source)
	{
		DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern(MONTH_FORMAT);
		
		return DateTime.parse(source, dateTimeFormat);
	}
	
	
	/**
	 * 
	 * 若字符串时间格式为"yyyy/MM/dd HH:mm:ss"，则直接使用该方法转为DateTime类型<br>
	 *
	 * @param source
	 * @return
	 * DateTime
	 * @Author fanyaowu
	 * @data 2014年7月7日
	 * @exception 
	 * @version
	 *
	 */
	public static DateTime parseDate(String source)
	{
		return DateTime.parse(source, defaultFormatter);
	}
	
	/**
	 * 
	 * 将Date类型转为DateTime<br>
	 *
	 * @param date
	 * @return
	 * DateTime
	 * @Author fanyaowu
	 * @data 2014年7月7日
	 * @exception 
	 * @version
	 *
	 */
	public static DateTime parseDate(Date date)
	{
		return new DateTime(date);
	}
	
	/**
	 * 
	 * 将calendar类型转为datetime类型<br>
	 *
	 * @param calendar
	 * @return
	 * DateTime
	 * @Author fanyaowu
	 * @data 2014年7月7日
	 * @exception 
	 * @version
	 *
	 */
	public static DateTime parseDate(Calendar calendar)
	{
		return new DateTime(calendar);
	}
	
	/**
	 * 
	 * 将dateime以默认的时间格式"yyyy-MM-dd HH:mm:ss"转为字符串<br>
	 *
	 * @param dt
	 * @return
	 * String
	 * @Author fanyaowu
	 * @data 2014年7月7日
	 * @exception 
	 * @version
	 *
	 */
	public static String date2Str(DateTime dt)
	{
		return dt.toString(DEFAULT_DATE_FORMAT);
	}
	
	/**
	 * 
	 * 将date按照指定的格式转为字符串类型<br>
	 *
	 * @param dt
	 * @param format
	 * @return
	 * String
	 * @Author fanyaowu
	 * @data 2014年7月7日
	 * @exception 
	 * @version
	 *
	 */
	public static String date2Str(DateTime dt,String format)
	{
		return dt.toString(format);
	}
	
	/**
	 * 
	 * 将dateime以默认的时间格式"yyyy-MM-dd HH:mm:ss"转为字符串<br>
	 *
	 * @param dt
	 * @return
	 * String
	 * @Author fanyaowu
	 * @data 2014年7月7日
	 * @exception 
	 * @version
	 *
	 */
	public static String date2Str(Date date)
	{
		return new DateTime(date).toString(DEFAULT_DATE_FORMAT);
	}
	
	/**
	 * 
	 * 将date按照指定的格式转为字符串类型<br>
	 *
	 * @param dt
	 * @param format
	 * @return
	 * String
	 * @Author fanyaowu
	 * @data 2014年7月7日
	 * @exception 
	 * @version
	 *
	 */
	public static String date2Str(Date date,String format)
	{
		return new DateTime(date).toString(format);
	}
	
	/**
	 * 
	 * 输入时间是否晚于当前时间<br>
	 * 该方法仅比较小时和分钟
	 * @param timeStr 格式为"HH:mm:SS"
	 * @return
	 * boolean
	 * @Author fanyaowu
	 * @data 2014年7月17日
	 * @exception 
	 * @version
	 *
	 */
	public static boolean isAfterNow(String timeStr)
	{
		assert StringUtils.isNoneBlank(timeStr);
		
		String currentDate = getCurrentDateMark();
		
		DateTime dt = DateUtils.parseDate(currentDate+' '+timeStr,DATE_MARK+' '+HOUR_MINUTE);
		
		return dt.isAfterNow();
		
	}
	
	/**
	 * 
	 * 输入时间是否早于当前时间<br>
	 * 该方法仅比较小时和分钟
	 * @param timeStr 格式为"HH:mm:SS"
	 * @return
	 * boolean
	 * @Author fanyaowu
	 * @data 2014年7月17日
	 * @exception 
	 * @version
	 *
	 */
	public static boolean isBeforeNow(String timeStr)
	{
		assert StringUtils.isNoneBlank(timeStr);
		
		String currentDate = getCurrentDateMark();
		
		DateTime dt = DateUtils.parseDate(currentDate+' '+timeStr,DATE_MARK+' '+HOUR_MINUTE);
		
		return dt.isBeforeNow();
		
	}
	
	/**
	 * 
	 * 根据小时和分钟组装成今天的指定时刻<br>
	 *
	 * @param timeStr
	 * @return
	 * DateTime
	 * @Author fanyaowu
	 * @data 2014年7月17日
	 * @exception 
	 * @version
	 *
	 */
	public static DateTime parseHourAndMinute(String timeStr)
	{
		assert StringUtils.isNoneBlank(timeStr);
		String currentDate = getCurrentDateMark();
		DateTime dt = DateUtils.parseDate(currentDate+' '+timeStr,DATE_MARK+' '+HOUR_MINUTE);
		
		return dt;
	}
	
    /**
     * getWeekOfYear
     * 计算当前时间属于一年的第几周,并计算出相对于基准时间 offsetDate的周数<br/>
     * 如果 基准时间 offsetDate 为 null 则仅返回当前时间属于一年中的第几周<br/>
     * 一周从 星期一开始计算  <br/>
     * 遵循 ISO8601 week algorithm, 即 一年的第一周至少有4天及以上
     * @param date
     * @param offsetDate
     * @return
     * @return int  返回类型 
     * @author wangwei
     * @date 2016年6月24日 下午2:39:13 
     * @version  [1.0, 2016年6月24日]
     * @since  version 1.0
     */
    public static int getWeekOfYear(Date date, Date offsetDate)
    {
    	Calendar calendar1 = Calendar.getInstance();
    	calendar1.setFirstDayOfWeek(Calendar.MONDAY);
    	calendar1.setMinimalDaysInFirstWeek(4);
    	calendar1.setTime(date);
    	int weekOfYear = calendar1.get(Calendar.WEEK_OF_YEAR);
    	
    	if(null == offsetDate){
    		return weekOfYear;
    	}
    	
    	Calendar calendar2 = Calendar.getInstance();
  		calendar2.setTime(offsetDate);
  		calendar2.setFirstDayOfWeek(Calendar.MONDAY);
  		calendar2.setMinimalDaysInFirstWeek(4);
  		int weekOfYear2 = calendar2.get(Calendar.WEEK_OF_YEAR);
  
  		int tmp = weekOfYear - weekOfYear2 + 1;
  		return tmp < 0 ? tmp+52 : tmp; // 跨年处理
    }
	
    /**
     * 计算当前是星期几
     * 返回 星期x
     */
    public static String getWeekday(Date date)
    {
    	DateTime dt = new DateTime(date);
    	String weekday = "";
    	//星期  
    	switch(dt.getDayOfWeek()) {  
    	case DateTimeConstants.SUNDAY:  
    		weekday = "星期日";  
    	    break;  
    	case DateTimeConstants.MONDAY:  
    		weekday = "星期一";  
    	    break;  
    	case DateTimeConstants.TUESDAY:  
    		weekday = "星期二";  
    	    break;  
    	case DateTimeConstants.WEDNESDAY:  
    		weekday = "星期三";  
    	    break;  
    	case DateTimeConstants.THURSDAY:  
    		weekday = "星期四";  
    	    break;  
    	case DateTimeConstants.FRIDAY:  
    		weekday = "星期五";  
    	    break;  
    	case DateTimeConstants.SATURDAY:  
    		weekday = "星期六";  
    	    break;  
    	}  
    	return weekday;
    }
    
    /** 返回 周x */
    public static String getWeekday2(Date date)
    {
      DateTime dt = new DateTime(date);
      String weekday = "";
      //星期  
      switch(dt.getDayOfWeek()) {  
      case DateTimeConstants.SUNDAY:  
        weekday = "周日";  
          break;  
      case DateTimeConstants.MONDAY:  
        weekday = "周一";  
          break;  
      case DateTimeConstants.TUESDAY:  
        weekday = "周二";  
          break;  
      case DateTimeConstants.WEDNESDAY:  
        weekday = "周三";  
          break;  
      case DateTimeConstants.THURSDAY:  
        weekday = "周四";  
          break;  
      case DateTimeConstants.FRIDAY:  
        weekday = "周五";  
          break;  
      case DateTimeConstants.SATURDAY:  
        weekday = "周六";  
          break;  
      }  
      return weekday;
    }
    
    /** 返回 SUN,MON,TUE*/
    public static String getEnWeekday(Date date)
    {
    	DateTime dt = new DateTime(date);
    	String weekday = "";
    	//星期  
    	switch(dt.getDayOfWeek()) {  
    	case DateTimeConstants.SUNDAY:  
    		weekday = "SUN";  
    	    break;  
    	case DateTimeConstants.MONDAY:  
    		weekday = "MON";  
    	    break;  
    	case DateTimeConstants.TUESDAY:  
    		weekday = "TUE";  
    	    break;  
    	case DateTimeConstants.WEDNESDAY:  
    		weekday = "WED";  
    	    break;  
    	case DateTimeConstants.THURSDAY:  
    		weekday = "THU";  
    	    break;  
    	case DateTimeConstants.FRIDAY:  
    		weekday = "FRI";  
    	    break;  
    	case DateTimeConstants.SATURDAY:  
    		weekday = "SAT";  
    	    break;  
    	}  
    	return weekday;
    }
}

package net.logvv.raven.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 正则表达式工具类<br>
 * 
 * @author fanyaowu
 * @data 2014年7月13日
 * @version 1.0.0
 *
 */
public final class RegexUtils {

	// 私有构造
	private RegexUtils() {

	}

	/**
	 * 正则表达式匹配，若匹配有结果则返回匹配内容，则无结果则返回null<br>
	 * @param matchSource
	 * @param regexExpression
	 * @return
	 * String
	 * @Author fanyaowu
	 * @data 2014年7月13日
	 * @exception 
	 * @version
	 *
	 */
	public static String matchRegex(String matchSource, String regexExpression) {
		
		return matchRegex(matchSource, regexExpression, 0);
	}

	/**
	 * 是否可以匹配<br>
	 * @param matchSource
	 * @param regexExpression
	 * @return
	 * boolean
	 * @Author fanyaowu
	 * @data 2014年7月13日
	 * @exception 
	 * @version
	 *
	 */
	public static boolean isMatch(String matchSource, String regexExpression) {
		Pattern pattern = Pattern.compile(regexExpression);
		Matcher matcher = pattern.matcher(matchSource);

		return matcher.matches();
	}
	
	/**
	 * 带组信息的正则表达式匹配<br>
	 * @param matchSource
	 * @param regexExpression
	 * @param gourpIndex
	 * @return
	 * String
	 * @Author fanyaowu
	 * @data 2014年7月13日
	 * @exception 
	 * @version
	 *
	 */
	public static String matchRegex(String matchSource, String regexExpression, int gourpIndex)
	{
		Pattern pattern = Pattern.compile(regexExpression);
		Matcher matcher = pattern.matcher(matchSource);

		String matchResult = null;
		if (matcher.find()) {
			matchResult = matcher.group(gourpIndex);
		}
		
		return matchResult;
	}
}

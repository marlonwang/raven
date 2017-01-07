package net.logvv.raven;

import net.logvv.raven.common.constant.RegexConstant;
import net.logvv.raven.utils.RegexUtils;
import net.logvv.raven.utils.DateUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.annotation.Async;

public class Test {
	public static void main(String[] args) {
		String str = "";
		for(int i = 0; i < 5; i++)
		{
			//System.out.println(RandomStringUtils.randomNumeric(8));
			str += RandomStringUtils.randomNumeric(4)+",";
		}
		
		System.out.println(str);
		System.out.println(str.substring(0,str.lastIndexOf(",")));
//		for(int i = 0;i<10;i++){
//			method1();
//		}
		
//		if(Days.daysBetween(DateUtils.parseDate("2016-10-25 00:00:00"),DateTime.now()).getDays() == 0){
//			System.out.println("today");
//		}
		
		String dtStr = DateUtils.getCurrentTimeStr();
		dtStr = dtStr.substring(0, dtStr.lastIndexOf(" ")) + " 00:00:00";
		System.out.println(dtStr);

		regexPhoneTest("17700001111");
	}
	
	@Async
	private static void method1()
	{
		System.out.println("run outside async. ");
		method2();
	}
	
	@Async
	private static void method2(){
		System.out.println("run inside async. ");
	}

	private static void regexPhoneTest(String phoneNumber)
	{
		System.out.println("phone:"+RegexUtils.isMatch(phoneNumber, RegexConstant.PHONE_NUMBER_REGEX));
		System.out.println("china mobile:"+RegexUtils.isMatch(phoneNumber, RegexConstant.CM_PHONE_NUMBER_REGEX));
		System.out.println("china telecom:"+RegexUtils.isMatch(phoneNumber, RegexConstant.CT_PHONE_NUMBER_REGEX));
		System.out.println("china unicom:"+RegexUtils.isMatch(phoneNumber, RegexConstant.CU_PHONE_NUMBER_REGEX));

	}
}

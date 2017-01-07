package net.logvv.raven.cache;

import net.logvv.raven.AbstractTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.fail;

public class CacheServiceTest extends AbstractTestBase {
	@Autowired
	private CacheService cacheService;

	@Test
	public void testCleanValidateLimit() {
		fail("Not yet implemented");
	}

	@Test
	public void testPutValidateCodeLimit() {
		fail("Not yet implemented");
	}

	@Test
	public void testCleanRegValidate() {
		cacheService.cleanRegValidate("13714410115", "123456");
	}

	@Test
	public void testPutSendRegisterValidateCode() {
		cacheService.putSendRegisterValidateCode("13714410115", "123456");
		System.out.println("done");
	}

	@Test
	public void testCleanPwdValidate() {
		fail("Not yet implemented");
	}

	@Test
	public void testPutSendResetPwdValidateCode() {
		fail("Not yet implemented");
	}

	@Test
	public void testPutSendRebindValidateCode() {
		fail("Not yet implemented");
	}

	@Test
	public void testCleanRebindValidate() {
		fail("Not yet implemented");
	}

	@Test
	public void testCleanLoginValidate() {
		fail("Not yet implemented");
	}

	@Test
	public void testPutSendLoginCode() {
		fail("Not yet implemented");
	}

	@Test
	public void testJudgeSendValidateCodeLimit() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckRegisterCode() {
		boolean flag = cacheService.checkRegisterCode("13751370089","822126");

		System.out.println(flag);
	}

	@Test
	public void testCheckResetPwdCode() {
		fail("Not yet implemented");
	}


}

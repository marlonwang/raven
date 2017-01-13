package net.logvv.raven.cache;

import net.logvv.raven.AbstractTestBase;
import net.logvv.raven.utils.JsonUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.fail;

public class CacheServiceTest extends AbstractTestBase {
	@Autowired
	private CacheService cacheService;

	@Test
	public void testGetHuaweiAK() {

		System.out.println(JsonUtils.obj2json(cacheService.getHuaweiAK()));
	}

}

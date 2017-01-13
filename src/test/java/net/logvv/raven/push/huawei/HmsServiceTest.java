package net.logvv.raven.push.huawei;

import net.logvv.raven.AbstractTestBase;
import net.logvv.raven.push.hwpush.huawei.HMSService;
import net.logvv.raven.push.hwpush.huawei.common.AccessToken;
import net.logvv.raven.utils.JsonUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Author: wangwei
 * Created on 2017/1/13 11:26.
 */
public class HmsServiceTest extends AbstractTestBase {

    @Autowired
    private HMSService hmsService;

    @Test
    public void testApplyHMSAccessToken()
    {
        AccessToken ak = hmsService.applyHMSAccessToken();
        System.out.println(JsonUtils.obj2json(ak));
    }

}

package net.logvv.raven.push;

import net.logvv.raven.AbstractTestBase;
import net.logvv.raven.push.model.PushMessage;
import net.logvv.raven.push.model.PushMessage.AppType;
import net.logvv.raven.push.model.PushMessage.ClientType;
import net.logvv.raven.push.model.PushMessage.PushChannel;
import net.logvv.raven.push.model.PushMessage.PushType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class PushMessageServiceTest extends AbstractTestBase {
	
	@Autowired
	private PushMessageService pushMessageService;
	
	@Test
	public void testPushViaXGToken() {
		// 待获取安卓的 pushId (deviceToken)
        String token = "fe4bfa5d29f2e4a94a46bb7930c502fe65965661";
		PushMessage pushMsg = new PushMessage();
        pushMsg.setPushChannel(PushChannel.XG);
        pushMsg.setPushType(PushType.UNICAST);
        pushMsg.setClientType(ClientType.ANDROID);
        pushMsg.setAppType(AppType.COMMON);

		pushMsg.setTitle("校园助手");
		pushMsg.setText("信鸽测试-Android");
		
		pushMsg.setAudiences(Arrays.asList(new String[]{token}));

		pushMessageService.push(pushMsg);
	}


	@Test
	public void testPushViaUmengToken() {
		// 待获取安卓的 pushId (deviceToken)
		String pushId = "AsBEOJycd7maqKYCYh887k3rOhZYZWuw0oRw9bOzCoV8";

		PushMessage pushMsg = new PushMessage();
        pushMsg.setTicker("campus-ticker");   // 必须 通知栏位置(可以设置同text)
        pushMsg.setTitle("campus-title");    // 必须 标题位置
		pushMsg.setText("umeng测试-Android"); // 必须  正文位置

		pushMsg.setAudiences(Arrays.asList(new String[]{pushId}));
		pushMsg.setPushChannel(PushChannel.UMENG);
		pushMsg.setPushType(PushType.UNICAST);
		pushMsg.setClientType(ClientType.ANDROID);
		pushMsg.setAppType(AppType.COMMON);

		pushMessageService.push(pushMsg);
	}

    @Test
    public void testPushViaUmengAlias() {
        // 待获取IOS的 pushId (deviceToken)
        // android D6415EFE66B16A3FCA21C00EB32D2A42
        // ios 9BAB90E4CCE946623907BE49B9AF2444
        List<String> aliasList = Arrays.asList(new String[]{"2051CD88A1D8F76C0CDF07CCE1769D15"});

        PushMessage pushMsg = new PushMessage();
        pushMsg.setTitle("campus-title");
        pushMsg.setText("umeng测试-后台别名推送"); // 必须 内容位置

        System.out.println(aliasList.toString());

        pushMsg.setAudiences(aliasList);
        pushMsg.setPushChannel(PushChannel.UMENG);
        pushMsg.setPushType(PushType.ALIASCAST);
        pushMsg.setClientType(ClientType.IOS);
        pushMsg.setAppType(AppType.COMMON);

        pushMessageService.push(pushMsg);
    }

	@Test
	public void testPushViaHuaweiToken() {
        String token = "0866696026685576300000151700CN01";

        PushMessage pushMsg = new PushMessage();
        pushMsg.setPushChannel(PushChannel.HUAWEI);
        pushMsg.setPushType(PushType.UNICAST);
        pushMsg.setClientType(ClientType.ANDROID);
        pushMsg.setAppType(AppType.COMMON);

        pushMsg.setTitle("HUAWEI");
        pushMsg.setText("来自华为的推送");

        pushMsg.setAudiences(Arrays.asList(new String[]{token}));

        pushMessageService.push(pushMsg);
	}
}

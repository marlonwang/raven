package net.logvv.raven.push.umeng.ios;

import net.logvv.raven.push.umeng.IOSNotification;

public class IOSGroupcast extends IOSNotification {
	public IOSGroupcast() {
		try {
			this.setPredefinedKeyValue("type", "groupcast");	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

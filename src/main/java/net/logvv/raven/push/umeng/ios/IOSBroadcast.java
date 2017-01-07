package net.logvv.raven.push.umeng.ios;

import net.logvv.raven.push.umeng.IOSNotification;

public class IOSBroadcast extends IOSNotification {
	public IOSBroadcast() {
		try {
			this.setPredefinedKeyValue("type", "broadcast");	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

package net.logvv.raven.push.umeng.ios;

import net.logvv.raven.push.umeng.IOSNotification;

public class IOSUnicast extends IOSNotification {
	public IOSUnicast() {
		try {
			this.setPredefinedKeyValue("type", "unicast");	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

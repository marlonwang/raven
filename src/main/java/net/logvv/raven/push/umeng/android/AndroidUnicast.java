package net.logvv.raven.push.umeng.android;

import net.logvv.raven.push.umeng.AndroidNotification;

public class AndroidUnicast extends AndroidNotification {
	public AndroidUnicast() {
		try {
			this.setPredefinedKeyValue("type", "unicast");	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
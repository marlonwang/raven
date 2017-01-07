package net.logvv.raven.push.umeng.android;

import net.logvv.raven.push.umeng.AndroidNotification;

public class AndroidBroadcast extends AndroidNotification {
	public AndroidBroadcast() {
		try {
			this.setPredefinedKeyValue("type", "broadcast");	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

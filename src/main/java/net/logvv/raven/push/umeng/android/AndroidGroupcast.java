package net.logvv.raven.push.umeng.android;

import net.logvv.raven.push.umeng.AndroidNotification;

public class AndroidGroupcast extends AndroidNotification {
	public AndroidGroupcast() {
		try {
			this.setPredefinedKeyValue("type", "groupcast");	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

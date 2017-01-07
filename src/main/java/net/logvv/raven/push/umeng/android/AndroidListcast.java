package net.logvv.raven.push.umeng.android;

import net.logvv.raven.push.umeng.AndroidNotification;

public class AndroidListcast extends AndroidNotification
{
    public AndroidListcast()
    {
        try
        {
            this.setPredefinedKeyValue("type","listcast");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}

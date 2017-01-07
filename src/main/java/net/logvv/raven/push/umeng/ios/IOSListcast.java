package net.logvv.raven.push.umeng.ios;

import net.logvv.raven.push.umeng.IOSNotification;

public class IOSListcast extends IOSNotification
{
    public IOSListcast()
    {
        try
        {
            this.setPredefinedKeyValue("type", "listcast");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
}

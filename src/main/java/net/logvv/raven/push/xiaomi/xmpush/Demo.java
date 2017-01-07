package net.logvv.raven.push.xiaomi.xmpush;

import net.logvv.raven.push.xiaomi.xmpush.server.Sender;
import net.logvv.raven.push.xiaomi.xmpush.server.Constants;
import net.logvv.raven.push.xiaomi.xmpush.server.Message;

/**
 * Author : wangwei
 * Created on 2016/11/28 1:13.
 */
public class Demo {

    private static String iosSecretKey = "" ;
    private static String androidSecretKey = "" ;
    private static String packageName = "";

    public static void main(String[] args) {
        // regid push
        pushViaRegId("xiaomi push","this is content","23213131");
    }

    // regid push
    public static void pushViaRegId(String title,String text,String regid)
    {
        try{
            Constants.useOfficial();
            Sender sender = new Sender(iosSecretKey);
            Message message = new Message.Builder()
                    .title(title)
                    .description(text)
                    .payload(text)
                    .restrictedPackageName(packageName)
                    .notifyType(1)     // 使用默认提示音提示
                    .build();
            sender.sendToAlias(message, regid, 3); //根据aliasList, 发送消息到指定设备上
            // 一次不能超过1000
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // alias push
    private void pushViaAlias(String title,String text,String alias)
    {
        try{
            Constants.useOfficial();
            Sender sender = new Sender(iosSecretKey);
            Message message = new Message.Builder()
                    .title(title)
                    .description(text)
                    .payload(text)
                    .restrictedPackageName(packageName)
                    .notifyType(1)     // 使用默认提示音提示
                    .build();
            sender.sendToAlias(message, alias, 3); //根据aliasList, 发送消息到指定设备上
            // 一次不能超过1000
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

package net.logvv.raven.push.xiaomi.xmpush;

import net.logvv.raven.push.xiaomi.xmpush.server.Result;
import net.logvv.raven.push.xiaomi.xmpush.server.Sender;
import net.logvv.raven.push.xiaomi.xmpush.server.Constants;
import net.logvv.raven.push.xiaomi.xmpush.server.Message;
import net.logvv.raven.utils.JsonUtils;

/**
 * Author : wangwei
 * Created on 2016/11/28 1:13.
 */
public class Demo {

    private static String iosSecretKey = "" ;
    private static String androidSecretKey = "bGdIYzaT7Z1gT99zv3LGTA==" ;
    private static String packageName = "com.thirtydays.pushtest";

    public static void main(String[] args) {
        // regid push
        pushViaRegId("xiaomi push","this is content","23213131");

        //pushViaAlias("title","push content","1122222");
    }

    // regid push
    public static void pushViaRegId(String title,String text,String regid)
    {
        try{
            Constants.useOfficial();
            Sender sender = new Sender(androidSecretKey);
            //Sender sender = new Sender(iosSecretKey);
            Message message = new Message.Builder()
                    .title(title)
                    .description(text)
                    .payload(text)
                    .restrictedPackageName(packageName)
                    .notifyType(1)     // 使用默认提示音提示
                    .build();
            Result result = sender.sendToAlias(message, regid, 3); //根据aliasList, 发送消息到指定设备上
            System.out.println(JsonUtils.obj2json(result));
            // 一次不能超过1000
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // alias push
    private static void pushViaAlias(String title,String text,String alias)
    {
        try{
            Constants.useOfficial();
            //Sender sender = new Sender(iosSecretKey);
            Sender sender = new Sender(androidSecretKey);
            Message message = new Message.Builder()
                    .title(title)
                    .description(text)
                    .payload(text)
                    .restrictedPackageName(packageName)
                    .notifyType(1)     // 使用默认提示音提示
                    .build();
            Result result = sender.sendToAlias(message, alias, 3); //根据aliasList, 发送消息到指定设备上
            System.out.println(JsonUtils.obj2json(result));
            // 一次不能超过1000
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

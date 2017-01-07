package net.logvv.raven.push.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.logvv.raven.push.model.jpush.Options;

/**
 * 
 * (推送消息模型，支持安卓、苹果)<br>
 * 
 * @author fanyaowu
 * @data 2015年1月26日
 * @version 1.0.0
 *
 */
public class PushMessage
{
    
    public enum DisplayType
    {
        // 通知，在界面弹出显示给用户的
        NOTIFICATION("notification"),
        // 消息，仅在后台处理的
        MESSAGE("message");
        
        private String value;
        
        private DisplayType(String value)
        {
            this.value = value;
        }
        
        @Override
        public String toString()
        {
            return this.value;
        }
    }
    
    public enum PushChannel
    {
        // 可以通过自己的消息服务器发送，也可以通过友盟发送，还可以继续集成其他第三方的
        UMENG, JPUSH, XG, MI, HUAWEI
    }
    
    // 消息推送类型，有广播、单播、列播、别名推送
    public enum PushType
    {
        BROADCAST, UNICAST, LISTCAST, ALIASCAST
    }
    
    // 极光对应的是platform
    public enum ClientType
    {
        ANDROID,IOS,ALL
    }
    
    // APP 类型,该字段针对自由城市 商家端和用户端而定,其他app可以删除该字段
    public enum AppType
    {
    	MERCHANT,CUSTOMER,COMMON
    }

    // 别名类型 友盟设置别名推送时必填
    public enum AliasType{
        CustomAliasTypeToken;
    }
    
    // 安卓打开页面类型 ,该字段针对自由城市 商家端和用户端而定,其他app可以删除该字段 
    public enum OpenPage
    {
    	ORDER_NEW,
        ORDER_PAIED,
        ORDER_FEE_CHANGE,
    	REFUND_APPLY,
        REFUND_RESULT,
    	TRADE_FINISH,
    	PROFILE_CHANGE,
    	STAFF_CHECK,
    	OPEN_FORBIDDEN,
    	CHECK_RESULT,
    	ACTIVITY_ALERT,
    	COMMENT_ALERT
    }
    
    // APP 类型,该字段针对自由城市 商家端和用户端而定,其他app可以删除该字段 & 推送方法中的判断
    private AppType appType;
    
    private PushType pushType;
    
    // 产品编码
    private String productCode;
    
    // app包名
    private String appPackageName;
    
    // 发布消息的主题列表，该主题可以为产品code，也可以为用户push id，当是广播类型时，该参数可以为空
    private List<String> audiences;
    
    // 推送消息类型
    private DisplayType displayType = DisplayType.NOTIFICATION;
    
    // 推送通道，默认为友盟
    private PushChannel pushChannel = PushChannel.UMENG;
    
    // 客户端类型，ANDROID、IOS、ALL， 默认为ALL
    private ClientType clientType = ClientType.ALL;

    // 别名类型
    private AliasType aliasType = AliasType.CustomAliasTypeToken;
    
    // 通知栏提示文字
    private String ticker;
    
    // 通知标题
    private String title;
    
    // 通知文字描述
    private String text;
    
    // 状态栏图标ID
    private String icon;
    
    // 通知栏拉开后左侧图标
    private String largeIcon;
    
    // 通知栏大图标的URL链接。该字段的优先级大于largeIcon
    private String img;
    
    // 通知声音
    private String sound;
    
    // 收到通知是否震动,默认为true
    private boolean playVibrate = true;
    
    // 收到通知是否闪灯,默认为true
    private boolean playLight = true;
    
    // 收到通知是否发出声音,默认为true
    private boolean playSound = true;
    
    // APP: 打开应用，URL: 跳转到URL，ACTIVITY: 打开特定的activity
    private String afterAction = "APP";
    
    // 当after_open为URL时，通知栏点击后跳转的URL，ACTIVITY时，通知栏点击后打开的Activity
    private String actionContent;
    
    // 扩展字段参数, key和value都是String类型, 扩展参数在Android仅对display_type为notification类型有效
    private Map<String, String> extraParams = new HashMap<String, String>();
    
    // 额外的配置信息
	private Options options;

	// 默认构造方法
    public PushMessage()
    {
        
    }
    /**
     * 
     * (有参的构造方法，参数必填)<br>
     *
     * @param productCode
     * @param ticker
     * @param title
     * @param text
     * @Constructors
     * @Author fanyaowu
     * @data 2015年1月29日
     * @version
     *
     */
    public PushMessage(String productCode, String appPackageName, String ticker, String title, String text)
    {
        super();
        this.productCode = productCode;
        this.ticker = ticker;
        this.title = title;
        this.text = text;
        this.appPackageName = appPackageName;
    }
    
    public void addExtraParams(String key, String value)
    {
        this.extraParams.put(key, value);
    }
    
    public String getActionContent()
    {
        return actionContent;
    }
    public ClientType getClientType()
    {
        return clientType;
    }
    
    public void setClientType(ClientType clientType)
    {
        this.clientType = clientType;
    }
    
    public String getAfterAction()
    {
        return afterAction;
    }
    
    public String getAppPackageName()
    {
        return appPackageName;
    }
    
    public List<String> getAudiences()
    {
        return audiences;
    }
    
    public DisplayType getDisplayType()
    {
        return displayType;
    }
    
    public Map<String, String> getExtraParams()
    {
        return extraParams;
    }
    
    public String getIcon()
    {
        return icon;
    }
    
    public String getImg()
    {
        return img;
    }
    
    public String getLargeIcon()
    {
        return largeIcon;
    }
    
    public String getProductCode()
    {
        return productCode;
    }
    
    public PushChannel getPushChannel()
    {
        return pushChannel;
    }
    
    public PushType getPushType()
    {
        return pushType;
    }
    
    public String getSound()
    {
        return sound;
    }
    
    public String getText()
    {
        return text;
    }
    
    public String getTicker()
    {
        return ticker;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public boolean isPlayLight()
    {
        return playLight;
    }
    
    public boolean isPlaySound()
    {
        return playSound;
    }
    
    public boolean isPlayVibrate()
    {
        return playVibrate;
    }
    
    public void setActionContent(String actionContent)
    {
        this.actionContent = actionContent;
    }
    
    public void setAfterAction(String afterAction)
    {
        this.afterAction = afterAction;
    }
    
    public void setAppPackageName(String appPackageName)
    {
        this.appPackageName = appPackageName;
    }
    
    public void setAudiences(List<String> audiences)
    {
        this.audiences = audiences;
    }
    
    public void setDisplayType(DisplayType displayType)
    {
        this.displayType = displayType;
    }
    
    public void setIcon(String icon)
    {
        this.icon = icon;
    }
    
    public void setImg(String img)
    {
        this.img = img;
    }
    
    public void setLargeIcon(String largeIcon)
    {
        this.largeIcon = largeIcon;
    }
    
    public void setPlayLight(boolean playLight)
    {
        this.playLight = playLight;
    }
    
    public void setPlaySound(boolean playSound)
    {
        this.playSound = playSound;
    }
    
    public void setPlayVibrate(boolean playVibrate)
    {
        this.playVibrate = playVibrate;
    }
    
    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }
    
    public void setPushChannel(PushChannel pushChannel)
    {
        this.pushChannel = pushChannel;
    }
    
    public void setPushType(PushType pushType)
    {
        this.pushType = pushType;
    }
    
    public void setSound(String sound)
    {
        this.sound = sound;
    }
    
    public void setText(String text)
    {
        this.text = text;
    }
    
    public void setTicker(String ticker)
    {
        this.ticker = ticker;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public Options getOptions()
    {
        return options;
    }

    public void setOptions(Options options)
    {
        this.options = options;
    }

    public void setExtraParams(Map<String, String> extraParams)
    {
        this.extraParams = extraParams;
    }
    
    public AppType getAppType() {
		return appType;
	}
	public void setAppType(AppType appType) {
		this.appType = appType;
	}

    public AliasType getAliasType() {
        return aliasType;
    }

    public void setAliasType(AliasType aliasType) {
        this.aliasType = aliasType;
    }

    @Override
    public String toString()
    {
        StringBuilder pushMessage = new StringBuilder();
        pushMessage.append("pushType=")
            .append(this.pushType.name())
            .append(",")
            .append("productCode=")
            .append(this.productCode)
            .append(",")
            .append("appPackageName")
            .append(this.appPackageName)
            .append(",")
            .append("displayType=")
            .append(this.displayType.toString())
            .append(",")
            .append("pushChannel=")
            .append(this.pushChannel.name())
            .append(",")
            .append("clientType=")
            .append(this.clientType)
            .append("aliasType")
            .append(this.aliasType)
            .append(",")
            .append("ticker=")
            .append(this.ticker)
            .append(",")
            .append("title=")
            .append(this.title)
            .append(",")
            .append("text=")
            .append(this.text)
            .append(",")
            .append("afterAction=")
            .append(this.afterAction)
            .append(",")
            .append("actionContent=")
            .append(this.actionContent)
            .append(",");
        pushMessage.append("audiences=(");
        if (null != this.audiences && !this.audiences.isEmpty())
        {
            
            for (String audience : audiences)
            {
                pushMessage.append(audience).append(",");
            }
            pushMessage.deleteCharAt(pushMessage.length() - 1);
        }
        pushMessage.append("),");
        pushMessage.append("extraParams=(");
        if (!this.extraParams.isEmpty())
        {
            Set<Entry<String, String>> entrySet = this.extraParams.entrySet();
            for (Entry<String, String> entry : entrySet)
            {
                pushMessage.append(entry.getKey()).append("=").append(entry.getValue()).append(",");
            }
            pushMessage.deleteCharAt(pushMessage.length() - 1);
        }
        pushMessage.append(")");
        return pushMessage.toString();
    }

    /** convert [str1,str2] to "str1,str2" */
    public static String list2Str(List<String> list)
    {
        StringBuilder result = new StringBuilder();
        if(list == null || list.size() == 0){
            return "";
        }
        for(String str : list)
        {
            result.append(str).append(",");
        }
        result.deleteCharAt(result.length() - 1);

        return result.toString();
    }
}
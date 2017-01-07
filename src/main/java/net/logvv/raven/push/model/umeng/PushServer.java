package net.logvv.raven.push.model.umeng;

/**
 * 
 * (推送消息服务器)<br>
 * 
 * @author fanyaowu
 * @data 2015年1月27日
 * @version 1.0.0
 *
 */
public class PushServer
{
	private int id;
	
	private String serverIp;
	
	private int port;
	
	private String userName;
	
	private String password;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getServerIp()
	{
		return serverIp;
	}

	public void setServerIp(String serverIp)
	{
		this.serverIp = serverIp;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

}

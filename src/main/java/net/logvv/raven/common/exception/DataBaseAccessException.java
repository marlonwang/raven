package net.logvv.raven.common.exception;

/**
 * 
 * 数据库异常类<br>
 * 
 * @author fanyaowu
 * @data 2014年7月29日
 * @version 1.0.0
 *
 */
public class DataBaseAccessException extends Exception
{

	/**
	 * serialVersionUID
	 *
	 * @since 1.0.0
	 */

	private static final long serialVersionUID = 1L;

	public DataBaseAccessException() {
		super();
	}

	public DataBaseAccessException(String msg) {
		super(msg);
	}

	public DataBaseAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DataBaseAccessException(Throwable cause) {
		super(cause);
	}
}

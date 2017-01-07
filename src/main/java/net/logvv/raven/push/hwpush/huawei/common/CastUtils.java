/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package net.logvv.raven.push.hwpush.huawei.common;

import java.nio.charset.Charset;

public class CastUtils {
  private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

  public static String newString(byte[] bytes) {
    return new String(bytes, CHARSET_UTF8);
  }

  public static byte[] getBytes(String str) {
    if (str == null) {
      return null;
    }

    return str.getBytes(CHARSET_UTF8);
  }
}
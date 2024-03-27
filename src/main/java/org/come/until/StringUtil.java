//接口类型：互亿无线触发短信接口，支持发送验证码短信、订单通知短信等。
// 账户注册：请通过该地址开通账户http://sms.ihuyi.com/register.html
// 注意事项：
//（1）调试期间，请用默认的模板进行测试，默认模板详见接口文档；
//（2）请使用APIID（查看APIID请登录用户中心->验证码短信->产品总览->APIID）及 APIkey来调用接口；
//（3）该代码仅供接入互亿无线短信接口参考使用，客户可根据实际需要自行编写；

package org.come.until;

import org.come.server.GameServer;

import java.security.MessageDigest;
import java.util.UUID;

public class StringUtil {
	public static String str;
	public static final String EMPTY_STRING = "";
	public static String[] nameCondition = { "管理", "GM", "gm", "Gm", "gM", "卖", "物集", "菜", "新开", "群", "号", "币", "艹", "贱", "系统", "当前", "传音", "世界", "帮派", "队伍" };
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * 转换字节数组为16进制字串
	 * @param b 字节数组
	 * @return 16进制字串
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes()));
		} catch (Exception ex) {
		}
		return resultString;
	}

	public static String generateUniqueString(int length, int numberSize, int index) {
		length -= numberSize;
		int beginIndex = GameServer.random.nextInt(32 - length);
		int endIndex = beginIndex + length;
		String uuid = intToString(index, numberSize) + UUID.randomUUID().toString().replaceAll("-", "").substring(beginIndex, endIndex);
		return uuid;
	}

	private static String intToString(int i,int length) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(i);
		length -= buffer.length();
		for (int j = 0; j < length; j++) buffer.insert(0,"0");
		return buffer.toString();
	}
}

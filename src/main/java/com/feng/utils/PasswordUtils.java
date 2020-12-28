package com.feng.utils;

import java.util.UUID;

/**
* @ClassName:       PasswordUtils
*                   密码工具类
* @Author:          小冯
* @CreateDate:      2019/9/7 13:44
* @UpdateUser:      小冯
* @UpdateDate:      2019/9/7 13:44
* @Version:         0.0.1
*/
public class PasswordUtils {

	/**
	 * 匹配密码
	 * @param salt 盐
	 * @param rawPass 明文
	 * @param encPass 密文
	 * @return
	 */
	public static boolean matches(String salt, String rawPass, String encPass) {
		return new PasswordEncoder(salt).matches(encPass, rawPass);
	}
	
	/**
	 * 明文密码加密
	 * @param rawPass 明文
	 * @param salt
	 * @return
	 */
	public static String encode(String rawPass, String salt) {
		return new PasswordEncoder(salt).encode(rawPass);
	}

	/**
	 * 获取加密盐
	 * @return
	 */
	public static String getSalt() {
		return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
	}

	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {
		// 获取盐
		String salt = PasswordUtils.getSalt();
		System.out.println("salt:"+ salt); // 8f5ca7b51f2a4e00b666
		// 对密码 666666 使用 盐 加密。
		salt = "8f5ca7b51f2a4e00b666";
		String password = PasswordUtils.encode("666666", salt);
		System.out.println(password); //57f896335fd98b87985780ef8b2ed4ee

		// 再判断
		boolean matches = PasswordUtils.matches(salt, "666666", password);
		System.out.println(matches); // true

	}
}

package com.winds.common.util;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户 加密工具
 * 生成随机salt
 * md5(md5(password))
 */
public class PasswordHelper {

    private static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    private static String algorithmName = "md5";
    private static final int hashIterations = 2;

    public static Map<String, String> encryptPassword(String passwd) {

        String salt = randomNumberGenerator.nextBytes().toHex();

        Map<String, String> map = new HashMap<>();
        String newPassword = new SimpleHash(
                algorithmName,
                passwd,
                ByteSource.Util.bytes(salt),
                hashIterations).toHex();

        map.put("salt", salt);
        map.put("passwd", newPassword);
       return map;
    }


}
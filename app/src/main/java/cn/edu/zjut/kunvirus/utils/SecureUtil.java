package cn.edu.zjut.kunvirus.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Hasher;
import at.favre.lib.crypto.bcrypt.BCrypt.Verifyer;

public class SecureUtil {
    private static Verifyer verifyer = BCrypt.verifyer();

    private static Hasher hasher = BCrypt.withDefaults();

    /**
     * 判断是否匹配
     */
    public static boolean match(String raw, String secret) {
        return verifyer.verify(raw.toCharArray(), secret).verified;
    }

    /**
     * 加密
     */
    public static String generate(String raw) {
        return hasher.hashToString(12, raw.toCharArray());
    }
}

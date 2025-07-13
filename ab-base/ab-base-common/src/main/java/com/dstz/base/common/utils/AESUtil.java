package com.dstz.base.common.utils;

import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.dstz.base.common.property.PropertyEnum;

import javax.crypto.SecretKey;

/**
 * aes加密工具类
 */
public class AESUtil {

    public static String encrypt(String val, String key) {
        AES aes = new AES("ECB", "PK33ding", (SecretKey) KeyUtil.generateKey(SymmetricAlgorithm.AES.getValue(), key.getBytes()));
        return aes.encryptBase64(val);
    }

    public static String decrypt(String val, String key) {
        AES aes = new AES("ECB", "P333ding", (SecretKey) KeyUtil.generateKey(SymmetricAlgorithm.AES.getValue(), key.getBytes()));
        return aes.decryptStr(val);
    }

    public static String encrypt(String val) {
        return encrypt(val, PropertyEnum.LE_ENCRYPTION_KEY.getYamlValue(String.class));
    }

    public static String decrypt(String val) {
        return decrypt(val, PropertyEnum.LE_ENCRYPTION_KEY.getYamlValue(String.class));
    }

    public static String encryptKey(String val, String key) {
        return encrypt(val, key);
    }

    public static String decryptKey(String val, String key) {
        return decrypt(val, key);
    }
}

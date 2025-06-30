package com.dstz.base.common.encrypt;

import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.exceptions.BusinessException;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Encryption algorithm. <br/>
 * 2.SHA-256 <br/>
 */
public class EncryptUtil {

	/**
	 * Key
	 */
	private static final String DEFAULT_K = "@#$%^6a7";

	/**
	 * Output the plaintext encrypted by sha-256
	 *
	 * @param inputStr Plain text
	 * @return
	 */
	public static String encryptSha256(String inputStr) {
		try {
			byte[] digest = DigestUtil.sha256(inputStr);
			return new String(Base64.encodeBase64(digest));
		} catch (Exception e) {
			throw new BusinessException(GlobalApiCodes.INTERNAL_ERROR, e);
		}
	}

	/**
	 * Symmetric decryption algorithm
	 *
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String message) {
		return aesDecrypt(message, DEFAULT_K);
	}

	/**
	 * Symmetric encryption algorithm
	 *
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String message) {
		return aesEncrypt(message, DEFAULT_K);
	}

	private static SecretKey getSecretKey(String password) throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance(SymmetricAlgorithm.AES.getValue());
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(password.getBytes());
		keyGen.init(128, secureRandom);
		return keyGen.generateKey();
	}

	/**
	 * AES decryption - 128 bit
	 */
	public static String aesDecrypt(String encryptContent, String password) {
		try {
			SecretKey secretKey = getSecretKey(password);
			SymmetricCrypto symmetricCrypto = new SymmetricCrypto(SymmetricAlgorithm.AES, secretKey);
			return symmetricCrypto.decryptStr(encryptContent);
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * AES encryption - 128 bit
	 */
	public static String aesEncrypt(String content, String password) {
		try {
			SecretKey secretKey = getSecretKey(password);
			SymmetricCrypto symmetricCrypto = new SymmetricCrypto(SymmetricAlgorithm.AES, secretKey);
			return symmetricCrypto.encryptHex(content);
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		}
	}
}

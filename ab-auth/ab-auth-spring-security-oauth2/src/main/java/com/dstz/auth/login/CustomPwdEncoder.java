package com.dstz.auth.login;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
/**
 * Password encryption
 *
 */
public class CustomPwdEncoder implements PasswordEncoder {

    private static ThreadLocal<Boolean> ingorePwd = new ThreadLocal<>();

    public static void setIngore(boolean ingore) {
        ingorePwd.set(ingore);
    }

    public static void removeIngore(){
        ingorePwd.remove();
    }

    public final int MAX_LENGTH=25;
    /**
     * Encode the raw password.
     * Generally, a good encoding algorithm applies a SHA-1 or greater hash combined with an 8-byte or greater randomly
     * generated salt.
     */
    @Override
    public String encode(CharSequence rawPassword) {
        String pwd = rawPassword.toString();
        try {
            if(StrUtil.isNotBlank(pwd)&&pwd.length()>MAX_LENGTH){
                return pwd;
            }
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(pwd.getBytes(StandardCharsets.UTF_8.displayName()));
            return new String(Base64.encodeBase64(digest));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * Verify the encoded password obtained from storage matches the submitted raw password after it too is encoded.
     * Returns true if the passwords match, false if they do not.
     * The stored password itself is never decoded.
     *
     * @param rawPassword     the raw password to encode and match
     * @param encodedPassword the encoded password from storage to compare with
     * @return true if the raw password, after encoding, matches the encoded password from storage
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (ingorePwd.get() == null || !ingorePwd.get()) {
            String enc = this.encode(rawPassword);
            return enc.equals(encodedPassword);
        }
        return true;
    }

}

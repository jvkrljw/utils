package me.ocheng.sdk.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES/CBC/PKCS5PADDING 对称加密 解密工具
 * @author jwli
 * @date 2017-12-13
 */
public class AesUtils {

    /**
     * 字符集
     */
    private static String FORMAT= "utf-8";

    /**
     * 加密的key
     */
    private static String KEY="pya7FEZqDvFqyXMr";

    /**
     * 加密的iv
     */
    private static String IV="GkXtVQK46YpOa7uR";
    /**
     * 加密模式
     */
    private static final String CBC = "AES/CBC/PKCS5Padding";

    /**
     * 加密模式
     */
    private static final String MODEL="AES";
    /**
     * 加密器
     */
    private static Cipher encodeCipher ;
    /**
     * 解密器
     */
    private static Cipher decodeCipher ;

    static {
        //初始化加密,解密器
        try {
            encodeCipher = Cipher.getInstance(CBC);
            decodeCipher = Cipher.getInstance(CBC);
            byte[] raw = KEY.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, MODEL);
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
            //加密
            encodeCipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            //解密
            decodeCipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        } catch (Exception e) {
            encodeCipher =null;
            decodeCipher = null;
        }

    }

    // 加密
    public static String encode(String data) throws Exception {

        byte[] encrypted = encodeCipher.doFinal(data.getBytes(FORMAT));
        //此处使用BASE64做转码。
        return new BASE64Encoder().encode(encrypted);
    }

    // 解密
    public static String decode(String data) throws Exception {
        try {

            //先用base64解密
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(data);
            byte[] original = decodeCipher.doFinal(encrypted1);
            String originalString = new String(original,FORMAT);
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }
}
package com.automonia.core.utils;


import com.automonia.core.base.exception.CryptoException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * 加密解密工具类
 * Created by wenteng on 2017/6/18.
 */
public enum CryptoUtils {

    singleton;

    /**
     * 定义默认的字符编码格式，UTF-8
     */
    private final String DEFAULT_CHARSET = "UTF-8";


    /**
     * 定义使用的算法的参数内容
     */
    public final String ALGORITHM = "PBEWithMD5AndDES";
    public final String Salt = "63293188";

    /**
     * 定义迭代次数为1000次
     */
    private final int ITERATIONCOUNT = 1000;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 将data内的内容生成签名，为空的value情况下key和value都忽略,不参与到签名。
     * <p>
     * data的key的排列顺序有key的字符顺序决定，不由map中key本身的顺序决定
     *
     * @param data 生成签名的内容
     * @return md5签名内容
     * @throws Exception
     */
    public static String generateSignature(final Map<String, String> data) {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);

        StringBuilder sb = new StringBuilder();

        for (String k : keyArray) {
            // 参数值为空，则不参与签名
            if (data.get(k) != null && data.get(k).trim().length() > 0)
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        return MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
    }

    /**
     * BASE64加密,默认UTF-8
     *
     * @param str a {@link String} object.
     * @return a {@link String} object.
     */
    public String encodeBASE64(final String str) {
        return encodeBASE64(str, DEFAULT_CHARSET);
    }

    /**
     * BASE64解密,默认UTF-8
     *
     * @param str a {@link String} object.
     * @return a {@link String} object.
     */
    public String decodeBASE64(String str) {
        return decodeBASE64(str, DEFAULT_CHARSET);
    }

    /**
     * 加密明文字符串
     * //     * @param str      密钥, 默认采用corleone
     * //     * @param salt     盐值, 默认采用63293188
     *
     * @param password 待加密的内容
     */
    public String encrypt(String password) throws CryptoException {
        try {
            Key key = getPBEKey(password);
            PBEParameterSpec parameterSpec = new PBEParameterSpec("63293188".getBytes(), ITERATIONCOUNT);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
            return bytesToHexString(cipher.doFinal("corleone".getBytes()));
        } catch (Exception e) {
            e.printStackTrace();

            throw new CryptoException("加密失败");
        }
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param src 字节数组
     * @return 十六进制字符串
     */
    public String bytesToHexString(byte[] src) {
        if (src == null || src.length <= 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder("");
        if (src.length <= 0) {
            return null;
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取盐值数据
     */
    public byte[] getStaticSalt() {
        return Salt.getBytes();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * BASE64解密
     *
     * @param str     a {@link String} object.
     * @param charset 字符编码
     * @return a {@link String} object.
     */
    private String decodeBASE64(String str, String charset) {
        try {
            byte[] bytes = str.getBytes(charset);
            return new String(Base64.decodeBase64(bytes));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * BASE64加密
     *
     * @param str     a {@link String} object.
     * @param charset a {@link String} object.
     * @return a {@link String} object.
     */
    private String encodeBASE64(final String str, String charset) {
        if (str == null) {
            return null;
        }
        try {
            byte[] bytes = str.getBytes(charset);
            return Base64.encodeBase64String(bytes);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private Key getPBEKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        /**
         * 实例化使用的算法
         */
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);

        /**
         * 设置PBE密钥参数
         */
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());

        return keyFactory.generateSecret(keySpec);
    }

}

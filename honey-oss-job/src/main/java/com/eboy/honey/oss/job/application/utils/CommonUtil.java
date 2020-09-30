package com.eboy.honey.oss.job.application.utils;

import com.google.common.base.CaseFormat;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author yangzhijie
 * @date 2020/9/30 9:51
 */
public class CommonUtil {

    /**
     * 获取精确到秒的时间戳
     */
    public static int getSecondTimestamp(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime() / 1000);
        return Integer.parseInt(timestamp);
    }

    /**
     * 构建32位UUID
     *
     * @return 32UUID
     */
    public static String get32Uid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 驼峰转下划线
     */
    public static String lowerCamel(String key) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key);
    }

    /**
     * 将Object对象里面的属性和值转化成Map对象
     */
    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : getAllFields(clazz)) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            if (value != null) {
                map.put(fieldName, value);
            }
        }
        return map;
    }

    /**
     * 获取本类及其父类的属性的方法
     *
     * @param clazz 当前类对象
     * @return 字段数组
     */
    protected static Field[] getAllFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        return fieldList.toArray(fields);
    }

    /**
     * 对字符串MD5加密
     *
     * @param str 要被加密的字符串
     * @return 加密后的字符串
     */
    public static String md5(String str) {
        Assert.notNull(str, "要加密的字符串不能为空！");
        String md5 = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            bytes = digest.digest(bytes);
            md5 = toHexStr(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

    private static String toHexStr(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        int var;
        for (byte b : bytes) {
            var = ((int) b) & 0xFF;
            if (var < 16) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(var));
        }
        return sb.toString().toUpperCase();
    }
}

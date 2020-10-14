package com.eboy.honey.oss.admin.application.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.util.UUID;

/**
 * @author yangzhijie
 * @date 2020/10/14 10:23
 */
@Slf4j
public class CommonUtil {


    /**
     * 构建32位UUID
     *
     * @return 32UUID
     */
    public static String get32Uid() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    public static String createCredential(String password, String salt) {
        //加密方式
        String hashAlgorithmName = "MD5";
        //盐：相同密码使用不同的盐加密后的结果不同
        ByteSource byteSalt = ByteSource.Util.bytes(salt);
        //加密次数
        int hashIterations = 2;
        SimpleHash result = new SimpleHash(hashAlgorithmName, password, byteSalt, hashIterations);
        return result.toString();
    }

    public static void main(String[] args) {
        String credential = createCredential("123456", "honey-oss-admin");
        log.info(credential);
    }
}

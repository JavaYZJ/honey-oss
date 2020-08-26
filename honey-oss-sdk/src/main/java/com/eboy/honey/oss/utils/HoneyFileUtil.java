package com.eboy.honey.oss.utils;

import com.eboy.honey.oss.dto.FileDto;
import com.eboy.honey.oss.dto.HoneyStream;
import com.sun.istack.internal.NotNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Objects;
import java.util.UUID;

/**
 * @author yangzhijie
 * @date 2020/8/14 10:18
 */
@Slf4j
public class HoneyFileUtil {

    /**
     * 缩略图fileKey前缀标志
     */
    public final static String PRE_THUMBNAIL_TAG = "THUMBNAIL_";

    /**
     * 获取文件MD5的fileKey
     *
     * @param file 文件
     * @return fileKey
     */
    public static String getFileKey(@NotNull File file) {
        try {
            FileInputStream inputStream = new FileInputStream(file);
            return getFileKey(inputStream);
        } catch (FileNotFoundException e) {
            log.warn("获取文件的FileKey失败，原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取文件MD5的fileKey
     *
     * @param filePath 文件路径
     * @return fileKey
     */
    public static String getFileKey(@NotNull String filePath) {
        File file = new File(filePath);
        return getFileKey(file);
    }

    /**
     * 获取文件MD5的fileKey
     *
     * @param inputStream 文件流
     * @return fileKey
     */
    public static String getFileKey(@NotNull InputStream inputStream) {
        try {
            ByteArrayOutputStream stream = cloneInputStream(inputStream);
            InputStream var1 = new ByteArrayInputStream(Objects.requireNonNull(stream).toByteArray());
            return DigestUtils.md5DigestAsHex(var1);
        } catch (IOException e) {
            log.warn("获取文件的FileKey失败，原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取缩略图ObjectName(构建规则：PRE_THUMBNAIL_TAG + 原图filKey)
     *
     * @param fileKey 原图fileKey
     * @return 缩略图fileKey
     */
    public static String getThumbnailObjectName(String fileKey, String outputFormat) {
        return PRE_THUMBNAIL_TAG + fileKey + "." + outputFormat;
    }

    /**
     * 获取缩略图fileKey(构建规则：PRE_THUMBNAIL_TAG + 原图filKey)
     *
     * @param fileKey 原图fileKey
     * @return 缩略图fileKey
     */
    public static String getThumbnailFileKey(String fileKey) {
        return PRE_THUMBNAIL_TAG + fileKey;
    }

    /**
     * 构建minio上传需要的objectName(唯一)
     *
     * @param fileKey 文件fileKey
     * @return objectName
     */
    public static String buildObjectNameByFileKey(@NotNull String fileName, @NotNull String fileKey) {
        StringBuilder sb = new StringBuilder(fileName);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("(");
        sb2.append(fileKey);
        sb2.append(")");
        int index = sb.lastIndexOf(".");
        sb.insert(index, sb2);
        return sb.toString();
    }

    /**
     * 构建分片objectName
     *
     * @param ShardName  分片名
     * @param shardIndex 分片索引
     * @return 分片objectName
     */
    public static String buildShardObjectName(@NotNull String ShardName, @NotNull int shardIndex) {
        StringBuilder sb = new StringBuilder(ShardName);
        return sb.append("_").append(shardIndex).toString();
    }

    /**
     * 将inputStream写入本地
     *
     * @param destination 本地文件目录
     * @param input       文件流
     */
    public static void writeToLocal(String destination, InputStream input) {
        int index;
        byte[] bytes = new byte[1024];
        try (FileOutputStream downloadFile = new FileOutputStream(destination)) {
            while ((index = input.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                downloadFile.flush();
            }
        } catch (IOException e) {
            log.warn("File inputStream write fail,reason:{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static ByteArrayOutputStream cloneInputStream(@NotNull InputStream input) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static InputStream getInputStream(@NotNull FileDto fileDto) throws FileNotFoundException {
        InputStream inputStream = null;
        String filePath = fileDto.getFilePath();
        if (filePath != null) {
            File file = new File(filePath);
            inputStream = new FileInputStream(file);
        }
        File file = fileDto.getFile();
        if (file != null) {
            inputStream = new FileInputStream(file);
        }
        InputStream honeyStream = fileDto.getHoneyStream().getInputStream();
        if (honeyStream != null) {
            inputStream = honeyStream;
        }
        Assert.isTrue(inputStream != null, "文件不能为空，请指定文件路径或者创建File实体，或者传入文件流InputStream");
        return inputStream;
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
     * 获取文件后缀格式
     *
     * @param fileName 文件名
     * @return 后缀名
     */
    public static String getFileSuffix(@NotNull String fileName) {
        Assert.isTrue(!fileName.isEmpty(), "文件名为空");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 获取文件名（不带后缀）
     *
     * @param fileName 文件名
     * @return 文件名（不带后缀）
     */
    public static String getFilePrefix(@NotNull String fileName) {
        Assert.isTrue(!fileName.isEmpty(), "文件名为空");
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * 获取文件流大小
     *
     * @param inputStream 文件流
     * @return 文件字节大小
     */
    public static long getFileSize(@NotNull FileInputStream inputStream) {
        try {
            return inputStream.getChannel().size();
        } catch (IOException e) {
            log.warn("获取文件流大小失败，原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 参数校验&构建
     *
     * @param fileDto 文件实体
     */
    @SneakyThrows
    public static void buildArgs(@NotNull FileDto fileDto) {
        // uid
        String uid = get32Uid();
        fileDto.setUid(uid);
        // 文件名
        String fileName = fileDto.getFileName();
        Assert.isTrue(!StringUtils.isEmpty(fileName), "文件名不能为空");
        // 文件后缀
        String fileSuffix = getFileSuffix(fileName);
        fileDto.setFileSuffix(fileSuffix);
        // 文件流 & fileKey
        InputStream inputStream = getInputStream(fileDto);
        String fileKey = getFileKey(inputStream);
        fileDto.setFileKey(fileKey);
        fileDto.setHoneyStream(new HoneyStream(inputStream));
        // 文件大小
        fileDto.setFileSize(HoneyFileUtil.getFileSize((FileInputStream) inputStream));
    }
}

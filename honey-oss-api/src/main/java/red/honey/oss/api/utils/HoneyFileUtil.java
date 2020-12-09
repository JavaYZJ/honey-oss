package red.honey.oss.api.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;
import red.honey.oss.api.dto.FileDto;
import red.honey.oss.api.dto.HoneyStream;

import javax.validation.constraints.NotNull;
import java.io.*;
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
     * 文件切割
     *
     * @param filePath 文件路径
     * @param count    切割块数
     */
    public static long spiltFile(String filePath, int count) {
        // 预分配文件所占用的磁盘空间，在磁盘创建一个指定大小的文件，“r”表示只读，“rw”支持随机读写
        try (RandomAccessFile raf = new RandomAccessFile(new File(filePath), "r")) {
            // 计算文件大小
            long length = raf.length();
            // 计算文件切片后每一份文件的大小
            long maxSize = length / count;
            // 定义初始文件的偏移量(读取进度)
            long offset = 0L;
            // 开始切割文件 count-1最后一份文件不处理
            for (int i = 0; i < count - 1; i++) {
                // 标记初始化
                long fbegin = offset;
                // 分割第几份文件
                long fend = (i + 1) * maxSize;
                // 写入文件
                offset = getWrite(filePath, i, fbegin, fend);
            }
            // 剩余部分文件写入到最后一份(如果不能平平均分配的时候)
            if (length - offset > 0) {
                // 写入文件
                getWrite(filePath, count - 1, offset, length);
            }
            return maxSize;
        } catch (Exception e) {
            log.warn("file spilt happen error,the reason: {}", e.getMessage());
            throw new RuntimeException("file spilt happen error");
        }
    }

    /**
     * 指定文件每一份的边界，写入不同文件中
     *
     * @param file  源文件
     * @param index 源文件的顺序标识
     * @param begin 开始指针的位置
     * @param end   结束指针的位置
     * @return long
     */
    public static long getWrite(String file, int index, long begin, long end) {

        long endPointer;
        try (
                // 申明文件切割后的文件磁盘
                RandomAccessFile in = new RandomAccessFile(new File(file), "r");
                // 定义一个可读，可写的文件并且后缀名为.tmp的二进制文件
                RandomAccessFile out = new RandomAccessFile(new File(file + "_" + index + ".tmp"), "rw")
        ) {
            // 申明具体每一文件的字节数组
            byte[] b = new byte[1024];
            int n;
            // 从指定位置读取文件字节流
            in.seek(begin);
            // 判断文件流读取的边界
            while (in.getFilePointer() <= end && (n = in.read(b)) != -1) {
                // 从指定每一份文件的范围，写入不同的文件
                out.write(b, 0, n);
            }
            // 定义当前读取文件的指针
            endPointer = in.getFilePointer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return endPointer;
    }

    /**
     * 文件合并
     *
     * @param desFile   指定合并文件
     * @param tempFile  分割前的文件名
     * @param tempCount 文件个数
     */
    public static void merge(String desFile, String tempFile, int tempCount) {
        // 申明随机读取文件RandomAccessFile
        try (RandomAccessFile raf = new RandomAccessFile(new File(desFile), "rw")) {
            // 开始合并文件，对应切片的二进制文件
            for (int i = 0; i < tempCount; i++) {
                // 读取切片文件
                RandomAccessFile reader = new RandomAccessFile(new File(tempFile + "_" + i + ".tmp"), "r");
                byte[] b = new byte[1024];
                int n;
                while ((n = reader.read(b)) != -1) {
                    // 一边读，一边写
                    raf.write(b, 0, n);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
            return DigestUtils.md5DigestAsHex(inputStream);
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
     * 构建分片ShardName
     *
     * @param fileName   文件名
     * @param shardIndex 分片索引
     * @return 分片objectName
     */
    public static String buildShardName(@NotNull String fileName, @NotNull int shardIndex) {
        return fileName + "_" + shardIndex + ".temp";
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
     * File转换成FileDto
     *
     * @param file file
     * @return FileDto
     */
    public static FileDto convertFileDto(File file) {
        try {
            FileDto fileDto = new FileDto();
            FileInputStream inputStream = FileUtils.openInputStream(file);
            // 设置uid
            fileDto.setUid(HoneyFileUtil.get32Uid());
            // 设置流
            HoneyStream honeyStream = new HoneyStream(inputStream);
            fileDto.setHoneyStream(honeyStream);
            // 设置文件名
            String fileName = file.getName();
            fileDto.setFileName(fileName);
            // 设置fileKey
            String fileKey = HoneyFileUtil.getFileKey(FileUtils.openInputStream(file));
            fileDto.setFileKey(fileKey);
            // 设置文件格式
            String fileSuffix = HoneyFileUtil.getFileSuffix(fileName);
            fileDto.setFileSuffix(fileSuffix);
            // 设置文件大小
            long fileSize = HoneyFileUtil.getFileSize(inputStream);
            fileDto.setFileSize(fileSize);
            // 设置分片总数
            fileDto.setShardTotal(0);
            // 设置分片大小
            fileDto.setShardSize(0);
            return fileDto;
        } catch (IOException e) {
            log.warn("SDK构建FileDto失败，原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}

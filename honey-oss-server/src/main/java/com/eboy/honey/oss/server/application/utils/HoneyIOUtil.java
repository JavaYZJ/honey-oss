package com.eboy.honey.oss.server.application.utils;

import com.eboy.honey.oss.server.application.vo.FileVo;
import com.eboy.honey.oss.utils.HoneyFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @author yangzhijie
 * @date 2020/8/26 16:14
 */
@Slf4j
public class HoneyIOUtil {

    public static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ioe) {
            // ignore
        }
    }

    /**
     * MultipartFile转File
     *
     * @param file MultipartFile
     * @return File
     */
    public static File multipartFile2File(MultipartFile file) {
        Assert.notNull(file, "file must not null");
        CommonsMultipartFile cf = (CommonsMultipartFile) file;
        DiskFileItem fi = (DiskFileItem) cf.getFileItem();
        return fi.getStoreLocation();
    }

    /**
     * MultipartFile转FileVo
     *
     * @param file MultipartFile
     * @return FileVo
     */
    public static FileVo multipartFile2FileVo(MultipartFile file) {
        Assert.notNull(file, "file must not null");
        File temp = multipartFile2File(file);
        FileVo fileVo = BeanConvertUtil.convertFileVo(temp);
        fileVo.setFileName(file.getOriginalFilename());
        fileVo.setFileSuffix(HoneyFileUtil.getFileSuffix(Objects.requireNonNull(file.getOriginalFilename())));
        return fileVo;
    }
}

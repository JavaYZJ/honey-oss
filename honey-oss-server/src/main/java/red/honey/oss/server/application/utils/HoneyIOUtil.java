package red.honey.oss.server.application.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import red.honey.oss.api.utils.HoneyFileUtil;
import red.honey.oss.server.application.vo.FileVo;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
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

    public static ResponseEntity<InputStream> giveBackStream(InputStream inputStream, String fileName) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + fileName);
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok().headers(headers).contentLength(inputStream.available()).contentType(MediaType.parseMediaType("application/octet-stream")).body(inputStream);
    }
}

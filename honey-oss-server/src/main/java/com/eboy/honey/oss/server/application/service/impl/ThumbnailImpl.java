package com.eboy.honey.oss.server.application.service.impl;

import com.eboy.honey.oss.api.entiy.OutputMode;
import com.eboy.honey.oss.api.entiy.Region;
import com.eboy.honey.oss.api.entiy.Thumbnail;
import com.eboy.honey.oss.api.entiy.WaterMark;
import com.eboy.honey.oss.api.utils.FilePathUtil;
import com.eboy.honey.oss.api.utils.HoneyFileUtil;
import com.eboy.honey.oss.server.application.service.FileService;
import com.eboy.honey.oss.server.application.service.ThumbnailService;
import com.eboy.honey.oss.server.application.utils.BeanConvertUtil;
import com.eboy.honey.oss.server.application.vo.FileVo;
import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author yangzhijie
 * @date 2020/8/21 10:47
 */
@Service
@Slf4j
public class ThumbnailImpl implements ThumbnailService {

    @Autowired
    private FileService fileService;

    /**
     * 构建缩略图
     *
     * @param thumbnail 构建规则
     */
    @Override
    public void buildThumbnail(@NotNull Thumbnail thumbnail) {
        // 设置输入源
        Thumbnails.Builder<File> of = handleInputSource(thumbnail);
        // 设置缩略图的长宽比例以及旋转角度
        of.scale(thumbnail.getLengthScale(), thumbnail.getWidthScale()).rotate(thumbnail.getRotate());
        // 设置裁剪
        handleRegion(of, thumbnail);
        // 设置水印
        handleWaterMark(of, thumbnail);
        // 设置缩略图质量
        handleOutputQuality(of, thumbnail);
        // 设置输出格式转化
        handleOutputFormat(of, thumbnail);
        // 设置输出方式
        handleOutputMode(of, thumbnail);
    }

    /**
     * 上传缩略图
     *
     * @param image       缩略图
     * @param bucketName  桶名
     * @param contentType contentType
     * @return 缩略图fileKey
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String upload(File image, String bucketName, MediaType contentType) {
        FileVo fileVo = BeanConvertUtil.convertFileVo(image);
        return fileService.upload(fileVo, bucketName, contentType);
    }

    /**
     * 根据原图获取缩略图链接
     *
     * @param fileKey 原图fileKey
     * @return 缩略图链接
     */
    @Override
    public String getUrlByOriginalPicture(String bucketName, String fileKey) {
        String thumbnailFileKey = HoneyFileUtil.getThumbnailFileKey(fileKey);
        return fileService.downAsUrl(bucketName, thumbnailFileKey);
    }

    /**
     * 根据原图获取缩略图链接
     *
     * @param bucketName 桶名
     * @param fileKey    原图fileKey
     * @param expires    过期时间（秒）
     * @return 缩略图链接
     */
    @Override
    public String getUrlByOriginalPicture(String bucketName, String fileKey, Integer expires) {
        String thumbnailFileKey = HoneyFileUtil.getThumbnailFileKey(fileKey);
        return fileService.downAsUrl(bucketName, thumbnailFileKey, expires);
    }

    /**
     * 根据原图获取缩略图流
     *
     * @param bucketName 桶名
     * @param fileKey    原图fileKey
     * @return 缩略图流
     */
    @Override
    public InputStream getStreamByOriginalPicture(String bucketName, String fileKey) {
        String thumbnailFileKey = HoneyFileUtil.getThumbnailFileKey(fileKey);
        return fileService.downAsStream(bucketName, thumbnailFileKey);
    }

    /**
     * 根据原图将缩略图下载至指定本地路径
     *
     * @param bucketName   桶名
     * @param fileKey      原图fileKey
     * @param fileDownPath 指定本地路径
     */
    @Override
    public void down2LocalByOriginalPicture(String bucketName, String fileKey, String fileDownPath) {
        String thumbnailFileKey = HoneyFileUtil.getThumbnailFileKey(fileKey);
        fileService.down2Local(bucketName, thumbnailFileKey, fileDownPath);
    }


    /**
     * 处理输入源
     */
    protected Thumbnails.Builder<File> handleInputSource(Thumbnail thumbnail) {
        File inputSource = thumbnail.getInputSource();
        Thumbnails.Builder<File> of;
        if (inputSource != null) {
            File[] files = inputSource.listFiles();
            of = files != null && files.length > 0 ? Thumbnails.of(files) : Thumbnails.of(inputSource);
            return of;
        } else {
            throw new IllegalArgumentException("input source is null");
        }
    }

    /**
     * 处理裁剪
     */
    protected void handleRegion(Thumbnails.Builder<File> of, Thumbnail thumbnail) {
        Region region = thumbnail.getRegion();
        if (region != null) {
            int top = region.getTop();
            int button = region.getButton();
            int left = region.getLeft();
            int right = region.getRight();
            of.sourceRegion(top, button, left, right);
        }
    }

    /**
     * 处理水印
     */
    protected void handleWaterMark(Thumbnails.Builder<File> of, Thumbnail thumbnail) {
        WaterMark waterMark = thumbnail.getWaterMark();
        if (waterMark != null) {
            // 水印位置
            Positions positions = waterMark.getPositions();
            // 水印透明度
            float transparency = waterMark.getTransparency();
            // 水印图片源
            File waterMarkSource = waterMark.getWaterMarkSource();
            Assert.notNull(waterMarkSource, "waterMarkSource must not null");
            try {
                of.watermark(positions, ImageIO.read(waterMarkSource), transparency);
            } catch (IOException e) {
                log.warn("handle the watermark happen error,the reason:{}", e.getMessage());
                throw new RuntimeException("handle the watermark happen error");
            }
        }
    }

    /**
     * 处理输出图片质量
     */
    protected void handleOutputQuality(Thumbnails.Builder<File> of, Thumbnail thumbnail) {
        double outputQuality = thumbnail.getOutputQuality();
        of.outputQuality(outputQuality);
    }

    /**
     * 处理输出图片格式
     */
    protected void handleOutputFormat(Thumbnails.Builder<File> of, Thumbnail thumbnail) {
        String outputFormat = thumbnail.getOutputFormat();
        of.outputFormat(outputFormat);
    }

    /**
     * 处理输出方式
     */
    protected void handleOutputMode(Thumbnails.Builder<File> of, Thumbnail thumbnail) {
        OutputMode outputMode = thumbnail.getOutputMode();
        try {
            if (outputMode != null) {
                String filePath = outputMode.getFilePath();
                OutputStream outputStream = outputMode.getOutputStream();
                if (filePath != null) {
                    of.toFile(filePath);
                } else if (outputStream != null) {
                    of.toOutputStream(outputStream);
                }
            } else {
                // 什么都没设置时，默认存到一个目录
                String thumbnailPath = FilePathUtil.defaultThumbnailPath() + thumbnail.getOutputFormat();
                of.toFile(thumbnailPath);
                OutputMode mode = new OutputMode();
                mode.setFilePath(thumbnailPath);
                thumbnail.setOutputMode(mode);
            }
        } catch (IOException e) {
            log.warn("handle output mode happen error,the reason:{}", e.getMessage());
            throw new RuntimeException("handle output mode happen error");
        }
    }
}

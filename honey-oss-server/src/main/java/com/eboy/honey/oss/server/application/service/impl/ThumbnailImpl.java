package com.eboy.honey.oss.server.application.service.impl;

import com.eboy.honey.oss.entiy.OutputMode;
import com.eboy.honey.oss.entiy.Region;
import com.eboy.honey.oss.entiy.Thumbnail;
import com.eboy.honey.oss.entiy.WaterMark;
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

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
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
    public String upload(File image, String bucketName, MediaType contentType) {
        FileVo fileVo = BeanConvertUtil.convertFileVo(image);
        return fileService.upload(fileVo, bucketName, contentType);
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
     * 处理输出图片质量
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
        if (outputMode != null) {
            String filePath = outputMode.getFilePath();
            try {
                if (!filePath.isEmpty()) {
                    of.toFile(filePath);
                    return;
                }
                OutputStream outputStream = outputMode.getOutputStream();
                of.toOutputStream(outputStream);
                outputMode.setOutputStream(outputStream);
            } catch (IOException e) {
                log.warn("handle output mode happen error,the reason:{}", e.getMessage());
                throw new RuntimeException("handle output mode happen error");
            }
        }
    }
}
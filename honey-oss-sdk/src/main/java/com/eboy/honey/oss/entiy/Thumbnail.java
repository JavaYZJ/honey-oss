package com.eboy.honey.oss.entiy;

import java.io.File;
import java.io.Serializable;

/**
 * @author yangzhijie
 * @date 2020/8/21 10:02
 */
public class Thumbnail implements Serializable {

    private static final long serialVersionUID = 8930079888136360437L;
    /**
     * 原始图片来源
     */
    private File inputSource;
    /**
     * 长 缩放比例
     */
    private double lengthScale = 0.5d;
    /**
     * 宽 缩放比例
     */
    private double widthScale = 0.5d;
    /**
     * 旋转度数
     */
    private double rotate;
    /**
     * 格式转化
     */
    private String outputFormat = "jpg";
    /**
     * 裁剪规则
     */
    private Region region;
    /**
     * 水印
     */
    private WaterMark waterMark;

    /**
     * 输出图片质量
     */
    private double outputQuality = 0.8d;

    /**
     * 输出方式
     */
    private OutputMode outputMode;

    public File getInputSource() {
        return inputSource;
    }

    public void setInputSource(File inputSource) {
        this.inputSource = inputSource;
    }

    public double getLengthScale() {
        return lengthScale;
    }

    public void setLengthScale(double lengthScale) {
        this.lengthScale = lengthScale;
    }

    public double getWidthScale() {
        return widthScale;
    }

    public void setWidthScale(double widthScale) {
        this.widthScale = widthScale;
    }

    public double getRotate() {
        return rotate;
    }

    public void setRotate(double rotate) {
        this.rotate = rotate;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public WaterMark getWaterMark() {
        return waterMark;
    }

    public void setWaterMark(WaterMark waterMark) {
        this.waterMark = waterMark;
    }

    public OutputMode getOutputMode() {
        return outputMode;
    }

    public void setOutputMode(OutputMode outputMode) {
        this.outputMode = outputMode;
    }

    public double getOutputQuality() {
        return outputQuality;
    }

    public void setOutputQuality(double outputQuality) {
        this.outputQuality = outputQuality;
    }
}

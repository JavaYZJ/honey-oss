package com.eboy.honey.oss.job.application.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author yangzhijie
 * @date 2020/9/29 14:04
 */
public class TerrorismRequestParam extends AbstractRequestParam {

    /**
     * 待识别图片
     * image和image_url必须至少提供一个
     */
    private String image;

    /**
     * 待识别图片url
     * image和image_url必须至少提供一个
     */
    @JsonProperty("image_url")
    private String imageUrl;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

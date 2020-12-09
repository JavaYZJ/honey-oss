package red.honey.oss.job.application.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author yangzhijie
 * @date 2020/9/29 11:02
 */
public class PornRequestParamParam extends AbstractRequestParam {

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
    private String image_url;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}

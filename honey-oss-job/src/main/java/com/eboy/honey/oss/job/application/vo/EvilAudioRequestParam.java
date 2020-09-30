package com.eboy.honey.oss.job.application.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author yangzhijie
 * @date 2020/9/29 14:06
 */
public class EvilAudioRequestParam extends AbstractRequestParam {
    /**
     * 语音唯一标识
     */
    @JsonProperty("speech_id")
    private String speechId;

    /**
     * 音频URL，建议音频时长不超过3分钟
     */
    @JsonProperty("speech_url")
    private String speechUrl;

    /**
     * 是否开通音频鉴黄（0-不开通，1-开通，不传默认开通）
     */
    @JsonProperty("porn_detect")
    private int pornDetect;

    /**
     * 是否开通敏感词检测（0-不开通，1-开通，不传默认开通）
     */
    @JsonProperty("keyword_detect")
    private int keywordDetect;

    public String getSpeechId() {
        return speechId;
    }

    public void setSpeechId(String speechId) {
        this.speechId = speechId;
    }

    public String getSpeechUrl() {
        return speechUrl;
    }

    public void setSpeechUrl(String speechUrl) {
        this.speechUrl = speechUrl;
    }

    public int getPornDetect() {
        return pornDetect;
    }

    public void setPornDetect(int pornDetect) {
        this.pornDetect = pornDetect;
    }

    public int getKeywordDetect() {
        return keywordDetect;
    }

    public void setKeywordDetect(int keywordDetect) {
        this.keywordDetect = keywordDetect;
    }
}

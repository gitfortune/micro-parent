package com.company.project.resource.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "convert")
public class ConvertProperties {

    /**
     * ffmpeg路径
     */
    public String ffmpeg;

    /**
     * 转码后临时存放视频路径
     */
    public String videoTemp;

    /**
     * 转码后临时存放音频路径
     */
    public String aideoTemp;

    /**
     *  决定转码后文件是保存到原文件的同级目录下，还是放到wowza指定目录下的 分界线
     */
    public float SeparateSize;

    /**
     * WOWZA ,VOD指定路径
     */
    public String content;
}

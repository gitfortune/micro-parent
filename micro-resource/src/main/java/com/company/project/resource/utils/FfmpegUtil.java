package com.company.project.resource.utils;

import com.company.project.resource.config.ConvertProperties;
import com.company.project.resource.dto.ProcessFileDTO;
import com.company.project.resource.enmu.ResultEnmu;
import com.company.project.resource.exception.ConvertException;
import com.xuggle.xuggler.IContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 音视频转码，ffmpeg工具类
 */


@Slf4j
public class FfmpegUtil {

    @Autowired
    static ConvertProperties properties;

    private static String ffmpegPath;

    private static String videoMp4Path;

    private static String audioMp3Path;

    @Value("${file.ffmpeg.path}")
    public void setFfmpegPath(String path) {
        ffmpegPath = path;
    }

    @Value("${file.videoMp4.path}")
    public void setVideoMp4Path(String outputPath) {
        videoMp4Path = outputPath;
    }

    @Value("${file.audioMp3.path}")
    public void setAudioMp3Path(String outputPath) {
        audioMp3Path = outputPath;
    }

    /**
     * 视频转码
     * @param processFileDTO
     * @return
     */
    public static boolean videoTranscoding(ProcessFileDTO processFileDTO){
        IContainer container = IContainer.make();
        boolean result = false;
        String inputPath = processFileDTO.getFilePath();

        File file = new File(inputPath);
        String name = file.getName();
        String subName = name.substring(0,name.lastIndexOf("."));  //截取文件名字
        List<String> commend = new ArrayList<>();
        commend.add(ffmpegPath);
        commend.add("-i");
        commend.add(inputPath);
//        commend.add("-c:v");
//        commend.add("libx264");
//        commend.add("-s");
//        commend.add("960x540");
        commend.add(videoMp4Path+subName+".mp4");
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process process = builder.start();
            result = true;
        } catch (IOException e) {
            log.error("ffmpeg转码出错了：{}",e.getMessage());
            throw new ConvertException(ResultEnmu.FFMPEG_FAIL);
        }
        return result;

    }

    /**
     * 音频转码
     * @param processFileDTO
     * @return
     */
    public static boolean aideoTranscoding(ProcessFileDTO processFileDTO){
        boolean result = false;

        return result;
    }


}

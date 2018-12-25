package com.company.project.resource.utils;

import com.company.project.resource.config.ConvertProperties;
import com.company.project.resource.dto.ProcessFileDTO;
import com.company.project.resource.enmu.ResultEnmu;
import com.company.project.resource.exception.ConvertException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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

    public static void setProperties(ConvertProperties properties) {
        FfmpegUtil.properties = properties;
    }

    /**
     * 视频转码
     * @param processFileDTO
     * @return
     */
    /*public static String videoTranscoding(ProcessFileDTO processFileDTO){

        String inputPath = processFileDTO.getFilePath();
        File file = new File(inputPath);
        String name = file.getName();
        String subName = name.substring(0,name.lastIndexOf("."));  //截取文件名字
        String outputPath = properties.getVideoTemp()+subName+".mp4";
        List<String> commend = new ArrayList<>();
        commend.add(properties.getFfmpeg());
        commend.add("-i");
        commend.add(inputPath);
//        commend.add("-c:v");
//        commend.add("libx264");
//        commend.add("-s");
//        commend.add("960x540");
        commend.add(outputPath);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            builder.start();
        } catch (IOException e) {
            log.error("ffmpeg视频转码出错了：{}",e.getMessage());
            throw new ConvertException(ResultEnmu.FFMPEG_FAIL);
        }
        return outputPath;

    }*/

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

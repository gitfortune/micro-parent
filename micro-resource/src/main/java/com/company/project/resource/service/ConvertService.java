package com.company.project.resource.service;

import com.company.project.resource.config.ConvertProperties;
import com.company.project.resource.dto.ProcessFileDTO;
import com.company.project.resource.enmu.ResultEnmu;
import com.company.project.resource.entity.MediaInfo;
import com.company.project.resource.exception.ConvertException;
import com.company.project.resource.http.HttpAPIService;
import com.company.project.resource.utils.CheckFileTypeUtil;
import com.company.project.resource.utils.FileUtil;
import com.company.project.resource.utils.ImageUtil;
import com.company.project.resource.utils.XuggleUtil;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * 音视频转码服务，图片处理
 */
@Service
@Slf4j
public class ConvertService {

    @Autowired
    ConvertProperties properties;

    @Autowired
    HttpAPIService httpAPIService;

    /**
     * 转码前的判断
     * @param processFileDTO
     */
    public void consumerMsg(ProcessFileDTO processFileDTO){
        String newPath;
        String filePath = processFileDTO.getFilePath();

        //使用checkType（）方法无法区分avi和wav，需要另作处理
        int fileType = CheckFileTypeUtil.checkType(CheckFileTypeUtil.getFileHeader(filePath));
        if(fileType == CheckFileTypeUtil.CONFUSING){
            String sub = filePath.substring(filePath.lastIndexOf(".")+1);
            if("avi".equals(sub)){
                fileType = CheckFileTypeUtil.VIDEO;
            }else if("wav".equals(sub)){
                fileType = CheckFileTypeUtil.AUDIO;
            }
        }
        log.info("开始处理业务");
        //如果是音、视频，图片，进入处理程序
        if(fileType != CheckFileTypeUtil.UNKNOWN && fileType != CheckFileTypeUtil.EXPECT){

            newPath = this.process(fileType, processFileDTO);

        }else if(fileType == CheckFileTypeUtil.EXPECT){
            //MP3，MP4格式，不需要转码，如果大于xx M存入指定路径
            newPath = this.checkSizeAndMove(processFileDTO);
        }else {

            //无法处理的未知类型
            log.error("转码服务：无法处理的未知类型：{}",filePath);
            throw new ConvertException(ResultEnmu.UNIDENTIFIED);
        }
        try {
            Map map = new HashMap();
            map.put("url",newPath);
            httpAPIService.doPost("http://localhost:8090/callBack",map);
            log.info("回调方法执行完毕！！！！！！！！！！！！！！！！！！！！！！！");
        } catch (Exception e) {
            log.error("回调方法出错：{}",e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 转码业务处理
     * @param fileType
     * @param processFileDTO
     * @return
     */
    public String process(int fileType,ProcessFileDTO processFileDTO) {

        String filePath = processFileDTO.getFilePath();
        String tempPath = null;
        String descPath = null;
        //先判断文件格式
        if(fileType == CheckFileTypeUtil.VIDEO){ //视频

            tempPath = this.videoTranscoding(processFileDTO);
            //转码完成后，获取文件信息
            MediaInfo mediaInfo = XuggleUtil.getMediaInfo(tempPath);
            log.info(mediaInfo.getFileSize()+"M");

            String fileName = tempPath.substring(tempPath.lastIndexOf("/")+1);
            //如果转码后文件小于指定大小，存到原文件同级目录下
            if(mediaInfo.getFileSize() <= properties.getSeparateSize()){
                descPath = filePath.substring(0, filePath.lastIndexOf("/")+1) + fileName;
            }else{
                //如果大于指定大小，上传到WOWZA。
                File file = new File(properties.getContent());
                if(!file.exists()){
                    file.mkdir();
                }
                descPath = properties.getContent()+fileName;
            }
        }else if (fileType == CheckFileTypeUtil.AUDIO){ //音频
            tempPath = this.aideoTranscoding(processFileDTO);
            String fileName = tempPath.substring(tempPath.lastIndexOf("/")+1);
            descPath = filePath.substring(0, filePath.lastIndexOf("/")+1) + fileName;
        }else if(fileType == CheckFileTypeUtil.PICTURE){ //图片
            descPath = this.picProcess(processFileDTO);
            return descPath;
        }
        log.info("最终路径：{}",descPath);
        FileUtil.move(tempPath,descPath);
        return descPath;
    }

    /**
     * 检查大小，然后根据大小移动到指定位置
     * @param processFileDTO
     */
    public String checkSizeAndMove(ProcessFileDTO processFileDTO){
        String filePath = processFileDTO.getFilePath();
        String fileName = new File(filePath).getName();
        MediaInfo mediaInfo = XuggleUtil.getMediaInfo(filePath);
        //文件大于指定大小，存到WOWZA指定目录下，否则不做处理
        if(mediaInfo.getFileSize() > properties.getSeparateSize()){
            //如果大于指定大小，上传到WOWZA。
            File file = new File(properties.getContent());
            if(!file.exists()){
                file.mkdir();
            }
            String descPath = properties.getContent()+fileName;
            FileUtil.move(filePath,descPath);
            return descPath;
        }
        return filePath;
    }

    /**
     * 图片处理
     * @param processFileDTO
     * @return
     */
    public String picProcess(ProcessFileDTO processFileDTO){
        //目前只有加水印和裁剪需求，所以暂时这样处理，以后根据新需求完善
        String srcImg = processFileDTO.getFilePath();
        String fileName = new File(srcImg).getName();
        String uuid = UUID.randomUUID().toString().replace("-", ""); //UUID
        String targerImg = srcImg.replace(fileName.substring(0,fileName.lastIndexOf(".")),uuid);
        //水印图片位置
        Positions positions = ImageUtil.checkPositions(processFileDTO.getPosition());
        String waterMarkPath = processFileDTO.getWaterMarkPath();   //水印路径
        String newImg;
        //缩放比例
        float scale = processFileDTO.getScale() == 0f ? 1f : processFileDTO.getScale();
        //如果没有水印图片地址，则是裁剪图片
        if(StringUtils.isBlank(waterMarkPath)){
            int x = processFileDTO.getX();
            int y = processFileDTO.getY();
            int cutWidth = processFileDTO.getCutWidth();
            int cutHeight = processFileDTO.getCutHeight();
            newImg = ImageUtil.cutImage(srcImg,targerImg,x,y,cutWidth,cutHeight,scale);
        }else{
            float opacity = processFileDTO.getOpacity() == 0f ? 1f : processFileDTO.getOpacity();
            newImg = ImageUtil.putWaterMarker(srcImg,targerImg,waterMarkPath,positions,scale,opacity);
        }
        return newImg;
    }

    /**
     * 视频转码
     * @param processFileDTO
     * @return
     */
    public String videoTranscoding(ProcessFileDTO processFileDTO){
        float scale = processFileDTO.getScale();    //缩放比例
        String rate = processFileDTO.getRate();     //码率
        int height;
        int width;

        File dire = new File(properties.getVideoTemp());
        if(!dire.exists()){
            dire.mkdir();
        }
        String inputPath = processFileDTO.getFilePath();
        File file = new File(inputPath);
        String name = file.getName();
        String subName = name.substring(0,name.lastIndexOf("."));  //截取文件名字
        String outputPath = properties.getVideoTemp()+subName+".mp4";
        List<String> commend = new ArrayList<>();
        commend.add(properties.getFfmpeg());
        commend.add("-i");
        commend.add(inputPath);
        commend.add("-y");  //如果有重名，覆盖掉
        commend.add("-loglevel");
        commend.add("quiet");
//        commend.add("-c:v");
//        commend.add("libx264");

        if(StringUtils.isNotBlank(rate)){
            commend.add("-b:v");
            commend.add(rate+"k");
            commend.add("-bufsize");
            commend.add(rate+"k");
        }
//        -minrate ,-maxrate 在线视频有时候，希望码率波动，不要超过一个阈值，可以设置maxrate。
//        commend.add("-maxrate");
//        commend.add("2500k");

        if(scale != 0){
            //说明需要改变视频分辨率
            MediaInfo mediaInfo = XuggleUtil.getMediaInfo(processFileDTO.getFilePath());
            height = (int) (mediaInfo.getHeight() * scale);
            width = (int) (mediaInfo.getWidth()* scale);
            commend.add("-s");
            commend.add(width+"x"+height);
        }
        commend.add(outputPath);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process process = builder.start();
            process.waitFor();  //执行完才能继续下一步，输出路径
        } catch (IOException | InterruptedException e) {
            log.error("ffmpeg视频转码出错了：{}，转码失败视频：{}",e.getMessage(),inputPath);
            throw new ConvertException(ResultEnmu.FFMPEG_FAIL);
        }
        log.info("转码完成！！！！！！！！");
        return outputPath;
    }

    /**
     * 音频转码
     * @param processFileDTO
     * @return
     */
    public String aideoTranscoding(ProcessFileDTO processFileDTO) {
        String rate = processFileDTO.getRate();     //码率
        File dire = new File(properties.getAideoTemp());
        if(!dire.exists()){
            dire.mkdir();
        }
        String inputPath = processFileDTO.getFilePath();
        File file = new File(inputPath);
        String name = file.getName();
        String subName = name.substring(0,name.lastIndexOf("."));  //截取文件名字
        String outputPath = properties.getAideoTemp()+subName+".mp3";
        List<String> commend = new ArrayList<>();
        commend.add(properties.getFfmpeg());
        commend.add("-i");
        commend.add(inputPath);
        commend.add("-y");  //如果有重名，覆盖掉
        commend.add("-loglevel");
        commend.add("quiet");
//        commend.add("-c:v");
//        commend.add("libx264");
        if(StringUtils.isNotBlank(rate)){
            commend.add("-b:v");
            commend.add(rate+"k");
            commend.add("-bufsize");
            commend.add(rate+"k");
        }
        commend.add(outputPath);
        try {

            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process process = builder.start();
            process.waitFor();  //执行完才能继续下一步，输出路径
        } catch (IOException | InterruptedException e) {
            log.error("ffmpeg音频转码出错了：{}，转码失败音频：{}",e.getMessage(),inputPath);
            throw new ConvertException(ResultEnmu.FFMPEG_FAIL);
        }
        log.info("音频转码完成！！！！！！！！");
        return outputPath;
    }
}

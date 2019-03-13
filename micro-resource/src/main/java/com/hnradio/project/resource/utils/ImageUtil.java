package com.hnradio.project.resource.utils;

import com.hnradio.project.resource.enmu.ResultEnmu;
import com.hnradio.project.resource.exception.ConvertException;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * 图片处理工具类
 */
@Slf4j
public class ImageUtil {

    /**
     * 裁剪
     * @param srcImg
     * @param targetImg
     * @param x
     * @param y
     * @param width
     * @param height
     * @param scale
     */
    public static String cutImage(String srcImg, String targetImg,int x,int y,int width,int height,float scale) {
        try {
            Thumbnails.of(srcImg)
                    .sourceRegion(x,y,width,height)  //裁剪图片：x轴、y轴，裁剪宽、裁剪高
                    .scale(scale)//缩放比例,默认不缩放
                    .toFile(targetImg);
        } catch (IOException e) {
            log.error("裁剪图片异常：{}",e.getMessage());
            throw new ConvertException(ResultEnmu.THUMBNAILS_FAIL);
        }
        return targetImg;
    }

    /**
     * 加水印
     * @param srcImg
     * @param targetImg
     * @param waterImg
     * @param scale
     * @param opacity
     */
    public static String putWaterMarker(String srcImg,String targetImg,String waterImg,Positions positions,
                                        float scale,float opacity){
        try {
            Thumbnails.of(srcImg)
                    .watermark(positions, ImageIO.read(new File(waterImg)), opacity == 0f ? 0.8f : opacity)
                    .outputQuality(1f)
                    .scale(scale == 0f ? 1f : scale)//缩放比例,默认不缩放
                    .toFile(targetImg);
        } catch (IOException e) {
            log.error("添加水印图片异常：{}",e.getMessage());
            throw new ConvertException(ResultEnmu.THUMBNAILS_FAIL);
        }
        log.info(targetImg);
        return targetImg;
    }

    public static Positions checkPositions(int i){
        switch (i){
            case 1:
                return Positions.TOP_LEFT;
            case 2:
                return Positions.BOTTOM_LEFT;
            case 3:
                return Positions.TOP_RIGHT;
            case 4:
                return Positions.BOTTOM_RIGHT;
            case 5:
                return Positions.CENTER;
            default:
                return Positions.BOTTOM_RIGHT;
        }
    }
}

package com.company.project.resource.utils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * 图片处理工具类
 */
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
    public static void cutImage(String srcImg, String targetImg,int x,int y,int width,int height,float scale) {
        try {
            Thumbnails.of(srcImg)
                    .sourceRegion(x,y,width,height)  //裁剪图片：x轴、y轴，裁剪宽、裁剪高
                    .scale(scale)//缩放比例,默认不缩放
                    .toFile(targetImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加水印
     * @param srcImg
     * @param targetImg
     * @param waterImg
     * @param scale
     * @param opacity
     */
    public static void putWaterMarker(String srcImg,String targetImg,String waterImg,float scale,float opacity){
        try {
            Thumbnails.of(srcImg)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(waterImg)), opacity == 0f ? 0.8f : opacity)
                    .outputQuality(1f)
                    .scale(scale == 0f ? 1f : scale)//缩放比例,默认不缩放
                    .toFile(targetImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

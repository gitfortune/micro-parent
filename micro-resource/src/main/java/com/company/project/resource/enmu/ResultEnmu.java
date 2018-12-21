package com.company.project.resource.enmu;

import lombok.Getter;

@Getter
public enum ResultEnmu {

    LOGIN_FAIL(1,"登陆失败，用戶名或密碼不正确"),

    UNIDENTIFIED(2,"无法识别文件格式"),

    FFMPEG_FAIL(3,"ffmpeg执行转码出现异常"),

    THUMBNAILATOR_FAIL(4,"Thumbnailator出现异常"),

    FILE_NOT_FOUND(5,"找不到该文件"),





    ;



    private int code;

    private String msg;

    ResultEnmu(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}

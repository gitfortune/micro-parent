package com.hnradio.project.resource.exception;

import com.hnradio.project.resource.enmu.ResultEnmu;
import lombok.Data;

@Data
public class ConvertException extends RuntimeException {

    private int code;

    public ConvertException (ResultEnmu resultEnmu){
        super(resultEnmu.getMsg());

        this.code = resultEnmu.getCode();

    }

    public ConvertException(int code,String msg){
        super(msg);
        this.code = code;
    }
}

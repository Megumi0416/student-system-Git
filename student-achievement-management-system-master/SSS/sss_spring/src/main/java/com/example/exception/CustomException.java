package com.example.exception;

import com.example.common.enums.ResultCodeEnum;

public class CustomException extends RuntimeException {

    private String msg;
    private String code;

    public CustomException(String code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public CustomException(ResultCodeEnum resultCodeEnum) {
        this.msg = resultCodeEnum.msg;
        this.code = resultCodeEnum.code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

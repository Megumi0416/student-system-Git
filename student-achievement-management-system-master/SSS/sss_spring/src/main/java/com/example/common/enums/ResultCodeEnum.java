package com.example.common.enums;

public enum ResultCodeEnum {

    SUCCESS("200", "成功"),
    // 通用错误
    INTERNAL_SERVER_ERROR("500", "服务器内部错误"),
    BAD_REQUEST("400", "请求参数错误"),
    UNAUTHORIZED("401", "未授权"),
    FORBIDDEN("403", "禁止访问"),
    NOT_FOUND("404", "资源未找到"),
    METHOD_NOT_ALLOWED("405", "请求方法不允许"),
    REQUEST_TIMEOUT("408", "请求超时"),
    TOO_MANY_REQUESTS("429", "请求过多"),
    SERVICE_UNAVAILABLE("503", "服务不可用"),
    DO_NOT_REPEAT_OPERATION("504","请勿重复操作"),
    COURSE_ALREADY_SELECTED("406","您已选过这个课程"),

    // 参数错误
    INVALID_PARAM("1001", "参数无效"),
    MISSING_PARAM("1002", "缺少必要参数"),
    TOKEN_INVALID_ERROR("1003","无效的token"),
    TOKEN_CHECK_ERROR("1004","token验证失败，请重新登录"),

    // 用户相关错误
    USER_NOT_FOUND("2001", "用户未找到"),
    USER_ALREADY_EXISTS("2002", "用户已存在"),
    USER_NOT_LOGIN("2003","用户未登录"),
    USER_PASSWORD_INCORRECT("2004", "账户或密码错误"),
    ACCOUNT_DISABLED("2005", "账户已禁用"),
    USER_NOT_EXIST_ERROR("2006","用户不存在"),
    PARAM_PASSWORD_ERROR("2007","原密码不一致"),

    // 资源相关错误
    RESOURCE_NOT_FOUND("3001", "资源未找到"),
    RESOURCE_CONFLICT("3002", "资源冲突"),
    OPERATION_NOT_ALLOWED("3003", "操作不允许"),


    // 数据库相关错误
    DATABASE_ERROR("4001", "数据库错误"),
    ENTITY_NOT_FOUND("4002", "实体未找到"),
    ENTITY_ALREADY_EXISTS("4003", "实体已存在"),

    // 系统维护
    MAINTENANCE("5001", "系统维护中，请稍后重试");

    public String code;
    public String msg;
    ResultCodeEnum(String code, String msg) {
        this.code =code;
        this.msg = msg;
    }


}

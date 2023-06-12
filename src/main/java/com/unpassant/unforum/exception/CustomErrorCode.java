package com.unpassant.unforum.exception;

public enum CustomErrorCode implements ICustomErrorCode{

    POST_NOT_FOUND(2001,"帖子不存在"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    NO_LOGIN(2003,"当前操作需要登录，请登录后重试"),
    SYS_ERROR(2004,"服务器异常，It Just Doesn't Work"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"操作的评论不存在");


    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private  String message;

    CustomErrorCode(Integer code,String message) {
        this.message = message;
        this.code = code;
    }
}

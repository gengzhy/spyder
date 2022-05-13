package xin.cosmos.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * http请求响应结果
 */
@Data
public class R<T> {
    private int code;// 响应码
    private boolean success;// 是否响应成功
    private String message;// 响应消息
    private T data;// 响应数据体

    // 响应成功
    @JsonIgnore
    final static int DEFAULT_SUCCESS = 200;

    @JsonIgnore
    final static String DEFAULT_SUCCESS_MSG = "操作成功！";

    public R(T data) {
        this(DEFAULT_SUCCESS, true, DEFAULT_SUCCESS_MSG, data);
    }

    public R(int code, boolean success, String message, T data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> R<T> ok(T data) {
        return new R(data);
    }

    public static <T> R<T> ok() {
        return new R(null);
    }

    public static R failed(boolean success, String message) {
        return new R(DEFAULT_SUCCESS, success, message, null);
    }
}

package com.wanli.WanliStore.utils;

public class Result<T> {

    private Integer code;     // 状态码
    private String message;   // 提示信息
    private T data;           // 具体数据

    // ===== Getter / Setter =====
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // ===== 静态工厂方法 =====

    // 成功返回（带数据）
    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMessage("success");
        r.setData(data);
        return r;
    }

    // 成功返回（无数据）
    public static <T> Result<T> success() {
        return success(null);
    }

    // 失败返回（自定义状态码和消息）
    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(message);
        r.setData(null);
        return r;
    }

    // 通用失败（默认400）
    public static <T> Result<T> fail(String message) {
        return fail(400, message);
    }

    // ===== 可选：toString() 方法（便于调试打印） =====
    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

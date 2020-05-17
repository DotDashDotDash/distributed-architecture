package org.codwh.redis.monitor.service.constants;

public enum EnvType {

    DEVELOP(1, "开发"), INTEGRATED(2, "集成"), PRODUCE(3, "生产");

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    EnvType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

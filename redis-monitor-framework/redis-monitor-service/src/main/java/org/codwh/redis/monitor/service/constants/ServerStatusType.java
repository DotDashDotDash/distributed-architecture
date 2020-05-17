package org.codwh.redis.monitor.service.constants;

public enum ServerStatusType {

    ENABLED(1, "有效"), DISABLED(2, "失效");

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

    ServerStatusType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

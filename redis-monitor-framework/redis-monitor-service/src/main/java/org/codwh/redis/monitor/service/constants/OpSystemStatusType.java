package org.codwh.redis.monitor.service.constants;

public enum OpSystemStatusType {

    DOING(1, "正在做"), DONE(2, "已完成");

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

    OpSystemStatusType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(int code) {
        for (OpSystemStatusType t : OpSystemStatusType.values()) {
            if (t.getCode() == code) {
                return t.message;
            }
        }
        return null;
    }
}

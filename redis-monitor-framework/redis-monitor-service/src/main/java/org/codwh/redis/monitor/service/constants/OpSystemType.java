package org.codwh.redis.monitor.service.constants;

public enum OpSystemType {

    LOCAL(1, "本地"), REMOTE(2, "远程");

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

    OpSystemType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(int code) {
        for (OpSystemType t : OpSystemType.values()) {
            if (t.code == code) {
                return t.message;
            }
        }
        return null;
    }
}

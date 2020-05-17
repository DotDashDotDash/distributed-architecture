package org.codwh.redis.monitor.service.constants;

public enum LeafType {

    YES(1, "是"), NO(2, "否");

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

    LeafType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

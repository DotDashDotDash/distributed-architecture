package org.codwh.redis.monitor.service.constants;

public enum ConnectionType {

    HTTP(1, "HTTP"), HTTPS(2, "HTTPS"), TCP(3, "TCP");

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

    ConnectionType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(int code) {
        for (ConnectionType t : ConnectionType.values()) {
            if (t.getCode() == code) {
                return t.message;
            }
        }
        return null;
    }
}

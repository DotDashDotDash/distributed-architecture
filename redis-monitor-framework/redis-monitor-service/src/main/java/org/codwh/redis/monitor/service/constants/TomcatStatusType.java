package org.codwh.redis.monitor.service.constants;

import org.codwh.common.constants.TomcatStatus;

public enum TomcatStatusType {

    RUNNING(1, "运行"), STOP(2, "停止");

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

    TomcatStatusType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(int code) {
        for (TomcatStatusType t : TomcatStatusType.values()) {
            if (t.getCode() == code) {
                return t.message;
            }
        }
        return null;
    }
}

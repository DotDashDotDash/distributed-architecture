package org.codwh.redis.monitor.service.constants;

public enum JobStatusType {

    DRAFT(1, "草稿"), ENABLE(2, "启动"), DISABLE(3, "停用");

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

    JobStatusType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(int code) {
        for (JobStatusType t : JobStatusType.values()) {
            if (t.getCode() == code) {
                return t.message;
            }
        }
        return null;
    }
}

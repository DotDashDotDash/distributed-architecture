package org.codwh.redis.monitor.service.constants;

public enum JobType {

    NON_BLOCK(1, "并行", "org.codwh.redis.monitor.service.util.ConcurrentExecutionJobQuartz"),
    BLOCK(2, "串行", "org.codwh.redis.monitor.service.util.ConcurrentExecutionJobQuartz");

    private Integer code;
    private String message;
    private String className;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static String getMessage(Integer code) {
        for (JobType t : JobType.values()) {
            if (t.getCode() == code) {
                return t.message;
            }
        }
        return null;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getClassName() {
        return className;
    }

    public static String getClass(Integer code) {
        for (JobType t : JobType.values()) {
            if (t.getCode() == code) {
                return t.className;
            }
        }
        return null;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    JobType(Integer code, String message, String className) {
        this.code = code;
        this.message = message;
        this.className = className;
    }
}

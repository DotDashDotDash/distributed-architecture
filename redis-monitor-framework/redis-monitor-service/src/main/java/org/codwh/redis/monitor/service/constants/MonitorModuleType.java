package org.codwh.redis.monitor.service.constants;

public enum MonitorModuleType {

    JMX(1, "JMX"), OLD(2, "老年代"), PERM(3, "永久代"), CPU(4, "CPU"),
    TOMCAT(5, "TOMCAT"), QUARTZ(6, "QUARTZ"), THREAD(7, "THREAD");

    private int code;
    private String message;

    MonitorModuleType(int code, String message) {
        this.code = code;
        this.message = message;
    }

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

    public static String getMessage(int code) {
        for (MonitorModuleType t : MonitorModuleType.values()) {
            if (t.code == code) {
                return t.message;
            }
        }
        return null;
    }
}

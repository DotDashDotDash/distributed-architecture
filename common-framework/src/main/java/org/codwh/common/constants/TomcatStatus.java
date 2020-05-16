package org.codwh.common.constants;

public enum TomcatStatus {


    RUNNING(1, "运行"),
    STOP(2, "停止");

    private Integer code;

    private String message;

    /**
     * @param code
     * @param message
     */
    TomcatStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(int code) {
        for (TomcatStatus c : TomcatStatus.values()) {
            if (c.getCode() == code) {
                return c.message;
            }
        }
        return null;
    }

    /**
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}

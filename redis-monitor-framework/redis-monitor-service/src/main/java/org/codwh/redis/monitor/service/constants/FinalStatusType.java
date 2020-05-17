package org.codwh.redis.monitor.service.constants;

public enum FinalStatusType {

    SUCCESS(1, "成功"), FAIL(2, "失败");

    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    FinalStatusType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

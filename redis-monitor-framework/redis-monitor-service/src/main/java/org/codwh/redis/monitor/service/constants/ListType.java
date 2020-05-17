package org.codwh.redis.monitor.service.constants;

public enum ListType {

    DATA_GRID(1, "data-grid"), TREE_GRID(2, "tree-grid");

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

    ListType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

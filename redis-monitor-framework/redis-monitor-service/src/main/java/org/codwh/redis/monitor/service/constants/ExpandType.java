package org.codwh.redis.monitor.service.constants;

public enum ExpandType {

    TRUE(1, true), FALSE(2, false);

    private int code;
    private boolean flag;

    ExpandType(int code, boolean flag) {
        this.code = code;
        this.flag = flag;
    }
}

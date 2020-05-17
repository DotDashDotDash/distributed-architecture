package org.codwh.common.util.JVM;

public class TomcatThreadBean {

    private Long maxThreads;//最大线程

    private Long currentThreadCount;//当前线程总数

    private Long currentThreadsBusy;//繁忙线程

    private int status;//当前状态

    private String name;//名称

    public Long getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(Long maxThreads) {
        this.maxThreads = maxThreads;
    }

    public Long getCurrentThreadCount() {
        return currentThreadCount;
    }

    public void setCurrentThreadCount(Long currentThreadCount) {
        this.currentThreadCount = currentThreadCount;
    }

    public Long getCurrentThreadsBusy() {
        return currentThreadsBusy;
    }

    public void setCurrentThreadsBusy(Long currentThreadsBusy) {
        this.currentThreadsBusy = currentThreadsBusy;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

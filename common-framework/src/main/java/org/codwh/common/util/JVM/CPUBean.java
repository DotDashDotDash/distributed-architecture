package org.codwh.common.util.JVM;

public class CPUBean {

    private Long processCpuTime;
    private Integer availableProcessors;

    public CPUBean(Long processCpuTime, Integer availableProcessors) {
        super();
        this.processCpuTime = processCpuTime;
        this.availableProcessors = availableProcessors;
    }

    public CPUBean() {
        super();
    }

    public Long getProcessCpuTime() {
        return processCpuTime;
    }

    public void setProcessCpuTime(Long processCpuTime) {
        this.processCpuTime = processCpuTime;
    }

    public Integer getAvailableProcessors() {
        return availableProcessors;
    }

    public void setAvailableProcessors(Integer availableProcessors) {
        this.availableProcessors = availableProcessors;
    }


}
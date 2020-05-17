package org.codwh.common.util.JVM;

public class HeapMemoryGarbage {

    /**
     * 年轻代回收总量
     */
    private Long youngCollectionCount;

    /**
     * 年轻代回收时间
     */
    private Long youngCollectionTime;

    /**
     * 老年代回收总量
     */
    private Long oldCollectionCount;

    /**
     * 老年代回收时间
     */
    private Long oldCollectionTime;


    public Long getYoungCollectionCount() {
        return youngCollectionCount;
    }


    public void setYoungCollectionCount(Long youngCollectionCount) {
        this.youngCollectionCount = youngCollectionCount;
    }


    public Long getYoungCollectionTime() {
        return youngCollectionTime;
    }


    public void setYoungCollectionTime(Long youngCollectionTime) {
        this.youngCollectionTime = youngCollectionTime;
    }


    public Long getOldCollectionCount() {
        return oldCollectionCount;
    }


    public void setOldCollectionCount(Long oldCollectionCount) {
        this.oldCollectionCount = oldCollectionCount;
    }


    public Long getOldCollectionTime() {
        return oldCollectionTime;
    }


    public void setOldCollectionTime(Long oldCollectionTime) {
        this.oldCollectionTime = oldCollectionTime;
    }


}
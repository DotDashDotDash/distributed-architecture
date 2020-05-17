package org.codwh.common.util.JVM;

public class MemorySpaceBean {

    private long edenInit;
    private long edenUsed;
    private long edenMax;
    private long edenCommitted;
    private long survivorInit;
    private long survivorUsed;
    private long survivorMax;
    private long survivorCommitted;
    private long oldInit;
    private long oldUsed;
    private long oldMax;
    private long oldCommitted;
    private long permInit;
    private long permUsed;
    private long permMax;
    private long permCommitted;

    public long getEdenInit() {
        return edenInit;
    }

    public void setEdenInit(long edenInit) {
        this.edenInit = edenInit;
    }

    public long getEdenUsed() {
        return edenUsed;
    }

    public void setEdenUsed(long edenUsed) {
        this.edenUsed = edenUsed;
    }

    public long getEdenMax() {
        return edenMax;
    }

    public void setEdenMax(long edenMax) {
        this.edenMax = edenMax;
    }

    public long getSurvivorInit() {
        return survivorInit;
    }

    public void setSurvivorInit(long survivorInit) {
        this.survivorInit = survivorInit;
    }

    public long getSurvivorUsed() {
        return survivorUsed;
    }

    public void setSurvivorUsed(long survivorUsed) {
        this.survivorUsed = survivorUsed;
    }

    public long getSurvivorMax() {
        return survivorMax;
    }

    public void setSurvivorMax(long survivorMax) {
        this.survivorMax = survivorMax;
    }

    public long getOldInit() {
        return oldInit;
    }

    public void setOldInit(long oldInit) {
        this.oldInit = oldInit;
    }

    public long getOldUsed() {
        return oldUsed;
    }

    public void setOldUsed(long oldUsed) {
        this.oldUsed = oldUsed;
    }

    public long getOldMax() {
        return oldMax;
    }

    public void setOldMax(long oldMax) {
        this.oldMax = oldMax;
    }

    public long getPermInit() {
        return permInit;
    }

    public void setPermInit(long permInit) {
        this.permInit = permInit;
    }

    public long getPermUsed() {
        return permUsed;
    }

    public void setPermUsed(long permUsed) {
        this.permUsed = permUsed;
    }

    public long getPermMax() {
        return permMax;
    }

    public void setPermMax(long permMax) {
        this.permMax = permMax;
    }

    public long getEdenCommitted() {
        return edenCommitted;
    }

    public void setEdenCommitted(long edenCommitted) {
        this.edenCommitted = edenCommitted;
    }

    public long getSurvivorCommitted() {
        return survivorCommitted;
    }

    public void setSurvivorCommitted(long survivorCommitted) {
        this.survivorCommitted = survivorCommitted;
    }

    public long getOldCommitted() {
        return oldCommitted;
    }

    public void setOldCommitted(long oldCommitted) {
        this.oldCommitted = oldCommitted;
    }

    public long getPermCommitted() {
        return permCommitted;
    }

    public void setPermCommitted(long permCommitted) {
        this.permCommitted = permCommitted;
    }
}

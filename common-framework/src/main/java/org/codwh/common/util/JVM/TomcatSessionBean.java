package org.codwh.common.util.JVM;

public class TomcatSessionBean {

    private String frameworkname;

    private Long maxActiveSessions;

    private Long activeSessions;

    private Long sessionCounter;

    public TomcatSessionBean(String frameworkName, Long maxActiveSessions,
                             Long activeSessions, Long sessionCounter) {
        super();
        this.frameworkname = frameworkName;
        this.maxActiveSessions = maxActiveSessions;
        this.activeSessions = activeSessions;
        this.sessionCounter = sessionCounter;
    }

    public TomcatSessionBean() {
        super();
    }

    public String getFrameworkName() {
        return frameworkname;
    }

    public void setFrameworkName(String frameworkName) {
        this.frameworkname = frameworkName;
    }

    public Long getMaxActiveSessions() {
        return maxActiveSessions;
    }


    public void setMaxActiveSessions(Long maxActiveSessions) {
        this.maxActiveSessions = maxActiveSessions;
    }

    public Long getActiveSessions() {
        return activeSessions;
    }

    public void setActiveSessions(Long activeSessions) {
        this.activeSessions = activeSessions;
    }

    public Long getSessionCounter() {
        return sessionCounter;
    }

    public void setSessionCounter(Long sessionCounter) {
        this.sessionCounter = sessionCounter;
    }
}

package org.codwh.redis.zookeeper.factory;

import org.I0Itec.zkclient.ZkClient;

public class RegistryFactory implements IRegistryFactory {

    private String addressUrl;

    private int sessionTimeout;

    private int timeout;

    @Override
    public ZkClient getZkClient(String addressUrl, int sessionTimeout, int timeout) {
        ZkClient zkClient = new ZkClient(addressUrl, sessionTimeout, timeout);
        return zkClient;
    }

    public String getAddressUrl() {
        return addressUrl;
    }

    public void setAddressUrl(String addressUrl) {
        this.addressUrl = addressUrl;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}

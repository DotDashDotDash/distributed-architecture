package org.codwh.redis.zookeeper.factory;

import org.I0Itec.zkclient.ZkClient;

public interface IRegistryFactory {

    /**
     * 获取zk客户端
     *
     * @param addressUrl     地址url
     * @param sessionTimeout 会话超时时间
     * @param timeout        客户端连接超时时间
     * @return
     */
    ZkClient getZkClient(String addressUrl, int sessionTimeout, int timeout);
}

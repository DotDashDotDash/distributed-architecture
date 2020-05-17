package org.codwh.redis.zookeeper.util;

import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.xpath.internal.res.XPATHErrorResources_zh_TW;
import org.I0Itec.zkclient.ZkClient;
import org.codwh.common.util.StringUtils;
import org.codwh.redis.zookeeper.constants.ZkConstants;
import org.codwh.redis.zookeeper.constants.ZkType;
import org.codwh.redis.zookeeper.factory.IRegistryFactory;
import org.codwh.redis.zookeeper.factory.RegistryFactory;

import java.util.List;


public class ZkUtils {

    /**
     * ZooKeeper连接的地址
     */
    private String addressUrl;

    /**
     * Session超时时间
     */
    private int sessionTimeout;

    /**
     * ZooKeeper连接过期时间
     */
    private int timeout;

    /**
     * ZooKeeper客户端
     */
    private ZkClient zkClient;

    /**
     * ZooKeeper客户端初始化
     */
    public void init() {
        IRegistryFactory registryFactory = new RegistryFactory();
        zkClient = registryFactory.getZkClient(addressUrl, sessionTimeout, timeout);
    }

    /**
     * 向对应ZooKeeper节点中写入数据
     *
     * @param parentPath
     * @param path
     * @param data
     */
    public void setNodeData(String parentPath, String path, String data) {
        try {
            path = toNodePath(parentPath, path, ZkType.AVAILABLE_SERVER);
            zkClient.writeData(path, data);
        } catch (Exception e) {
            throw new RuntimeException("设置ZooKeeper对应路径的数据失败!");
        }
    }

    /**
     * 读取ZooKeeper节点中的数据
     *
     * @param parentPath
     * @param path
     * @return
     */
    public String getNodeData(String parentPath, String path) {
        String data = null;
        try {
            path = toNodePath(parentPath, path, ZkType.AVAILABLE_SERVER);
            data = zkClient.readData(path, true);
        } catch (Exception e) {
            throw new RuntimeException("读取ZooKeeper节点中的数据失败!");
        }
        return data;
    }

    /**
     * 获取ZooKeeper节点的孩子节点
     *
     * @param parentPath
     * @param path
     * @return
     */
    public List<String> getChildren(String parentPath, String path) {
        List<String> res = null;
        try {
            path = toNodePath(parentPath, path, ZkType.AVAILABLE_SERVER);
            return zkClient.getChildren(path);
        } catch (Exception e) {
            throw new RuntimeException("获取ZooKeeper节点的孩子节点失败!");
        }
    }

    /**
     * 获取ZooKeeper所有可用节点
     *
     * @return
     */
    public List<String> getAllChildren() {
        try {
            String path = toNodeTypePath(ZkType.AVAILABLE_SERVER);
            return zkClient.getChildren(path);
        } catch (Exception e) {
            throw new RuntimeException("获取ZooKeeper所有孩子节点失败!");
        }
    }

    /**
     * 删除指定ZooKeeper节点
     *
     * @param parentPath
     * @param path
     * @param zkType
     * @return
     */
    public boolean deleteNode(String parentPath, String path, ZkType zkType) {
        try {
            path = toNodePath(parentPath, path, zkType);
            return zkClient.delete(path);
        } catch (Exception e) {
            throw new RuntimeException("删除ZooKeeper节点失败!");
        }
    }

    /**
     * 删除所有可用ZooKeeper节点
     *
     * @return
     */
    public boolean deleteAllNodes() {
        try {
            String path = toNodeTypePath(ZkType.AVAILABLE_SERVER);
            return zkClient.delete(path);
        } catch (Exception e) {
            throw new RuntimeException("删除ZooKeeper所有孩子节点失败!");
        }
    }

    /**
     * 查询是否存在执行ZooKeeper节点
     *
     * @param parentPath
     * @param path
     * @param zkType
     * @return
     */
    public boolean exist(String parentPath, String path, ZkType zkType) {
        try {
            path = toNodePath(parentPath, path, zkType);
            return zkClient.exists(path);
        } catch (Exception e) {
            throw new RuntimeException("执行是否存在ZooKeeper节点异常!");
        }
    }

    /**
     * 关闭ZooKeeper客户端
     */
    public void closeZkClient() {
        zkClient.close();
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

    public ZkClient getZkClient() {
        return zkClient;
    }

    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    /**
     * 找到ZooKeeper节点的开始存储的位置
     *
     * @return
     */
    public static String toServicePath() {
        return ZkConstants.ZOOKEEPER_REGISTRY_NAMESPACE;
    }

    /**
     * command为前缀
     *
     * @return
     */
    public static String toCommandPath() {
        return toServicePath() + ZkConstants.ZOOKEEPER_REGISTRY_COMMAND;
    }

    /**
     * node实际存储的路径
     *
     * @param zkType
     * @return
     */
    public static String toNodeTypePath(ZkType zkType) {
        String type = null;
        if (zkType == ZkType.AVAILABLE_SERVER) {
            type = "available_server";
        } else if (zkType == ZkType.UN_AVAILABLE_SERVER) {
            type = "un_available_server";
        } else if (zkType == ZkType.CLIENT) {
            type = "client";
        } else {
            throw new RuntimeException("无法解析的ZkType");
        }
        return toCommandPath() + ZkConstants.PATH_SEPARATOR + type;
    }

    public static String toNodePath(String parentPath, String serverKey, ZkType zkType) {
        if (StringUtils.isNotEmpty(parentPath)) {
            return toNodeTypePath(zkType) + ZkConstants.PATH_SEPARATOR + parentPath + ZkConstants.PATH_SEPARATOR
                    + serverKey;
        }
        return toNodeTypePath(zkType) + ZkConstants.PATH_SEPARATOR + serverKey;
    }
}

package org.codwh.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class IPUtils {

    static Logger logger = LoggerFactory.getLogger(IPUtils.class);

    /**
     * 获取本机内网IP地址的方法
     *
     * @return
     */
    public static String getHost() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address != null
                            && address instanceof Inet4Address
                            && !address.isLoopbackAddress()
                            && address.getHostAddress().indexOf(":") == -1) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }
}

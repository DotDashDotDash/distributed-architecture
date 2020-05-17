package org.codwh.redis.monitor.service.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.codwh.common.util.DateUtils;
import org.codwh.common.util.StringUtils;
import org.codwh.redis.custom.model.redis.RedisConfigInfo;
import org.codwh.redis.monitor.service.service.IRedisService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Date;

@Service("redisService")
public class RedisService implements IRedisService {

    @Override
    public RedisConfigInfo getRedisConfigInfo(String host, int port) {
        Jedis jedis = new Jedis(host, port, 5000);
        String redisInfo = jedis.info();

        if (StringUtils.isNotEmpty(redisInfo)) {
            String[] data = redisInfo.split("\r\n");
            JSONObject jsonObject = new JSONObject();
            for (String d : data) {
                if (StringUtils.isNotEmpty(d) && !d.contains("#") && d.contains(":")) {
                    String[] values = d.split(":");
                    if (values[0].equals("connected_clients")) {
                        jsonObject.put("clientConnection", Integer.parseInt(values[1]));
                    } else if (values[0].equals("total_commands_processed")) {
                        jsonObject.put("totalCommands", Integer.parseInt(values[1]));
                    } else if (values[0].equals("used_memory_peak_human")) {
                        jsonObject.put("sysMemory", values[1].substring(0, values[1].length() - 1));
                    } else if (values[0].equals("used_memory_human")) {
                        jsonObject.put("usedMemory", values[1].substring(0, values[1].length() - 1));
                    } else if (values[0].equals("keyspace_hits")) {
                        jsonObject.put("keyHits", Double.parseDouble(values[1]));
                    } else if (values[0].equals("keyspace_misses")) {
                        jsonObject.put("keyMiss", Double.parseDouble(values[1]));
                    } else if (values[0].equals("used_cpu_sys")) {
                        jsonObject.put("usedSystemCPU", Double.parseDouble(values[1]));
                    } else if (values[0].equals("used_cpu_user")) {
                        jsonObject.put("usedHumanCPU", Double.parseDouble(values[1]));
                    }
                }
            }
            if (!jsonObject.isEmpty()) {
                jsonObject.put("timestamp", DateUtils.formatDate(new Date(), DateUtils.DATETIME_FORMAT));
                RedisConfigInfo redisConfigInfo = JSONObject.parseObject(jsonObject.toJSONString(), RedisConfigInfo.class);
                return redisConfigInfo;
            }
        }
        jedis.close();
        return null;
    }
}

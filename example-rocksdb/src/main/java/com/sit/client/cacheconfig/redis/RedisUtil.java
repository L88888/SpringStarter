package com.sit.client.cacheconfig.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className RedisUtil
 * @description Redis工具类
 * @date 2021-02-03 0003 下午 18:33
 **/
@Component
public class RedisUtil {

    /**
     * 设置redisTemplate初始化为自定义方式
     * @param lettuceConnectionFactory LettuceConnectionFactory
     * @return RedisTemplate<String, Object>
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        return serializer(redisTemplate);
    }

    /**
     * 设置序列化
     * @param redisTemplate RedisTemplate
     * @return RedisTemplate<String, Object>
     */
    private RedisTemplate<String, Object> serializer(RedisTemplate<String, Object> redisTemplate) {
        // 使用 FastJsonRedisSerializer 来序列化和反序列化redis 的 value的值
        FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<>(Object.class);

        ParserConfig.getGlobalInstance().addAccept("com.sailing");
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);
        serializer.setFastJsonConfig(fastJsonConfig);

        // key 的 String 序列化采用 StringRedisSerializer
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        // value 的值序列化采用 fastJsonRedisSerializer
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Autowired(required=true)
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 发布消息到redis频道中
     * @param topicName 频道名称
     * @param message 消息数据对象
     */
    public void pusLish(String topicName, Object message) {
        Assert.notNull(topicName,"发布消息的频道不可用.频道名称为:" + topicName);
        Assert.notNull(message,"发布消息的内容不可用.");
        this.redisTemplate.convertAndSend(topicName, message);
    }

    /**
     * 批量保存对象
     * val: [{"deviceId":"31011811001320021008","deviceName":"广场北口1"}]
     * key: deviceId
     * 存储到redis的结构
     * 31011811001320021008 : {"deviceId":"31011811001320021008","deviceName":"广场北口1"}
     *
     * @param key 对象中的key
     * @param val 对象集合
     */
    public void saveBatch(String key, List<Map> val){
        this.setDatabase();
        this.set(key, val);
        this.setDatabase();
    }
    public void ssaveBatch(Map map){
        this.setDatabase(6);
        this.set(map);
    }
    /**
     * 指定database index 批量保存对象
     * val: [{"deviceId":"31011811001320021008","deviceName":"广场北口1"}]
     * key: deviceId
     * 存储到redis的结构
     * 31011811001320021008 : {"deviceId":"31011811001320021008","deviceName":"广场北口1"}
     *
     * @param key 对象中的key
     * @param val 对象集合
     * @param database 数据库Index
     */
    public void saveBatch(String key, List<Map> val, int database){
        this.setDatabase(database);
        this.set(key, val);
        this.setDatabase();
    }

    /**
     * 指定Database，批量获取多个数据
     * @param key Redis key
     * @param database 数据库Index
     * @return Object
     */
    public Object getValue(String key, int database){
        this.setDatabase(database);
        List<Object> list = this.get(new ArrayList<String>() {
            {
                add(key);
            }
        });
        this.setDatabase();
        if (list != null && list.size() > 0){
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 批量获取多个数据，数据库为默认配置的db
     * keys集合中的Redis key需在同一个database下面
     *
     * @param keys Redis key 集合
     * @return
     */
    public List<Object> getValue(List<String> keys){
        this.setDatabase();
        return this.get(keys);
    }

    /**
     * 指定Database，批量获取多个数据
     * keys集合中的Redis key需在同一个database下面
     *
     * @param keys Redis key 集合
     * @param database 数据库Index
     * @return
     */
    public List<Object> getValue(List<String> keys, int database){
        this.setDatabase(database);
        List<Object> list = this.get(keys);
        this.setDatabase();
        return list;
    }

    /**
     * 指定Database，批量获取多个数据
     * keys集合中的Redis key需在同一个database下面,返回结果为key-value
     * @param keys
     * @param database
     * @return
     */
    public Map<String,Object> getValues(List<String> keys,int database){
        this.setDatabase(database);
        Map<String, Object> stringObjectMap = batchQueryByKeys(keys);
        this.setDatabase();
        return stringObjectMap;
    }

    /**
     * 指定database，批量获取多个数据
     * @param keys Redis key
     * @return List<Object>
     */
    private List<Object> get(List<String> keys){
        return redisTemplate.executePipelined(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                for (String key : keys) {
                    redisConnection.get(redisTemplate.getStringSerializer().serialize(key));
                }
                return null;
            }
        });
    }

    /**
     * 指定database，批量获取多个数据,转为对应的key-value
     * @param keys
     * @return
     */
    private Map<String,Object> batchQueryByKeys(List<String> keys){

        if(null == keys || keys.size() == 0 ){
            return null;
        }
        //批量获取
        List<Object> results = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                for (String key : keys) {
                    redisConnection.get(redisTemplate.getStringSerializer().serialize(key));
                }
                return null;
            }
        });
        if(null == results || results.size() == 0 ){return null;}
        //组装对应的key-value
        Map<String,Object> resultMap  = Collections.synchronizedMap(new HashMap<String,Object>());
        keys.parallelStream().forEach(t -> {
            resultMap.put(t,results.get(keys.indexOf(t)));
        });
        return resultMap;
    }
    /**
     * 批量保存Map对象
     * val: [{"deviceId":"31011811001320021008","deviceName":"广场北口1"}]
     * key: deviceId
     * 存储到redis的结构
     * 31011811001320021008 : {"deviceId":"31011811001320021008","deviceName":"广场北口1"}
     *
     * @param key 对象中的key
     * @param val 对象集合
     */
    private void set(String key, List<Map> val){
        redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                for (Map map : val) {
                    redisConnection.set(redisTemplate.getStringSerializer().serialize(String.valueOf(map.get(key))), JSON.toJSONBytes(map));
                }
                return null;
            }
        });
    }

    /**
     * 批量保存String对象
     * val: {"Redis Key值":"Redis Value值",...}
     *
     * @param val Map key:value存储集合
     */
    public void set(Map<String, String> val){
        redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                val.forEach((k, v) -> {
                    redisConnection.set(redisTemplate.getStringSerializer().serialize(k), v.getBytes());
                });
                return null;
            }
        });
    }


    /**
     * 设置指定数据库
     * @param database 数据库Index
     */
    private void setDatabase(int database){
        LettuceConnectionFactory lettuceConnectionFactory = (LettuceConnectionFactory) redisTemplate.getConnectionFactory();
        if (database != lettuceConnectionFactory.getDatabase()){
            lettuceConnectionFactory.setDatabase(database);
            lettuceConnectionFactory.afterPropertiesSet();
        }
    }

    // =============================common============================

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                this.setDatabase();
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        this.setDatabase();
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            this.setDatabase();
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            this.setDatabase();
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    // ============================String=============================

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        this.setDatabase();
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            this.setDatabase();
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value,Integer database) {
        try {
            this.setDatabase(database);
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                this.setDatabase();
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     * @param key 键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        this.setDatabase();
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @param key 键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        this.setDatabase();
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    // ================================Map=================================

    void setDatabase(){
        System.out.println("不建议频繁切库，会导致redis缓存性能问题.");
    }

    /**
     * HashGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        this.setDatabase();
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        this.setDatabase();
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            this.setDatabase();
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map,int db) {
        try {
            this.setDatabase(db);
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * HashSet 并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            this.setDatabase();
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            this.setDatabase();
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            this.setDatabase();
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     * @param key 键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        this.setDatabase();
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        this.setDatabase();
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key 键
     * @param item 项
     * @param by 要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        this.setDatabase();
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     * @param key 键
     * @param item 项
     * @param by 要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        this.setDatabase();
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            this.setDatabase();
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            this.setDatabase();
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            this.setDatabase();
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     * @param key 键
     * @param time 时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            this.setDatabase();
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            this.setDatabase();
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            this.setDatabase();
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ===============================list=================================

    /**
     * 获取list缓存的内容
     * @param key 键
     * @param start 开始
     * @param end 结束 0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            this.setDatabase();
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            this.setDatabase();
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            this.setDatabase();
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            this.setDatabase();
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            this.setDatabase();
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            this.setDatabase();
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            this.setDatabase();
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            this.setDatabase();
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            this.setDatabase();
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


}

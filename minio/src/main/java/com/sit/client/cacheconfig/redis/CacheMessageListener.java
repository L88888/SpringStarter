//package com.tgl.example.cacheconfig.redis;
//
//import com.alibaba.fastjson.JSONObject;
//import DeviceEntity;
//import DeviceInfoCacheManager;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.cache.RedisCacheWriter;
//import org.springframework.data.redis.connection.Message;
//import org.springframework.data.redis.connection.MessageListener;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.listener.PatternTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
//import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//
//import java.models.Objects;
//
///**
// * @program: spring-starter
// * @description: 实时订阅并监听redis中的消息频道
// * 1、目前只监听设备这个频道
// * 2、设备频道编号cacheDeviceInfo
// * 3、消费到设备信息后直接将该信息刷新至spring cache中
// *  {key:设备编号, value:{DeviceEntity对象}}
// * @author: LIULEI-TGL
// * @create: 2021-09-23 17:29:
// **/
//@Configuration
//@Slf4j
//public class CacheMessageListener {
//
//    @Autowired
//    private DeviceInfoCacheManager deviceInfoCache;
//
//    @Value("${springext.cache.redis.topic:cacheDeviceInfo}")
//    String topicName;
//
//    /**
//     * 定义缓存容器
//     * @param connectionFactory
//     * @param listenerAdapter
//     * @return
//     */
//    @Bean
//    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
//                                            MessageListenerAdapter listenerAdapter) {
//        log.info("自定义缓存的主题名称为:>{}", topicName);
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.addMessageListener(listenerAdapter, new PatternTopic(topicName));
//        return container;
//    }
//
//    /**
//     * 定义消息监听适配器
//     * @param cacheManager
//     * @return
//     */
//    @Bean
//    MessageListenerAdapter listenerAdapter(final TowLevelCacheManager cacheManager) {
//        log.info("listener 实时监听消息对象.");
//        return new MessageListenerAdapter(new MessageListener() {
//            public void onMessage(Message message, byte[] pattern) {
//                log.info("listener 实时消息:::>>>{}", new String(pattern));
//                // Sub 一个消息，通知缓存管理器
//                cacheManager.receiver(message.getBody());
//            }
//        });
//    }
//
//    /**
//     * 自动注入TowLevelCacheManager对象
//     * 1、初始化redistemplate引用
//     * 2、实例化TowlevelCacheManager对象
//     * 3、加入Spring托管
//     * @param redisTemplate redis模板对象
//     * @return
//     */
//    @Bean
//    public TowLevelCacheManager cacheManager(RedisTemplate redisTemplate) {
//        RedisCacheWriter writer = RedisCacheWriter.lockingRedisCacheWriter(redisTemplate.getConnectionFactory());
//        RedisSerializationContext.SerializationPair pair = RedisSerializationContext.SerializationPair
//                .fromSerializer(new JdkSerializationRedisSerializer(this.getClass().getClassLoader()));
//        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
//        return new TowLevelCacheManager(redisTemplate, writer, config);
//    }
//
//    /**
//     * 本地缓存管理
//     * 1、发布/订阅消息数据
//     * 2、自定义封装任何redis远程操作
//     */
//    class TowLevelCacheManager extends RedisCacheManager {
//        RedisTemplate redisTemplate;
//
//        public TowLevelCacheManager(RedisTemplate redisTemplate, RedisCacheWriter cacheWriter,
//                                    RedisCacheConfiguration defaultCacheConfiguration) {
//            super(cacheWriter, defaultCacheConfiguration);
//            this.redisTemplate = redisTemplate;
//        }
//
//        // 使用RedisAndLocalCache代替Spring Boot自带的RedisCache
////        @Override
////        protected Cache decorateCache(Cache cache) {
////            return new RedisAndLocalCache(this, (RedisCache) cache);
////        }
//
//        /**
//         * 发布消息(publish)给定义的主题topicName
//         * @param messageData
//         */
//        public void publishMessage(String messageData) {
//            this.redisTemplate.convertAndSend(topicName, messageData);
//        }
//
//        /**
//         * 接收一个消息给springCache中更新设备信息
//         * @param messageData
//         */
//        public void receiver(byte[] messageData) {
//            // 解析设备缓存对象DeviceEntity
//            log.info("receiver订阅的消息对象为:>{}", new String(messageData));
//            DeviceEntity deviceEntity = JSONObject.parseObject(messageData, DeviceEntity.class);
//            log.info("订阅的消息对象为:>{}", deviceEntity);
//            if (!Objects.isNull(deviceEntity)){
//                // 先删除缓存中的设备数据,在更新缓存设备对象
//                deviceInfoCache.delDeviceInfo(deviceEntity.getDeviceId());
//                deviceInfoCache.putDeviceInfo(deviceEntity.getDeviceId(), deviceEntity);
//            }
//        }
//    }
//}

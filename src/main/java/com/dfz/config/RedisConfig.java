package com.dfz.config;

import java.lang.reflect.Method;
import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 自定义Redis配置类
 * 
 * @author	Felix Du
 * @date	2021-06-11
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
	
	/**
	 * 重写生成key的方法：类名+方法名+参数名.这样在某些需要缓存的类的方法上就不用写key了.
	 */
	@Bean
	@Override
	public KeyGenerator keyGenerator() {
		System.out.println("重写生成key的方法：类名+方法名+参数名......");
		
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(".");
                sb.append(method.getName());
                for (Object obj : params) {
                	sb.append(".");
                    sb.append(obj.toString());
                }
                //类名.方法名.参数名
                return sb.toString();
            }
		};
	}
	
	
	/**
	 * 自定义缓存管理器
	 */
	@Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		System.out.println("缓存管理器......");
		
		RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        //Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        
        //配置序列化（解决乱码的问题）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                //缓存有效期. 7天缓存过期
                //.entryTtl(Duration.ofDays(7))
        		//.entryTtl(Duration.ofSeconds(60))
        		.entryTtl(Duration.ofHours(1))
                //使用StringRedisSerializer来序列化和反序列化redis的key值
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                //使用GenericJackson2JsonRedisSerializer来序列化和反序列化redis的value值
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(genericJackson2JsonRedisSerializer))
                //禁用空值
                .disableCachingNullValues();

        RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
        
        return cacheManager;
        
    }
	
	
	/**
	 * 自定义 RedisTemplate
	 * 
	 * public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory connectionFactory) {}
	 */
	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		System.out.println("自定义 RedisTemplate序列化......");
		
		RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
		template.setConnectionFactory(redisConnectionFactory);
		
		RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
		//Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
		
		//默认序列化 
		template.setDefaultSerializer(genericJackson2JsonRedisSerializer);
		
		template.setKeySerializer(stringRedisSerializer);
		template.setValueSerializer(genericJackson2JsonRedisSerializer);
		//Hash key序列化 
		template.setHashKeySerializer(stringRedisSerializer);
		//Hash value序列化
		template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
		
		template.setEnableTransactionSupport(true);
		
		template.afterPropertiesSet(); 
		
		return template;
	}
}

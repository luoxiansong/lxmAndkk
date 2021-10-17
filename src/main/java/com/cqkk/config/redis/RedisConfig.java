package com.cqkk.config.redis;

import com.cqkk.config.redis.apply.apply3.CatListener;
import com.cqkk.config.redis.apply.apply3.FishListener;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @program: lxmAndkk
 * @description: redis配置类
 * @author: luo kk
 * @create: 2021-06-29 7:20
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.connect-timeout}")
    private int timeout;

    @Value("${spring.redis.database}")
    private int database;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private int maxWait;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    /**
     * 自定义序列化方式
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 初始化string的序列化方式
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        redisTemplate.setValueSerializer(stringRedisSerializer);
        // hash的value序列化方式采用jackson
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    //springboot2.x 集成redis时，配置连接信息和构造方法发生了改变。
    //2.X版本可以使用RedisStandaloneConfiguration、RedisSentinelConfiguration、RedisClusterConfiguration三种方式配置连接信息。
    //这里我们以RedisStandaloneConfiguration为例，修改之前的配置。
   /* @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setPassword(password);
        return new JedisConnectionFactory(configuration);
    }*/

    //同样的RedisCacheManager的配置方式也不能再以RedisTemplate为参数初始化
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        return RedisCacheManager.create(factory);
    }

    //以上配置使用的是直接连接redis的方式，即每次连接都创建新的连接。当并发量剧增时，这会带来性能上开销，
    // 同时由于没有对连接数进行限制，则可能使服务器崩溃导致无法响应。
    //所以我们一般都会建立连接池，事先初始化一组连接，供需要redis连接的线程取用。

    //JedisPool的配置参数大部分是由JedisPoolConfig的对应项来赋值的。
    //maxActive：控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted。
    //maxIdle：控制一个pool最多有多少个状态为idle(空闲)的jedis实例；
    //whenExhaustedAction：表示当pool中的jedis实例都被allocated完时，pool要采取的操作；默认有三种。
    //WHEN_EXHAUSTED_FAIL --> 表示无jedis实例时，直接抛出NoSuchElementException；
    //WHEN_EXHAUSTED_BLOCK --> 则表示阻塞住，或者达到maxWait时抛出JedisConnectionException；
    //WHEN_EXHAUSTED_GROW --> 则表示新建一个jedis实例，也就说设置的maxActive无用；
    //maxWait：表示当borrow一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
    //testOnBorrow：在borrow一个jedis实例时，是否提前进行alidate操作；如果为true，则得到的jedis实例均是可用的；
    //testOnReturn：在return给pool时，是否提前进行validate操作；
    //testWhileIdle：如果为true，表示有一个idle object evitor线程对idle object进行扫描，如果validate失败，此object会被从pool中drop掉；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
    //timeBetweenEvictionRunsMillis：表示idle object evitor两次扫描之间要sleep的毫秒数；
    //numTestsPerEvictionRun：表示idle object evitor每次扫描的最多的对象数；
    //minEvictableIdleTimeMillis：表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
    //softMinEvictableIdleTimeMillis：在minEvictableIdleTimeMillis基础上，加入了至少minIdle个对象已经在pool里面了。如果为-1，evicted不会根据idle time驱逐任何对象。如果minEvictableIdleTimeMillis>0，则此项设置无意义，且只有在timeBetweenEvictionRunsMillis大于0时才有意义；
    //lifo：borrowObject返回对象时，是采用DEFAULT_LIFO（last in first out，即类似cache的最频繁使用队列），如果为False，则表示FIFO队列；
    @Bean
    public RedisConnectionFactory redisConnectionFactory1() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setHostName(host);

        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfigurationBuilder = JedisClientConfiguration.builder();
        JedisClientConfiguration jedisClientConfiguration = jedisClientConfigurationBuilder.usePooling().poolConfig(jedisPoolConfig).build();
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }


    //=========================================redisApplyThree======================================================
    //消息监听适配器，绑定消息处理器
    @Bean(value = "catAdapter")
    public MessageListenerAdapter catAdapter() {
        return new MessageListenerAdapter(new CatListener());
    }

    @Bean(value = "fishAdapter")
    public MessageListenerAdapter fishAdapter() {
        return new MessageListenerAdapter(new FishListener());
    }

    //redis消息监听器容器
    //可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
    //通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory redisConnectionFactory,
                                                   @Qualifier("fishAdapter") MessageListenerAdapter catAdapter,
                                                   @Qualifier("fishAdapter") MessageListenerAdapter fishAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(catAdapter, new PatternTopic("cat"));
        container.addMessageListener(fishAdapter, new PatternTopic("fish"));
        return container;
    }
}

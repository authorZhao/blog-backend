package com.git.service;

import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author authorZhao
 * @since 2025-04-17
 */
public class MarkdownTestSample {


    @Test
    public void test() throws IOException {
        MutableDataSet options = new MutableDataSet();
        // 启用常用扩展
        options.set(Parser.EXTENSIONS, List.of(
                TablesExtension.create(),
                TaskListExtension.create(),
                StrikethroughExtension.create()
        ));
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        var md = FileUtils.readFileToString(new File("C:\\Users\\authorZhao\\Desktop\\工作文档\\worknote\\md\\grpc调研.md"), StandardCharsets.UTF_8);
        // You can re-use parser and renderer instances
        Node document = parser.parse(md);
        String html = renderer.render(document);  // <h1>标题</h1>

        System.out.println(html);
    }




    public static final String REDIS_USE = """
            
            redis常用用法分析
            
            
            
            ## 1常用命令：
            
            https://www.runoob.com/redis/keys-scan.html
            
            
            
            
            
            ## 2.脚本用法
            
            ```
            redis 127.0.0.1:6379> EVAL script numkeys key [key ...] arg [arg ...]
            ```
            
            ```lua
            EVAL "local unread = redis.call('GET',KEYS[1]) if(not unread) then return 0 end local desc = tonumber(unread)-tonumber(ARGV[1]) if(desc<0) then redis.call('SET',KEYS[1],0) return tonumber(unread) end redis.call('SET',KEYS[1],desc) return tonumber(ARGV[1])" 1 aa 4
            ```
            
            
            
            java 代码 \s
            
            ```xml
            <!-- redis -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-data-redis</artifactId>
            </dependency>
            ```
            
            spring-dara-redis使用
            
            配置
            
            ```java
            @Bean
                public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
                    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
                    redisTemplate.setConnectionFactory(redisConnectionFactory);
                    redisTemplate.setKeySerializer(RedisSerializer.string());
                    redisTemplate.setValueSerializer(RedisSerializer.string());
                    redisTemplate.setHashKeySerializer(RedisSerializer.string());
                    redisTemplate.setHashValueSerializer(RedisSerializer.string());
                    return redisTemplate;
                }
            ```
            
            ### redisTemplate使用
            
            ```java
            /**
            *  注意点
            *  1.返回结果类型
            *  2.key和参数的类型
            *  3.序列化的配置
            *  4.这里的脚本是字符串
            */
            DefaultRedisScript<String> redisScript = new DefaultRedisScript<>(script,String.class);
                    Object obj = redisTemplate.execute(redisScript, RedisSerializer.string(), RedisSerializer.string(), keys,params);
            ```
            
            redis-lua简单分析
            
            [lua语法](https://www.runoob.com/lua/lua-miscellaneous-operator.html)
            
            [参考文章](https://www.cnblogs.com/yaopengfei/p/13941841.html)
            
            redis的lua脚本可以使用lua的基本语法和redis、cjson等
            
            ```lua
            function exist(KEYS)
                local table = {}
                for i = 1,#(KEYS) do
                    local singleValue = redis.call('EXISTS' ,KEYS[i])
                    table[KEYS[i]] = singleValue>0
                end
                return cjson.encode(table) -- 这里返回java代码用字符串接收
            end
            
            -- 注意参数的取值，和参数的类型，这里java代码可用Long接收
            function DESC_STRING_KEY(KEYS,ARGV)
                local unread = redis.call('GET',KEYS[1])
                redis.log(redis.LOG_DEBUG,ARGV[1])
                if(not unread) then
                    return 0
                end
                local desc = tonumber(unread)-tonumber(ARGV[1])
                if(desc<0) then
                    redis.call('SET',KEYS[1],0)
                    return tonumber(unread)
                end
                redis.call('SET',KEYS[1],desc)
                return tonumber(ARGV[1])
            end
            
            ```
            
            
            
            ```java
            1.java用法，keys是一个list，param没有不传
            DefaultRedisScript<String> redisScript = new DefaultRedisScript<>(RedisScript.GET_ALL_KEYS,String.class);
                    // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
                    Object execute1 = redisTemplate.execute(redisScript,RedisSerializer.string(), RedisSerializer.string(), Lists.newArrayList("a","b","c") );
                    System.out.println(execute1);
            
            2.
                @Test
                public void test2() {
                    DefaultRedisScript<Long> script = new DefaultRedisScript(RedisScript.DESC_STRING_KEY,Long.class);
            
                    // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
                    Object execute1 = redisTemplate.execute(script,RedisSerializer.string(), RedisSerializer.string(), Lists.newArrayList("aa") ,"4");
                    System.out.println(execute1);
            
                }
            ```
            
            
            
            ## 3.RedisTemplate使用scan
            
            语法：SCAN cursor [MATCH pattern] [COUNT count]
            
            java代码使用
            
            ```java
            /**
            * 注意点在于cursor一定要保证关闭
            */
            public List<String> assembleScanKeys(String pattern, Long limit) {
                    HashSet<String> set = new HashSet<>();
                    Cursor<String> cursor = scan(redisTemplate, pattern, limit);
                    while (cursor.hasNext()) {
                        set.add(cursor.next());
                    }
                    try {
                        cursor.close();
                    } catch (Exception e) {
                        log.error("关闭 redis connection 失败");
                    }
                    return set.stream().map(String::valueOf).collect(toList());
                }
                /**
                 * 自定义 redis scan 操作
                 */
                private Cursor<String> scan(RedisTemplate redisTemplate, String pattern, Long limit) {
                    ScanOptions options = ScanOptions.scanOptions().match(pattern).count(limit).build();
                    RedisSerializer<String> redisSerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
                    return (Cursor) redisTemplate.executeWithStickyConnection(redisConnection -> new ConvertingCursor<>(redisConnection.scan(options), redisSerializer::deserialize));
                }
            ```
            
            
            
            ## 4.redis的订阅通知使用
            
            增加配置
            
            ```java
             @Bean
                public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
                    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
                    container.setConnectionFactory(connectionFactory);
                    return container;
                }
            ```
            
            ```java
            @Service
            @Slf4j
            public class KeyExpiredListener extends KeyExpirationEventMessageListener {
            
                @Autowired
                private RedisTemplate redisTemplate;
                @Resource(name = "ThreadPoolConfig.NewMsgExecutorProperties")
                public ThreadPoolTaskExecutor newMsgThreadPoolTaskExecutor;
                @Autowired
                private RedisMessageListenerContainer listenerContainer;
            
                public KeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
                    super(listenerContainer);
                }
            
                @Override
                protected void doRegister(RedisMessageListenerContainer listenerContainer) {
                        super.doRegister(listenerContainer);
            
                }
            
            
                @Override
                public void onMessage(Message message, byte[] pattern) {
                    String key = message.toString();
                    //不需要消费，直接过滤
                    if(!key.startsWith(TimelineRedisKey.EXPIRE_NEW_MSG)){
                        return;
                    }
                    log.info("redis 过期开始key={}",key);
                }
            
            }
            ```
            
            注意点：
            
            1.所有的服务都会受到key过期的时间通知，会造成重复消费
            
            2.key过期并不总是那么及时 [参考](https://www.cnblogs.com/pinxiong/p/13288087.html) rediskey过期策略和内存淘汰机制
            
            
            
            ## 5.作为分布式锁
            
            ```java
            //SETNX KEY_NAME VALUE
            Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(TimelineRedisKey.LOCK, "1", 15, TimeUnit.MINUTES);
            
            //2.leftPop
            redisTemplate.opsForList().leftPushAll("2021-07-23",list);
            redisTemplate.opsForObject o = redisTemplate.opsForList().leftPop("2021-07-23");List().leftPushAll("2021-07-23",list);
            
            ```
            
            ## 6.管道
            
            ```java
            public Map<String,Object> batchQueryByKeys(List<String> keys,Boolean useParallel){
                    if(null == keys || keys.size() == 0 ){
                        return null;
                    }
                    if(null == useParallel){
                        useParallel = true;
                    }
                    List<Object> results = redisTemplate.executePipelined(
                            new RedisCallback<Object>() {
                                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                                    RedisConnection stringRedisConn = connection;
                                    for(String key:keys) {
                                        stringRedisConn.get(key.getBytes());
                                    }
                                    return null;
                                }
                            });
                    if(null == results || results.size() == 0 ){return null;}
            
                    Map<String,Object> resultMap  =  null;
                    if(useParallel){
                        Map<String,Object> resultMapOne  = Collections.synchronizedMap(new HashMap<String,Object>());
                        keys.parallelStream().forEach(t -> {
                            resultMapOne.put(t,results.get(keys.indexOf(t)));
                        });
                        resultMap = resultMapOne;
                    }else{
                        Map<String,Object> resultMapTwo  = new HashMap<>();
                        for(String t:keys){
                            resultMapTwo.put(t,results.get(keys.indexOf(t)));
                        }
                        resultMap = resultMapTwo;
                    }
                    return  resultMap;
                }
            ```
            
            ## 7.限流
            
            
            
            ## 8.布隆过滤器
            
            
            9.参数
            最近使用Redis缓存行情数据，发现程序运行一段时间后，出现subscribe线程不再能够接收到订阅的行情数据，发现是由Redis的输出缓冲机制导致的。
            
            Redis为了解决输出缓冲区消息大量堆积的隐患，设置了一些保护机制，主要采用两种限制措施：
            
            大小限制，当某一客户端缓冲区超过设定值后直接关闭连接；
            持续性限制，当某一客户端缓冲区持续一段时间占用过大空间时关闭连接。
            通过CONFIG GET *查看，可以找到客户端输出缓冲区的默认配置：
            
            167) "client-output-buffer-limit"
            168) "normal 0 0 0 slave 268435456 67108864 60 pubsub 33554432 8388608 60" \s
            1
            2
            对于普通客户端来说，限制为0，也就是不限制。因为普通客户端通常采用阻塞式的消息应答模式，何谓阻塞式呢？如：发送请求，等待返回，再发送请求，再等待返回。这种模式下，通常不会导致Redis服务器输出缓冲区的堆积膨胀；
            对于Pub/Sub客户端（也就是发布/订阅模式），大小限制是8M，当输出缓冲区超过8M时，会关闭连接。持续性限制是，当客户端缓冲区大小持续60秒超过2M，则关闭客户端连接；
            对于slave客户端来说，大小限制是256M，持续性限制是当客户端缓冲区大小持续60秒超过64M，则关闭客户端连接。
            上述三种规则都是可以修改的。可以通过CONFIG SET 命令设置或者直接修改redis.conf
            
            config set client-output-buffer-limit pubsub 0 0 0 #将hard limit和soft limit同时置0，关闭该限制。
            
            
            
            修改 notify-keyspace-events Ex\s
            ```
            # K    键空间通知，以__keyspace@<db>__为前缀
            # E    键事件通知，以__keysevent@<db>__为前缀
            # g    del , expipre , rename 等类型无关的通用命令的通知, ...
            # $    String命令
            # l    List命令
            # s    Set命令
            # h    Hash命令
            # z    有序集合命令
            # x    过期事件（每次key过期时生成）
            # e    驱逐事件（当key在内存满了被清除时生成）
            # A    g$lshzxe的别名，因此”AKE”意味着所有的事件
            
            ```
            
            ```
            #是否作为守护进程运行 yes 或者 no
            daemonize yes
            
            #监听IP,redis一般监听127.0.0.1 网段访问，集群模式需要指定IP地址。
            bind 192.168.1.115
            
            # 当 Redis 以守护进程的方式运行的时候，Redis 默认会把 pid 文件放在/var/run/redis.pid
            # 可配置到其他地址，当运行多个 redis 服务时，需要指定不同的 pid 文件和端口
            # 指定存储Redis进程号的文件路径
            pidfile /var/run/redis-6379.pid
            
            #端口
            port 6379
            
            # TCP 监听的最大容纳数量
            # 此参数确定了TCP连接中已完成队列(完成三次握手之后)的长度，
            # 当系统并发量大并且客户端速度缓慢的时候，你需要把这个值调高以避免客户端连接缓慢的问题。
            # Linux 内核会一声不响的把这个值缩小成 /proc/sys/net/core/somaxconn 对应的值，默认是511，而Linux的默认参数值是128。
            # 所以可以将这二个参数一起参考设定，你以便达到你的预期。
            tcp-backlog 511
            
            # 客户端和Redis服务端的连接超时时间，默认是0，表示永不超时。
            timeout 0
            
            # 推荐一个合理的值就是60秒
            tcp-keepalive 0
            
            # 日志记录等级，4个可选值debug,verbose,notice,warning
            # 可以是下面的这些值：
            # debug (适用于开发或测试阶段)
            # verbose (many rarely useful info, but not a mess like the debug level)
            # notice (适用于生产环境)
            # warning (仅仅一些重要的消息被记录)
            loglevel notice
            
            #配置 log 文件地址,默认打印在命令行终端的窗口上，也可设为/dev/null屏蔽日志、
            logfile "/opt/redis/logs/redis-6379.log"
            
            # 可用的数据库数，默认值为16，默认数据库为0，数据库范围在0-（database-1）之间
            databases 16
            
            # 在 900 秒内最少有 1 个 key 被改动，或者 300 秒内最少有 10 个 key 被改动，又或者 60 秒内最少有 1000 个 key 被改动，以上三个条件随便满足一个，就触发一次保存操作。
            #    if(在60秒之内有10000个keys发生变化时){
            #      进行镜像备份
            #    }else if(在300秒之内有10个keys发生了变化){
            #      进行镜像备份
            #    }else if(在900秒之内有1个keys发生了变化){
            #      进行镜像备份
            #    }
            save 900 1
            save 300 10
            save 60 10000000
            
            # 默认情况下，如果 redis 最后一次的后台保存失败，redis 将停止接受写操作，这样以一种强硬的方式让用户知道数据不能正确的持久化到磁盘，
            # 否则就会没人注意到灾难的发生。如果后台保存进程重新启动工作了，redis 也将自动的允许写操作。
            # 然而你要是安装了靠谱的监控，你可能不希望 redis 这样做，那你就改成 no 好
            stop-writes-on-bgsave-error no
            
            # 在进行备份时,是否进行压缩 是否在 dump .rdb 数据库的时候使用 LZF 压缩字符串默认都设为 yes 如果你希望保存子进程节省点 cpu ，你就设置它为 no ，
            # 不过这个数据集可能就会比较大
            rdbcompression yes
            
            # 读取和写入的时候是否支持CRC64校验，默认是开启的
            rdbchecksum yes
            
            # 备份文件的文件名
            dbfilename dump.rdb
            
            
            # 数据库备份的文件放置的路径 路径跟文件名分开配置是因为 Redis 备份时，先会将当前数据库的状态写入到一个临时文件
            # 等备份完成时，再把该临时文件替换为上面所指定的文件而临时文件和上面所配置的备份文件都会放在这个指定的路径当中
            # 默认值为 ./
            dir /opt/redis/data
            
            # 当slave服务器和master服务器失去连接后，或者当数据正在复制传输的时候，如果此参数值设置“yes”，slave服务器可以继续接受客户端的请求，
            #否则，会返回给请求的客户端如下信息“SYNC with master in progress”,除了INFO，SLAVEOF这两个命令
            slave-serve-stale-data yes
            
            # 是否允许slave服务器节点只提供读服务
            slave-read-only yes
            
            #全量同步时,是否把快照先存于磁盘,如果存于磁盘,会有10性能,但是可以复用RDB文件
            #如果不存于磁盘,就会直接把RDB文件流发送给从节点,复用性降低,在从节点比较少的时候,建议开启
            repl-diskless-sync no
            
            #与上一个参数相关,当上一个设置为yes ,采用不存储磁盘时, master等待一定时间,等待更多的从节点要求增量复制,然后进行并行复制
            repl-diskless-sync-delay 5
            
            # 指定向slave同步数据时，是否禁用socket的NO_DELAY选 项。若配置为“yes”，则禁用NO_DELAY，则TCP协议栈会合并小包统一发送，这样可以减少主从节点间的包数量并节省带宽，
            #但会增加数据同步到 slave的时间。若配置为“no”，表明启用NO_DELAY，则TCP协议栈不会延迟小包的发送时机，这样数据同步的延时会减少，但需要更大的带宽。
            #通常情况下，应该配置为no以降低同步延时，但在主从节点间网络负载已经很高的情况下，可以配置为yes。
            repl-disable-tcp-nodelay no
            
            # 指定slave的优先级。在不只1个slave存在的部署环境下，当master宕机时，Redis
            # Sentinel会将priority值最小的slave提升为master。
            # 这个值越小，就越会被优先选中，需要注意的是，
            # 若该配置项为0，则对应的slave永远不会自动提升为master。
            slave-priority 100
            
            #内存满了后的淘汰策略,默认为noeviction ,即没有淘汰策略,直接返回错误给客户端
            #volatile-lru :利用lru算法淘汰设置过过期时间的key
            #allkeys-lru :利用lru算法淘汰所有的key
            #olatile-random :随机的淘汰设置过过期时间的key
            #allkeys-random :随机的淘汰所有的keyvolatile-ttl :淘汰即将到达过期时间的key
            #noeviction :没有淘汰算法
            maxmemory-policy allkeys-lru
            
            # redis 默认每次更新操作后会在后台异步的把数据库镜像备份到磁盘，但该备份非常耗时，且备份不宜太频繁
            # redis 同步数据文件是按上面save条件来同步的
            # 如果发生诸如拉闸限电、拔插头等状况,那么将造成比较大范围的数据丢失
            # 所以redis提供了另外一种更加高效的数据库备份及灾难恢复方式
            # 开启append only 模式后,redis 将每一次写操作请求都追加到appendonly.aof 文件中
            # redis重新启动时,会从该文件恢复出之前的状态。
            # 但可能会造成 appendonly.aof 文件过大，所以redis支持BGREWRITEAOF 指令，对appendonly.aof重新整理,默认是不开启的。
            appendonly yes
            
            # 默认为appendonly.aof
            appendfilename "appendonly.aof"
            
            # 设置对 appendonly.aof 文件进行同步的频率,有三种选择always、everysec、no，默认是everysec表示每秒同步一次。
            # always 表示每次有写操作都进行同步,everysec 表示对写操作进行累积,每秒同步一次。
            # no表示等操作系统进行数据缓存同步到磁盘，都进行同步,everysec 表示对写操作进行累积,每秒同步一次
            # appendfsync always
            # appendfsync everysec
            # appendfsync no
            appendfsync everysec
            
            # 指定是否在后台aof文件rewrite期间调用fsync，默认为no，表示要调用fsync（无论后台是否有子进程在刷盘）。
            #Redis在后台写RDB文件或重写afo文件期间会存在大量磁盘IO，此时，在某些linux系统中，调用fsync可能会阻塞。
            no-appendfsync-on-rewrite yes
            
            # 指定Redis重写aof文件的条件，默认为100，表示与上次rewrite的aof文件大小相比，当前aof文件增长量超过上次afo文件大小的100%时，
            #就会触发background rewrite。若配置为0，则会禁用自动rewrite
            auto-aof-rewrite-percentage 100
            
            # 指定触发rewrite的aof文件大小。若aof文件小于该值，即使当前文件的增量比例达到auto-aof-rewrite-percentage的配置值，
            #也不会触发自动rewrite。即这两个配置项同时满足时，才会触发rewrite。
            auto-aof-rewrite-min-size 64mb
            
            #指redis在恢复时，会忽略最后一条可能存在问题的指令。默认值yes。即在aof写入时，
            #可能存在指令写错的问题(突然断电，写了一半)，这种情况下，yes会log并继续，而no会直接恢复失败.
            aof-load-truncated yes
            
            # 一个Lua脚本最长的执行时间，单位为毫秒，如果为0或负数表示无限执行时间，默认为5000
            lua-time-limit 5000
            
            #开启集群
            cluster-enabled yes
            
            #集群配置文件
            cluster-config-file /opt/redis/conf/nodes-6379.conf
            
            #集群节点超时时间
            cluster-node-timeout 5000
            
            #redis slow log是一个系统到日志的查询，它超过了指定的执行时间。执行时间不包括I/O操作比如和客户交谈，发送回复等等，
            #但实际执行命令所需的时间（这是执行命令的阶段，其中线程被阻塞，无法提供服务同时提出其他要求）。
            #您可以使用两个参数配置慢日志：一个参数告诉redis执行时间是多少，以微秒为单位，以便获取日志的命令，另一个参数是
            #慢日志。当记录新命令时，最旧的命令将从记录的命令队列。以下时间以微秒表示，因此1000000相当于到一秒钟。注意，负数将禁用慢日志，而值为零会强制记录每个命令。
            slowlog-log-slower-than 10000
            
            #这个长度没有限制。只是要注意它会消耗内存。
            #您可以使用slow log reset回收慢日志使用的内存。
            slowlog-max-len 128
            
            #redis延迟监控子系统对不同的操作进行采样在运行时，以便收集与redis实例的延迟。通过延迟命令，用户可以使用这些信息打印图表并获取报告。系统只记录在时间等于或大于通过
            #延迟监视器阈值配置指令。当它的值被设置时将延迟监视器关闭为零。默认情况下，延迟监视被禁用，因为它基本上不需要 如果没有延迟问题，并且收集数据有性能冲击虽然很小，
            #但可以在大载荷下测量。延迟使用命令可以在运行时轻松启用监视
            #“config set latency monitor threshold<millishes>”如果需要。
            latency-monitor-threshold 0
            
            
            #事件通知 #零个或多个字符。空字符串表示通知已禁用。
            notify-keyspace-events ""
            
            # 当hash中包含超过指定元素个数并且最大的元素没有超过临界时，
            # hash将以一种特殊的编码方式（大大减少内存使用）来存储，这里可以设置这两个临界值
            hash-max-ziplist-entries 512
            hash-max-ziplist-value 64
            
            # list数据类型多少节点以下会采用去指针的紧凑存储格式。
            # list数据类型节点值大小小于多少字节会采用紧凑存储格式。
            list-max-ziplist-entries 512
            list-max-ziplist-value 64
            
            # set数据类型内部数据如果全部是数值型，且包含多少节点以下会采用紧凑格式存储。
            set-max-intset-entries 512
            
            # zsort数据类型多少节点以下会采用去指针的紧凑存储格式。
            # zsort数据类型节点值大小小于多少字节会采用紧凑存储格式。
            zset-max-ziplist-entries 128
            zset-max-ziplist-value 64
            
            #建议值为3000
            hll-sparse-max-bytes 3000
            
            
            # Redis将在每100毫秒时使用1毫秒的CPU时间来对redis的hash表进行重新hash，可以降低内存的使用
            # 当你的使用场景中，有非常严格的实时性需要，不能够接受Redis时不时的对请求有2毫秒的延迟的话，把这项配置为no。
            # 如果没有这么严格的实时性要求，可以设置为yes，以便能够尽可能快的释放内存
            activerehashing yes
            
            #当某些客户端由于处理速度不够,服务器端会缓冲一部分数据,当超过缓冲硬缓冲的限制,则会把客户端的连接关闭
            #当超过软限制( soft limit) ,并且达到一定的时间soft seconds ,则也会关闭连接
            #client-output-buffer-limit normal 0 0 0 //normal代表常规客户端,包括监控类型的客户端
            #client-output-buffer-limit slave 256mb 64mb 60 //slave代表主从结构的客户端
            #client-output-buffer-limit pubsub 32mb 8mb 60 //订阅类型的客户端
            client-output-buffer-limit normal 0 0 0
            client-output-buffer-limit slave 256mb 64mb 60
            client-output-buffer-limit pubsub 32mb 8mb 60
            
            #hz默认设为10，提高它的值将会占用更多的cpu，当然相应的redis将会更快的处理同时到期的许多key，以及更精确的去处理超时。
            #hz的取值范围是1~500，通常不建议超过100，只有在请求延时非常低的情况下可以将值提升到100。
            hz 10
            
            # aof rewrite过程中,是否采取增量文件同步策略,默认为“yes”。 rewrite过程中,每32M数据进行一次文件同步,这样可以减少aof大文件写入对磁盘的操作次数
            aof-rewrite-incremental-fsync yes
            
            ```
            
            """;
}

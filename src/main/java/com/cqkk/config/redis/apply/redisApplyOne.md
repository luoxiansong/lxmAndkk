应用场景 · 缓存系统：用于缓解数据库的高并发压力

· 计数器：使用Redis原子操作，用于社交网络的转发数，评论数，粉丝数，关注数等

· 排行榜：使用zset数据结构，进行排行榜计算

· 实时系统：使用Redis位图的功能实现布隆过滤器，进而实现垃圾邮件处理系统

· 消息队列：使用list数据结构，消息发布者push数据，多个消息订阅者通过阻塞线程pop数据，以此提供简单的消息队列能力

利用Redis实现高并发计数器 业务需求中经常有需要用到计数器的场景：譬如一个手机号一天限制发送5条短信、一个接口一分钟限制多少请求、一个接口一天限制调用多少次等等。使用Redis的Incr自增命令可以轻松实现以上需求。
代码见redisApplyOne.java
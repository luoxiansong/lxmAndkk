https://blog.csdn.net/lingbo229/article/details/80761778?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522162726289516780366586962%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fnavwordall.%2522%257D&request_id=162726289516780366586962&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~navwordall~first_rank_v2~hot_rank-1-80761778.first_rank_v2_pc_rank_v29&utm_term=kafka&spm=1018.2226.3001.4187

[kakfa]
Kafka是最初由Linkedin公司开发，是一个分布式、支持分区的（partition）、多副本的（replica），基于zookeeper协调的分布式消息系统，
它的最大的特性就是可以实时的处理大量数据以满足各种需求场景：比如基于hadoop的批处理系统、低延迟的实时系统、storm/Spark流式处理引擎，
web/nginx日志、访问日志，消息服务等等，用scala语言编写，Linkedin于2010年贡献给了Apache基金会并成为顶级开源 项目。

[kafka特性]
- 高吞吐量、低延迟：kafka每秒可以处理几十万条消息，它的延迟最低只有几毫秒，每个topic可以分多个partition, 
  consumer group 对partition进行consume操作。
- 可扩展性：kafka集群支持热扩展
- 持久性、可靠性：消息被持久化到本地磁盘，并且支持数据备份防止数据丢失
- 容错性：允许集群中节点失败（若副本数量为n,则允许n-1个节点失败）
- 高并发：支持数千个客户端同时读写

[kafka使用场景]
- 日志收集：一个公司可以用Kafka可以收集各种服务的log，通过kafka以统一接口服务的方式开放给各种consumer，例如hadoop、Hbase、Solr等。
- 消息系统：解耦和生产者和消费者、缓存消息等。
- 用户活动跟踪：Kafka经常被用来记录web用户或者app用户的各种活动，如浏览网页、搜索、点击等活动，这些活动信息被各个服务器发布到kafka的topic中，
  然后订阅者通过订阅这些topic来做实时的监控分析，或者装载到hadoop、数据仓库中做离线分析和挖掘。
- 运营指标：Kafka也经常用来记录运营监控数据。包括收集各种分布式应用的数据，生产各种操作的集中反馈，比如报警和报告。
- 流式处理：比如spark streaming和storm
- 事件源

[kafka的设计思想]

[Kafka部分名词解释]
Kafka中发布订阅的对象是topic。我们可以为每类数据创建一个topic，把向topic发布消息的客户端称作producer，从topic订阅消息的客户端称作consumer。
Producers和consumers可以同时从多个topic读写数据。一个kafka集群由一个或多个broker服务器组成，它负责持久化和备份具体的kafka消息。
[Broker]：Kafka节点，一个Kafka节点就是一个broker，多个broker可以组成一个Kafka集群。
[Topic]：一类消息，消息存放的目录即主题，例如page view日志、click日志等都可以以topic的形式存在，Kafka集群能够同时负责多个topic的分发。
[Partition]：topic物理上的分组，一个topic可以分为多个partition，每个partition是一个有序的队列
[Segment]：partition物理上由多个segment组成，每个Segment存着message信息
[Producer] : 生产message发送到topic
[Consumer]: 订阅topic消费message, consumer作为一个线程来消费
[Consumer Group]：一个Consumer Group包含多个consumer, 这个是预先在配置文件中配置好的。
各个consumer（consumer 线程）可以组成一个组（Consumer group ），partition中的每个message只能被组（Consumer group ） 
中的一个consumer（consumer 线程 ）消费，如果一个message可以被多个consumer（consumer 线程 ） 消费的话，
那么这些consumer必须在不同的组。Kafka不支持一个partition中的message由两个或两个以上的consumer thread来处理，
即便是来自不同的consumer group的也不行。它不能像AMQ那样可以多个BET作为consumer去处理message，
这是因为多个BET去消费一个Queue中的数据的时候，由于要保证不能多个线程拿同一条message，所以就需要行级别悲观所（for update）,
这就导致了consume的性能下降，吞吐量不够。而kafka为了保证吞吐量，只允许一个consumer线程去访问一个partition。如果觉得效率不高的时候，
可以加partition的数量来横向扩展，那么再加新的consumer thread去消费。这样没有锁竞争，充分发挥了横向的扩展性，吞吐量极高。这也就形成了分布式消费的概念。


[kafka一些原理概念]/
[持久化]
kafka使用文件存储消息(append only log), 这就直接决定kafka在性能上严重依赖文件系统的本身特性。且无论任何OS下,对文件系统本身的优化是非常艰难的。
文件缓存/直接内存映射等是常用的手段。因为kafka是对日志文件进行append操作, 因此磁盘检索的开支是较小的;同时为了减少磁盘写入的次数, broker会将消息暂时
buffer起来, 当消息的个数(或尺寸)达到一定阀值时, 再flush到磁盘,这样减少了磁盘IO调用的次数.对于kafka而言,较高性能的磁盘, 将会带来更加直接的性能提升。
[性能]
除磁盘IO之外,我们还需要考虑网络IO,这直接关系到kafka的吞吐量问题。kafka并没有提供太多高超的技巧;对于producer端,可以将消息buffer起来,
当消息的条数达到一定阀值时,批量发送给broker;对于consumer端也是一样,批量fetch多条消息.不过消息量的大小可以通过配置文件来指定.对于kafka broker端,
似乎有个sendfile系统调用可以潜在的提升网络IO的性能:将文件的数据映射到系统内存中,socket直接读取相应的内存区域即可,而无需进程再次copy和交换
(这里涉及到"磁盘IO数据"/"内核内存"/"进程内存"/"网络缓冲区",多者之间的数据copy)。 其实对于producer/consumer/broker三者而言,CPU的开支应该都不大,
因此启用消息压缩机制是一个良好的策略;压缩需要消耗少量的CPU资源,不过对于kafka而言,网络IO更应该需要考虑。可以将任何在网络上传输的消息都经过压缩。
kafka支持gzip/snappy等多种压缩方式。
[负载均衡]
kafka集群中的任何一个broker,都可以向producer提供metadata信息,这些metadata中包含"集群中存活的servers列表"/"partitions leader列表"等信息
(请参看zookeeper中的节点信息). 当producer获取到metadata信息之后, producer将会和Topic下所有partition leader保持socket连接;
消息由producer直接通过socket发送到broker,中间不会经过任何"路由层"。
异步发送，将多条消息暂且在客户端buffer起来,并将他们批量发送到broker;小数据IO太多,会拖慢整体的网络延迟,批量延迟发送事实上提升了网络效率;
不过这也有一定的隐患,比如当producer失效时,那些尚未发送的消息将会丢失。
[Topic模型]
其他JMS实现,消息消费的位置是有prodiver保留,以便避免重复发送消息或者将没有消费成功的消息重发等,同时还要控制消息的状态.
这就要求JMS broker需要太多额外的工作.在kafka中,partition中的消息只有一个consumer在消费,且不存在消息状态的控制,也没有复杂的消息确认机制,
可见kafka broker端是相当轻量级的.当消息被consumer接收之后,consumer可以在本地保存最后消息的offset,并间歇性的向zookeeper注册offset.
由此可见,consumer客户端也很轻量级。
kafka中consumer负责维护消息的消费记录,而broker则不关心这些,这种设计不仅提高了consumer端的灵活性,也适度的减轻了broker端设计的复杂度;
这是和众多JMS prodiver的区别.此外,kafka中消息ACK的设计也和JMS有很大不同,kafka中的消息是批量(通常以消息的条数或者chunk的尺寸为单位)发送给consumer,
当消息消费成功后,向zookeeper提交消息的offset,而不会向broker交付ACK.或许你已经意识到,这种"宽松"的设计,将会有"丢失"消息/"消息重发"的危险.
[消息传输一致]
Kafka提供3种消息传输一致性语义：最多1次，最少1次，恰好1次。
最少1次：可能会重传数据，有可能出现数据被重复处理的情况;
最多1次：可能会出现数据丢失情况;
恰好1次：并不是指真正只传输1次，只不过有一个机制。确保不会出现“数据被重复处理”和“数据丢失”的情况。

at most once: 消费者fetch消息,然后保存offset,然后处理消息;当client保存offset之后,但是在消息处理过程中consumer进程失效(crash),
导致部分消息未能继续处理.那么此后可能其他consumer会接管,但是因为offset已经提前保存,那么新的consumer将不能fetch到offset之前的消息
(尽管它们尚没有被处理),这就是"at most once".
at least once: 消费者fetch消息,然后处理消息,然后保存offset.如果消息处理成功之后,但是在保存offset阶段zookeeper异常或者consumer失效,
导致保存offset操作未能执行成功,这就导致接下来再次fetch时可能获得上次已经处理过的消息,这就是"at least once".
"Kafka Cluster"到消费者的场景中可以采取以下方案来得到“恰好1次”的一致性语义：
最少1次＋消费者的输出中额外增加已处理消息最大编号：由于已处理消息最大编号的存在，不会出现重复处理消息的情况。
[副本]
kafka中,replication策略是基于partition,而不是topic;kafka将每个partition数据复制到多个server上,
任何一个partition有一个leader和多个follower(可以没有);备份的个数可以通过broker配置文件来设定。leader处理所有的read-write请求,
follower需要和leader保持同步.Follower就像一个"consumer",消费消息并保存在本地日志中;leader负责跟踪所有的follower状态,
如果follower"落后"太多或者失效,leader将会把它从replicas同步列表中删除.当所有的follower都将一条消息保存成功,此消息才被认为是"committed",
那么此时consumer才能消费它,这种同步策略,就要求follower和leader之间必须具有良好的网络环境.即使只有一个replicas实例存活,
仍然可以保证消息的正常发送和接收,只要zookeeper集群存活即可.

选择follower时需要兼顾一个问题,就是新leader server上所已经承载的partition leader的个数,如果一个server上有过多的partition leader,
意味着此server将承受着更多的IO压力.在选举新leader,需要考虑到"负载均衡",partition leader较少的broker将会更有可能成为新的leader.
[分布式]
kafka使用zookeeper来存储一些meta信息,并使用了zookeeper watch机制来发现meta信息的变更并作出相应的动作(比如consumer失效,触发负载均衡等)
Broker node registry: 当一个kafka broker启动后,首先会向zookeeper注册自己的节点信息(临时znode),同时当broker和zookeeper断开连接时,此znode也会被删除.
Broker Topic Registry: 当一个broker启动时,会向zookeeper注册自己持有的topic和partitions信息,仍然是一个临时znode.
Consumer and Consumer group: 每个consumer客户端被创建时,会向zookeeper注册自己的信息;此作用主要是为了"负载均衡".一个group中的多个consumer可以交错的消费一个topic的所有partitions;简而言之,保证此topic的所有partitions都能被此group所消费,且消费时为了性能考虑,让partition相对均衡的分散到每个consumer上.
Consumer id Registry: 每个consumer都有一个唯一的ID(host:uuid,可以通过配置文件指定,也可以由系统生成),此id用来标记消费者信息.
Consumer offset Tracking: 用来跟踪每个consumer目前所消费的partition中最大的offset.此znode为持久节点,可以看出offset跟group_id有关,以表明当group中一个消费者失效,其他consumer可以继续消费.
Partition Owner registry: 用来标记partition正在被哪个consumer消费.临时znode。此节点表达了"一个partition"只能被group下一个consumer消费,同时当group下某个consumer失效,那么将会触发负载均衡(即:让partitions在多个consumer间均衡消费,接管那些"游离"的partitions)
当consumer启动时,所触发的操作:
A) 首先进行"Consumer id Registry";
B) 然后在"Consumer id Registry"节点下注册一个watch用来监听当前group中其他consumer的"leave"和"join";只要此znode path下节点列表变更,都会触发此group下consumer的负载均衡.(比如一个consumer失效,那么其他consumer接管partitions).
C) 在"Broker id registry"节点下,注册一个watch用来监听broker的存活情况;如果broker列表变更,将会触发所有的groups下的consumer重新balance.

总结:
1) Producer端使用zookeeper用来"发现"broker列表,以及和Topic下每个partition leader建立socket连接并发送消息.
2) Broker端使用zookeeper用来注册broker信息,已经监测partition leader存活性.
3) Consumer端使用zookeeper用来注册consumer信息,其中包括consumer消费的partition列表等,同时也用来发现broker列表,并和partition leader建立socket连接,并获取消息。
[Leader的选择]
   Kafka的核心是日志文件，日志文件在集群中的同步是分布式数据系统最基础的要素。
   如果leaders永远不会down的话我们就不需要followers了！一旦leader down掉了，需要在followers中选择一个新的leader.但是followers本身有可能延时太久或者crash，所以必须选择高质量的follower作为leader.必须保证，一旦一个消息被提交了，但是leader down掉了，新选出的leader必须可以提供这条消息。大部分的分布式系统采用了多数投票法则选择新的leader,对于多数投票法则，就是根据所有副本节点的状况动态的选择最适合的作为leader.Kafka并不是使用这种方法。
   Kafka动态维护了一个同步状态的副本的集合（a set of in-sync replicas），简称ISR，在这个集合中的节点都是和leader保持高度一致的，任何一条消息必须被这个集合中的每个节点读取并追加到日志中了，才回通知外部这个消息已经被提交了。因此这个集合中的任何一个节点随时都可以被选为leader.ISR在ZooKeeper中维护。ISR中有f+1个节点，就可以允许在f个节点down掉的情况下不会丢失消息并正常提供服。ISR的成员是动态的，如果一个节点被淘汰了，当它重新达到“同步中”的状态时，他可以重新加入ISR.这种leader的选择方式是非常快速的，适合kafka的应用场景。
   一个邪恶的想法：如果所有节点都down掉了怎么办？Kafka对于数据不会丢失的保证，是基于至少一个节点是存活的，一旦所有节点都down了，这个就不能保证了。
   实际应用中，当所有的副本都down掉时，必须及时作出反应。可以有以下两种选择:
1. 等待ISR中的任何一个节点恢复并担任leader。
2. 选择所有节点中（不只是ISR）第一个恢复的节点作为leader.
   这是一个在可用性和连续性之间的权衡。如果等待ISR中的节点恢复，一旦ISR中的节点起不起来或者数据都是了，那集群就永远恢复不了了。如果等待ISR意外的节点恢复，这个节点的数据就会被作为线上数据，有可能和真实的数据有所出入，因为有些数据它可能还没同步到。Kafka目前选择了第二种策略，在未来的版本中将使这个策略的选择可配置，可以根据场景灵活的选择。
   这种窘境不只Kafka会遇到，几乎所有的分布式数据系统都会遇到。
[副本管理]
   以上仅仅以一个topic一个分区为例子进行了讨论，但实际上一个Kafka将会管理成千上万的topic分区.Kafka尽量的使所有分区均匀的分布到集群所有的节点上而不是集中在某些节点上，另外主从关系也尽量均衡这样每个几点都会担任一定比例的分区的leader.
   优化leader的选择过程也是很重要的，它决定了系统发生故障时的空窗期有多久。Kafka选择一个节点作为“controller”,当发现有节点down掉的时候它负责在游泳分区的所有节点中选择新的leader,这使得Kafka可以批量的高效的管理所有分区节点的主从关系。如果controller down掉了，活着的节点中的一个会备切换为新的controller.
[Leader与副本同步]
   对于某个分区来说，保存正分区的"broker"为该分区的"leader"，保存备份分区的"broker"为该分区的"follower"。备份分区会完全复制正分区的消息，包括消息的编号等附加属性值。为了保持正分区和备份分区的内容一致，Kafka采取的方案是在保存备份分区的"broker"上开启一个消费者进程进行消费，从而使得正分区的内容与备份分区的内容保持一致。一般情况下，一个分区有一个“正分区”和零到多个“备份分区”。可以配置“正分区+备份分区”的总数量，关于这个配置，不同主题可以有不同的配置值。注意，生产者，消费者只与保存正分区的"leader"进行通信。

Kafka允许topic的分区拥有若干副本，这个数量是可以配置的，你可以为每个topic配置副本的数量。Kafka会自动在每个副本上备份数据，所以当一个节点down掉时数据依然是可用的。
Kafka的副本功能不是必须的，你可以配置只有一个副本，这样其实就相当于只有一份数据。
创建副本的单位是topic的分区，每个分区都有一个leader和零或多个followers.所有的读写操作都由leader处理，一般分区的数量都比broker的数量多的多，各分区的leader均匀的分布在brokers中。所有的followers都复制leader的日志，日志中的消息和顺序都和leader中的一致。followers向普通的consumer那样从leader那里拉取消息并保存在自己的日志文件中。
许多分布式的消息系统自动的处理失败的请求，它们对一个节点是否着（alive）”有着清晰的定义。Kafka判断一个节点是否活着有两个条件：
1. 节点必须可以维护和ZooKeeper的连接，Zookeeper通过心跳机制检查每个节点的连接。
2. 如果节点是个follower,他必须能及时的同步leader的写操作，延时不能太久。
   符合以上条件的节点准确的说应该是“同步中的（in sync）”，而不是模糊的说是“活着的”或是“失败的”。Leader会追踪所有“同步中”的节点，一旦一个down掉了，或是卡住了，或是延时太久，leader就会把它移除。至于延时多久算是“太久”，是由参数replica.lag.max.messages决定的，怎样算是卡住了，怎是由参数replica.lag.time.max.ms决定的。
   只有当消息被所有的副本加入到日志中时，才算是“committed”，只有committed的消息才会发送给consumer，这样就不用担心一旦leader down掉了消息会丢失。Producer也可以选择是否等待消息被提交的通知，这个是由参数acks决定的。
   Kafka保证只要有一个“同步中”的节点，“committed”的消息就不会丢失。
JDK1.8 新特性

jdk1.8新特性知识点：   
Lambda表达式  
函数式接口  
*方法引用和构造器调用  
Stream API
接口中的默认方法和静态方法  
新时间日期API  

在jdk1.8中对hashMap等map集合的数据结构优化。hashMap数据结构的优化
原来的hashMap采用的数据结构是哈希表（数组+链表），hashMap默认大小是16，一个0-15索引的数组，如何往里面存储元素，首先调用元素的hashcode  
方法，计算出哈希码值，经过哈希算法算成数组的索引值，如果对应的索引处没有元素，直接存放，如果有对象在，那么比较它们的equals方法比较内容  
如果内容一样，后一个value会将前一个value的值覆盖，如果不一样，在1.7的时候，后加的放在前面，形成一个链表，形成了碰撞，在某些情况下如果链表  
无限下去，那么效率极低，碰撞是避免不了的  
加载因子：0.75，数组扩容，达到总容量的75%，就进行扩容，但是无法避免碰撞的情况发生  
在1.8之后，在数组+链表+红黑树来实现hashmap，当碰撞的元素个数大于8时 & 总容量大于64，会有红黑树的引入  
除了添加之后，效率都比链表高，1.8之后链表新进元素加到末尾  
ConcurrentHashMap (锁分段机制)，分為16个桶,concurrentLevel,jdk1.8采用CAS算法(无锁算法，不再使用锁分段)，数组+链表中也引入了红黑树的使用  

众所周知，在 Java 中，HashMap 是非线程安全的，如果想在多线程下安全的操作 map，主要有以下解决方法：  
第一种方法，使用Hashtable线程安全类；Hashtable 是一个线程安全的类，Hashtable 几乎所有的添加、删除、查询方法都加了synchronized同步锁！  
相当于给整个哈希表加了一把大锁，多线程访问时候，只要有一个线程访问或操作该对象，那其他线程只能阻塞等待需要的锁被释放，在竞争激烈的多线程场景中性能就会非常差，所以 Hashtable 不推荐使用！
第二种方法，使用Collections.synchronizedMap方法，对方法进行加同步锁；     
如果传入的是 HashMap 对象，其实也是对 HashMap 做的方法做了一层包装，里面使用对象锁来保证多线程场景下，操作安全，本质也是对 HashMap 进行全表锁！
使用Collections.synchronizedMap方法，在竞争激烈的多线程环境下性能依然也非常差，所以不推荐使用！  
第三种方法，使用并发包中的ConcurrentHashMap类；      
ConcurrentHashMap 类所采用的正是分段锁的思想，将 HashMap 进行切割，把 HashMap 中的哈希数组切分成小数组，  
每个小数组有 n 个 HashEntry 组成，其中小数组继承自ReentrantLock（可重入锁），这个小数组名叫Segment  
JDK1.8 中 ConcurrentHashMap 类取消了 Segment 分段锁，采用 CAS + synchronized 来保证并发安全，  
数据结构跟 jdk1.8 中 HashMap 结构类似，都是数组 + 链表（当链表长度大于8时，链表结构转为红黑二叉树）结构。  
ConcurrentHashMap 中 synchronized 只锁定当前链表或红黑二叉树的首节点，只要节点 hash 不冲突，就不会产生并发，  
相比 JDK1.7 的 ConcurrentHashMap 效率又提升了 N 倍！  

Lambda表达式     
Lmabda表达式的语法总结： () -> ();  
前置	语法  
无参数无返回值	() -> System.out.println(“Hello WOrld”)  
有一个参数无返回值	(x) -> System.out.println(x)  
有且只有一个参数无返回值	x -> System.out.println(x)  
有多个参数，有返回值，有多条lambda体语句	(x，y) -> {System.out.println(“xxx”);return xxxx;}；  
有多个参数，有返回值，只有一条lambda体语句	(x，y) -> xxxx  

函数式接口  
简单来说就是只定义了一个抽象方法的接口（Object类的public方法除外），就是函数式接口，并且还提供了注解：@FunctionalInterface  
Consumer 《T》：消费型接口，有参无返回值  
Supplier 《T》：供给型接口，无参有返回值  
Function 《T,R》：:函数式接口，有参有返回值  
Predicate《T》： 断言型接口，有参有返回值，返回值是boolean类型    
在四大核心函数式接口基础上，还提供了诸如BiFunction、BinaryOperation、toIntFunction等扩展的函数式接口，都是在这四种函数式接口上扩展而来的。  


(a)方法引用  
若lambda体中的内容有方法已经实现了，那么可以使用“方法引用”  
也可以理解为方法引用是lambda表达式的另外一种表现形式并且其语法比lambda表达式更加简单  
(a) 方法引用  
三种表现形式：  
1. 对象::实例方法名  
2. 类::静态方法名  
3. 类::实例方法名 （lambda参数列表中第一个参数是实例方法的调用 者，第二个参数是实例方法的参数时可用）    

(b)构造器引用    
格式：ClassName::new    

Stream API  
Stream操作的三个步骤:  
创建stream   
中间操作（过滤、map）  
终止操作  

并行流和串行流  
在jdk1.8新的stream包中针对集合的操作也提供了并行操作流和串行操作流。并行流就是把内容切割成多个数据块，  
并且使用多个线程分别处理每个数据块的内容。Stream api中声明可以通过parallel()与sequential()方法在并行流和串行流之间进行切换。  
jdk1.8并行流使用的是fork/join框架进行并行操作  

ForkJoin框架  
Fork/Join 框架：就是在必要的情况下，将一个大任务，进行拆分(fork)成若干个小任务（拆到不可再拆时），再将一个个的小任务运算的结果进行 join 汇总。  
关键字：递归分合、分而治之。  
采用 “工作窃取”模式（work-stealing）：  
当执行新的任务时它可以将其拆分分成更小的任务执行，并将小任务加到线程队列中，然后再从一个随机线程的队列中偷一个并把它放在自己的队列中    
相对于一般的线程池实现,fork/join框架的优势体现在对其中包含的任务的处理方式上.在一般的线程池中,如果一个线程正在执行的任务由于某些原因    
无法继续运行,那么该线程会处于等待状态.而在fork/join框架实现中,如果某个子问题由于等待另外一个子问题的完成而无法继续运行.那么处理该子    
问题的线程会主动寻找其他尚未运行的子问题来执行.这种方式减少了线程的等待时间,提高了性能。     


Optional容器  
Optional的构造函数  
Optional 的三种构造方式：Optional.of(obj)， Optional.ofNullable(obj) 和明确的 Optional.empty()  

Optional.of(obj)：它要求传入的 obj 不能是 null 值的, 否则直接报NullPointerException 异常。    
Optional.ofNullable(obj)：它以一种智能的，宽容的方式来构造一个 Optional 实例。来者不拒，传 null 进到就得到 Optional.empty()，  
非 null 就调用 Optional.of(obj).    
Optional.empty()：返回一个空的 Optional 对象    

Optional的常用函数  
of：为非null的值创建一个Optional。of方法通过工厂方法创建Optional类。需要注意的是，创建对象时传入的参数不能为null。如果传入参数为null，  
则抛出NullPointerException。因此不经常用。  
ofNullable：为指定的值创建一个Optional，如果指定的值为null，则返回一个空的Optional。  
isPresent：如果值存在返回true，否则返回false。  
ifPresent：如果Optional实例有值则为其调用consumer，否则不做处理  
get：如果Optional有值则将其返回，否则抛出NoSuchElementException。因此也不经常用。  
orElse：如果有值则将其返回，否则返回指定的其它值。  
orElseGet：orElseGet与orElse方法类似，区别在于得到的默认值。orElse方法将传入的字符串作为默认值，orElseGet方法可以接受Supplier接口的实现用来生成默认值  
orElseThrow：如果有值则将其返回，否则抛出supplier接口创建的异常。  
filter：如果有值并且满足断言条件返回包含该值的Optional，否则返回空Optional。  
map：如果有值，则对其执行调用mapping函数得到返回值。如果返回值不为null，则创建包含mapping返回值的Optional作为map方法返回值，否则返回空Optional。  
flatMap：如果有值，为其执行mapping函数返回Optional类型返回值，否则返回空Optional。  



新的日期API LocalDate | LocalTime | LocalDateTime  

新的日期API都是不可变的，更使用于多线程的使用环境中  
* 之前使用的java.util.Date月份从0开始，我们一般会+1使用，很不方便，java.time.LocalDate月份和星期都改成了enum
* java.util.Date和SimpleDateFormat都不是线程安全的，而LocalDate和LocalTime和最基本的String一样，是不变类型，不但线程安全，而且不能修改。
* java.util.Date是一个“万能接口”，它包含日期、时间，还有毫秒数，更加明确需求取舍
* 新接口更好用的原因是考虑到了日期时间的操作，经常发生往前推或往后推几天的情况。用java.util.Date配合Calendar要写好多代码，而且一般的开发人员还不一定能写对。


#Async
引言： 在Java应用中，绝大多数情况下都是通过同步的方式来实现交互处理的；但是在处理与第三方系统交互的时候，容易造成响应迟缓的情况，
之前大部分都是使用多线程来完成此类任务，其实，在spring 3.x之后，就已经内置了@Async来完美解决这个问题。
对于异步方法调用，从Spring3开始提供了@Async注解，该注解可以被标注在方法上，以便异步地调用该方法。
调用者将在调用时立即返回，方法的实际执行将提交给Spring TaskExecutor的任务中，由指定的线程池中的线程执行。
在项目应用中，@Async调用线程池，推荐使用自定义线程池的模式。自定义线程池常用方案：重新实现接口AsyncConfigurer。

#应用场景
同步：同步就是整个处理过程顺序执行，当各个过程都执行完毕，并返回结果。
异步：异步调用则是只是发送了调用的指令，调用者无需等待被调用的方法完全执行完毕；而是继续执行下面的流程。
例如， 在某个调用中，需要顺序调用 A, B, C三个过程方法；如他们都是同步调用，则需要将他们都顺序执行完毕之后，
方算作过程执行完毕；如B为一个异步的调用方法，则在执行完A之后，调用B，并不等待B完成，而是执行开始调用C，待C执行完毕之后，
就意味着这个过程执行完毕了。在Java中，一般在处理类似的场景之时，都是基于创建独立的线程去完成相应的异步调用逻辑，
通过主线程和不同的业务子线程之间的执行流程，从而在启动独立的线程之后，主线程继续执行而不会产生停滞等待的情况。

#Spring 已经实现的线程池
SimpleAsyncTaskExecutor：不是真的线程池，这个类不重用线程，默认每次调用都会创建一个新的线程。
SyncTaskExecutor：这个类没有实现异步调用，只是一个同步操作。只适用于不需要多线程的地方。
ConcurrentTaskExecutor：Executor的适配类，不推荐使用。如果ThreadPoolTaskExecutor不满足要求时，才用考虑使用这个类。
SimpleThreadPoolTaskExecutor：是Quartz的SimpleThreadPool的类。线程池同时被quartz和非quartz使用，才需要使用此类。
ThreadPoolTaskExecutor ：最常使用，推荐。其实质是对java.util.concurrent.ThreadPoolExecutor的包装。

#异步的方法有：
最简单的异步调用，返回值为void
带参数的异步调用，异步方法可以传入参数
存在返回值，常调用返回Future

#@Async应用默认线程池
Spring应用默认的线程池，指在@Async注解在使用时，不指定线程池的名称。查看源码，@Async的默认线程池为SimpleAsyncTaskExecutor。

#有返回值CompletableFuture调用
CompletableFuture 并不使用@Async注解，可达到调用系统线程池处理业务的功能。

JDK5新增了Future接口，用于描述一个异步计算的结果。虽然 Future 以及相关使用方法提供了异步执行任务的能力，
但是对于结果的获取却是很不方便，只能通过阻塞或者轮询的方式得到任务的结果。阻塞的方式显然和我们的异步编程的初衷相违背，
轮询的方式又会耗费无谓的 CPU 资源，而且也不能及时地得到计算结果。

#CompletionStage代表异步计算过程中的某一个阶段，一个阶段完成以后可能会触发另外一个阶段
#一个阶段的计算执行可以是一个Function，Consumer或者Runnable。比如：
stage.thenApply(x -> square(x)).thenAccept(x -> System.out.print(x)).thenRun(() -> System.out.println())

#一个阶段的执行可能是被单个阶段的完成触发，也可能是由多个阶段一起触发
在Java8中，CompletableFuture提供了非常强大的Future的扩展功能，可以帮助我们简化异步编程的复杂性，并且提供了函数式编程的能力，
可以通过回调的方式处理计算结果，也提供了转换和组合 CompletableFuture 的方法。

#它可能代表一个明确完成的Future，也有可能代表一个完成阶段（ CompletionStage ），它支持在计算完成以后触发一些函数或执行某些动作。

#默认线程池的弊端
在线程池应用中，参考阿里巴巴java开发规范：线程池不允许使用Executors去创建，不允许使用系统默认的线程池，
推荐通过ThreadPoolExecutor的方式，这样的处理方式让开发的工程师更加明确线程池的运行规则，规避资源耗尽的风险。
Executors各个方法的弊端：
newFixedThreadPool和newSingleThreadExecutor：主要问题是堆积的请求处理队列可能会耗费非常大的内存，甚至OOM。
newCachedThreadPool和newScheduledThreadPool：要问题是线程数最大数是Integer.MAX_VALUE，可能会创建数量非常多的线程，甚至OOM。

@Async默认异步配置使用的是SimpleAsyncTaskExecutor，该线程池默认来一个任务创建一个线程，若系统中不断的创建线程，
最终会导致系统占用内存过高，引发OutOfMemoryError错误。针对线程创建问题，SimpleAsyncTaskExecutor提供了限流机制，
通过concurrencyLimit属性来控制开关，当concurrencyLimit>=0时开启限流机制，默认关闭限流机制即concurrencyLimit=-1，
当关闭情况下，会不断创建新的线程来处理任务。基于默认配置，SimpleAsyncTaskExecutor并不是严格意义的线程池，达不到线程复用的功能。

#@Async应用自定义线程池
自定义线程池，可对系统中线程池更加细粒度的控制，方便调整线程池大小配置，线程执行异常控制和处理。在设置系统自定义线程池代替默认线程池时，
虽可通过多种模式设置，但替换默认线程池最终产生的线程池有且只能设置一个（不能设置多个类继承AsyncConfigurer）。
自定义线程池有如下模式：
>>重新实现接口AsyncConfigurer
>>继承AsyncConfigurerSupport
>>配置由自定义的TaskExecutor替代内置的任务执行器
通过查看Spring源码关于@Async的默认调用规则，会优先查询源码中实现AsyncConfigurer这个接口的类，
实现这个接口的类为AsyncConfigurerSupport。但默认配置的线程池和异步处理方法均为空，
所以，无论是继承或者重新实现接口，都需指定一个线程池。且重新实现 public Executor getAsyncExecutor()方法。

1.  何为异步调用？
    在解释异步调用之前，我们先来看同步调用的定义；同步就是整个处理过程顺序执行，当各个过程都执行完毕，并返回结果。 
    异步调用则是只是发送了调用的指令，调用者无需等待被调用的方法完全执行完毕；而是继续执行下面的流程。

    例如， 在某个调用中，需要顺序调用 A, B, C三个过程方法；如他们都是同步调用，则需要将他们都顺序执行完毕之后，方算作过程执行完毕； 
    如B为一个异步的调用方法，则在执行完A之后，调用B，并不等待B完成，而是执行开始调用C，待C执行完毕之后，就意味着这个过程执行完毕了。
2.  常规的异步调用处理方式
    在Java中，一般在处理类似的场景之时，都是基于创建独立的线程去完成相应的异步调用逻辑，通过主线程和不同的线程之间的执行流程，
    从而在启动独立的线程之后，主线程继续执行而不会产生停滞等待的情况。

在Spring中，基于@Async标注的方法，称之为异步方法；这些方法将在执行的时候，将会在独立的线程中被执行， 
调用者无需等待它的完成， 即可继续其他的操作。

#如何在Spring中启用@Async
1、基于Java配置的启用方式：
@Configuration  
@EnableAsync  
public class SpringAsyncConfig { ... }  

springboot中的配置是：
@EnableSwagger2
@EnableAsync
@EnableTransactionManagement
public class SettlementApplication {
public static void main(String[] args) {
    SpringApplication.run(SettlementApplication.class, args);
    }
}

2、基于XML配置文件的启用方式，配置如下：
<task:executor id="myexecutor" pool-size="5"  />  
<task:annotation-driven executor="myexecutor"/>  
以上就是两种定义的方式。

#注意事项，以下情况会使@Async失效；
异步方法使用static修饰 ；
异步类没有使用@Component注解（或其他注解）导致spring无法扫描到异步类；
类中需要使用@Autowired或@Resource等注解自动注入，不能自己手动new对象；
如果使用SpringBoot框架必须在启动类中增加@EnableAsync注解 ；
在Async方法上标注@Transactional是没用的。 在Async 方法调用的方法上标注@Transactional 有效；


#CompletableFuture介绍
Future模式的缺点；
-Future虽然可以实现获取异步执行结果的需求，但是它没有提供通知机制，我们无法得知Future什么时候完成；
-Future是Java 5添加的类，用来描述一个异步计算的结果，但是获取一个结果时方法较少,要么通过轮询isDone，确认完成后，调用get()获取值，
要么调用get()设置一个超时时间。但是这个get()方法会阻塞住调用线程，这种阻塞的方式显然和我们的异步编程的初衷相违背。

CompletableFuture介绍；
-CompletableFuture能够将回调放到与任务不同的线程中执行，也能将回调作为继续执行的同步函数，在与任务相同的线程中执行。
它避免了传统回调最大的问题，那就是能够将控制流分离到不同的事件处理器中。
-CompletableFuture弥补了Future模式的缺点。在异步的任务完成后，需要用其结果继续操作时，无需等待。
可以直接通过thenAccept、thenApply、thenCompose等方式将前面异步处理的结果交给另外一个异步事件处理线程来处理。
CompletableFuture的方法介绍；
CompletableFuture的静态工厂方法
![img.png](img.png)
runAsync 和 supplyAsync 方法的区别是runAsync没有返回值；

-进行变换
public <U> CompletableFuture<U> thenApply(Function<? super T,? extends U> fn);
public <U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn);
public <U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn,Executor executor);
首先说明一下已Async结尾的方法都是可以异步执行的，如果指定了线程池，会在指定的线程池中执行，如果没有指定，
默认会在ForkJoinPool.commonPool()中执行，下文中将会有好多类似的，都不详细解释了。
关键的入参只有一个Function，它是函数式接口，所以使用Lambda表示起来会更加优雅。它的入参是上一个阶段计算后的结果，返回值是经过转化后结果。
-进行消耗
public CompletableFuture<Void> thenAccept(Consumer<? super T> action);
public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action);
public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action,Executor executor);
thenAccept是针对结果进行消耗，因为他的入参是Consumer，有入参无返回值。

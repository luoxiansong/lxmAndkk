[jps]
jps是jdk提供的一个查看当前java进程的小工具 是java提供的一个显示当前所有java进程pid的命令，适合在linux/unix平台上简单察看当前java进程的一些简单情况。
很多人都是用过unix系统里的ps命令，这个命令主要是用来显示当前系统的进程情况，有哪些进程以及进程id。 jps 也是一样，它的作用是显示当前系统的java进程情况及进程id。

使用方法： 注：在当前命令行下打jps(jps存放在JAVA_HOME/bin/jps，使用时为了方便需将JAVA_HOME/bin/加入到Path) 。 $> jps 23991 Jps 23651 Resin

-q 只显示pid，不显示class名称,jar文件名和传递给main方法的参数 [jps -q]
-m 输出传递给main方法的参数，在嵌入式jvm上可能是null [jps -m]
-l 输出应用程序main class的完整package名或者应用程序的jar文件完整路径名 [jps -l]
-v 输出传递给JVM的参数 [jps -v]
-V 隐藏输出传递给JVM的参数 [jps -V]

[jstat]
Jstat是JDK自带的一个轻量级小工具。 它位于java的bin目录下，主要利用JVM内建的指令对Java应用程序的资源和性能进行实时的命令行的监控，
包括了对Heap size和垃圾回收状况的监控。可见，Jstat是轻量级的、专门针对JVM的工具，非常适用。
jstat工具特别强大，有众多的可选项，详细查看堆内各个部分的使用量，以及加载类的数量。
使用时，需加上查看进程的进程id，和所选参数。参考格式如下： jstat -options 可以列出当前JVM版本支持的选项，常见的有 :
class (类加载器)
compiler (JIT)
gc (GC堆状态)
gccapacity (各区大小)
gccause (最近一次GC统计和原因)
gcnew (新区统计)
gcnewcapacity (新区大小)
gcold (老区统计)
gcoldcapacity (老区大小)
gcpermcapacity (永久区大小)
gcutil (GC统计汇总)
printcompilation (HotSpot编译统计)

[jstat –class<pid>] : 显示加载class的数量，及所占空间等信息。
显示列名          具体描述
Loaded          装载的类的数量
Bytes           装载类所占用的字节数
Unloaded        卸载类的数量
Bytes           卸载类的字节数
Time            装载和卸载类所花费的时间

[jstat -compiler <pid>]显示VM实时编译的数量等信息。
显示列名          具体描述
Compiled        编译任务执行数量
Failed          编译任务执行失败数量
Invalid         编译任务执行失效数量
Time            编译任务消耗时间
FailedType      最后一个编译失败任务的类型
FailedMethod    最后一个编译失败任务所在的类及方法

[jstat -gc <pid>]可以显示gc的信息，查看gc的次数，及时间。
S0C
年轻代中第一个survivor（幸存区）的容量 (字节)
S1C
年轻代中第二个survivor（幸存区）的容量 (字节)
S0U
年轻代中第一个survivor（幸存区）目前已使用空间 (字节)
S1U
年轻代中第二个survivor（幸存区）目前已使用空间 (字节)
EC
年轻代中Eden（伊甸园）的容量 (字节)
EU
年轻代中Eden（伊甸园）目前已使用空间 (字节)
OC
Old代的容量 (字节)
OU
Old代目前已使用空间 (字节)
PC
Perm(持久代)的容量 (字节)
PU
Perm(持久代)目前已使用空间 (字节)
YGC
从应用程序启动到采样时年轻代中gc次数
YGCT
从应用程序启动到采样时年轻代中gc所用时间(s)
FGC
从应用程序启动到采样时old代(全gc)gc次数
FGCT
从应用程序启动到采样时old代(全gc)gc所用时间(s)
GCT
从应用程序启动到采样时gc用的总时间(s)

[jstat -gccapacity <pid>]可以显示，VM内存中三代（young,old,perm）对象的使用和占用大小
NGCMN
年轻代(young)中初始化(最小)的大小(字节)
NGCMX
年轻代(young)的最大容量 (字节)
NGC
年轻代(young)中当前的容量 (字节)
S0C
年轻代中第一个survivor（幸存区）的容量 (字节)
S1C
年轻代中第二个survivor（幸存区）的容量 (字节)
EC
年轻代中Eden（伊甸园）的容量 (字节)
OGCMN
old代中初始化(最小)的大小 (字节)
OGCMX
old代的最大容量(字节)
OGC
old代当前新生成的容量 (字节)
OC
Old代的容量 (字节)
PGCMN
perm代中初始化(最小)的大小 (字节)
PGCMX
perm代的最大容量 (字节)
PGC
perm代当前新生成的容量 (字节)
PC
Perm(持久代)的容量 (字节)
YGC
从应用程序启动到采样时年轻代中gc次数
FGC
从应用程序启动到采样时old代(全gc)gc次数

[jstat -gcutil <pid>] 统计gc信息
S0
年轻代中第一个survivor（幸存区）已使用的占当前容量百分比
S1
年轻代中第二个survivor（幸存区）已使用的占当前容量百分比
E
年轻代中Eden（伊甸园）已使用的占当前容量百分比
O
old代已使用的占当前容量百分比
P
perm代已使用的占当前容量百分比
YGC
从应用程序启动到采样时年轻代中gc次数
YGCT
从应用程序启动到采样时年轻代中gc所用时间(s)
FGC
从应用程序启动到采样时old代(全gc)gc次数
FGCT
从应用程序启动到采样时old代(全gc)gc所用时间(s)
GCT
从应用程序启动到采样时gc用的总时间(s)

[jstat -gcnew <pid>] 年轻代对象的信息。

[jstat -gcnewcapacity<pid>] 年轻代对象的信息及其占用量。

[jstat -gcold <pid>] old代对象的信息。

[stat -gcoldcapacity <pid>] old代对象的信息及其占用量。

[jstat -gcpermcapacity<pid>] perm对象的信息及其占用量。

[stat -printcompilation <pid>] 当前VM执行的信息。
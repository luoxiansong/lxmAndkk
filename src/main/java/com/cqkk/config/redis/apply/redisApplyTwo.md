Redis实现排行榜功能  

需求 前段时间，做了一个世界杯竞猜积分排行榜。对世界杯64场球赛胜负平进行猜测，猜对+1分，错误+0分，一人一场只能猜一次。 1.展示前一百名列表。 2.展示个人排名(如：张三，您当前的排名106579)。

分析 一开始打算直接使用mysql数据库来做，遇到一个问题，每个人的分数都会变化,如何能够获取到个人的排名呢？数据库可以通过分数进行row_num排序， 但是这个方法需要进行全表扫描，当参与的人数达到10000的时候查询就非常慢了。
redis的排行榜功能就完美锲合了这个需求。来看看我是怎么实现的吧。

redis sorts sets简介 Sorted Sets数据类型就像是set和hash的混合。与sets一样，Sorted Sets是唯一的，不重复的字符串组成。可以说Sorted Sets也是Sets的一种。 Sorted
Sets是通过Skip List(跳跃表)和hash Table(哈希表)的双端口数据结构实现的，因此每次添加元素时，Redis都会执行O(log(N))操作。
所以当我们要求排序的时候，Redis根本不需要做任何工作了，早已经全部排好序了。元素的分数可以随时更新。

ZSetOperations 有序集合，默认按照score升序排列，存储格式K(1)==V(n)，V(1)=S(1)(K=key,V=value,S=score)

1.add(K,V,S)：添加

2.count(K,Smin,Smax)：键为K的集合，Smin<=score<=Smax的元素个数

3.size(K)：键为K的集合元素个数

4.score(K,obj)：键为K的集合，value为obj的元素分数

5.incrementScore(K,V,delta)：元素分数增加，delta是增量

6.intersectAndStore(K,otherK[s],destK)：K集合与otherK[s]集合，共同的交集元素存到destK（复制），返回元素个数

unionAndStore(K,otherK[s],destK)：K集合与otherK[s]集合，共同的并集元素存到destK（复制），返回元素个数

7.range(K,start,end)：键为K的集合，索引start<=index<=end的元素子集，正序

reverseRange(K,start,end)：键为K的集合，索引start<=index<=end的元素子集，倒序

8.rangeByScore(K,Smin,Smax,[offset],[count])：键为K的集合，offerset和count是限制条件，从索引1开始找到count个元素=子集，从子集中找到Smin<=score<
=Smax的元素集合，返回Set<V>，正序

reverseRangeByScore(K,Smin,Smax,[offset],[count])：键为K的集合，offerset和count是限制条件，从索引1开始找到count个元素=子集，从子集中找到Smin<=score<
=Smax的元素集合，返回Set<V>，倒序

9.rangeByScoreWithScores(K,Smin,Smax,[offset],[count])：键为K的集合，offerset和count是限制条件，从索引1开始找到count个元素=子集，从子集中找到Smin<=score<
=Smax的元素集合，返回泛型接口（包括score和value），正序

reverseRangeByScoreWithScores(K,Smin,Smax,[offset],[count])：键为K的集合，offerset和count是限制条件，从索引1开始找到count个元素=子集，从子集中找到Smin<
=score<=Smax的元素集合，返回泛型接口（包括score和value），倒序

10.rangeWithScores(K,start,end)：键为K的集合，索引start<=index<=end的元素子集，返回泛型接口（包括score和value），正序

reverseRangeWithScores(K,start,end)：键为K的集合，索引start<=index<=end的元素子集，返回泛型接口（包括score和value），倒序

11.rank(K,obj)：键为K的集合，value为obj的元素索引，正序

reverseRank(K,obj)：键为K的集合，value为obj的元素索引，倒序

12.remove(K,obj)：删除，键为K的集合，value为obj的元素

13.removeRange(K,start,end)：删除，键为K的集合，索引start<=index<=end的元素子集

14.removeRangeByScore(K,Smin,Smax)：删除，键为K的集合，Smin<=score<=Smax的元素，返回删除个数

新增or更新 有三种方式，一种是单个，一种是批量，对分数使用加法(如果不存在，则从0开始加)。

//单个新增or更新 Boolean add(K key, V value, double score); //批量新增or更新 Long add(K key, Set<TypedTuple<V>> tuples); //使用加法操作分数
Double incrementScore(K key, V value, double delta);

删除 删除提供了三种方式：通过key/values删除，通过排名区间删除，通过分数区间删除。

//通过key/value删除 Long remove(K key, Object... values);

//通过排名区间删除 Long removeRange(K key, long start, long end);

//通过分数区间删除 Long removeRangeByScore(K key, double min, double max);

查 1.列表查询:分为两大类，正序和逆序。以下只列表正序的，逆序的只需在方法前加上reverse即可：

//通过排名区间获取列表值集合 Set<V> range(K key, long start, long end);

//通过排名区间获取列表值和分数集合 Set<TypedTuple<V>> rangeWithScores(K key, long start, long end);

//通过分数区间获取列表值集合 Set<V> rangeByScore(K key, double min, double max);

//通过分数区间获取列表值和分数集合 Set<TypedTuple<V>> rangeByScoreWithScores(K key, double min, double max);

//通过Range对象删选再获取集合排行 Set<V> rangeByLex(K key, Range range);

//通过Range对象删选再获取limit数量的集合排行 Set<V> rangeByLex(K key, Range range, Limit limit);

2.单人查询

可获取单人排行，和通过key/value获取分数。以下只列表正序的，逆序的只需在方法前加上reverse即可：

//获取个人排行 Long rank(K key, Object o);

//获取个人分数 Double score(K key, Object o);

统计 统计分数区间的人数，统计集合基数。

//统计分数区间的人数 Long count(K key, double min, double max);

//统计集合基数 Long zCard(K key);

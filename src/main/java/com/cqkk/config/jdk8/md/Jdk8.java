package com.cqkk.config.jdk8.md;

import com.cqkk.entity.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: lxmAndkk
 * @description: jdk8新特性
 * @author: luo kk
 * @create: 2021-07-13 07:24
 */
@Component
public class Jdk8 {

    private Log log = LogFactory.getLog(Jdk8.class);
    private static final String NAME = "name_1";

    //(a)方法引用
    public void test1() {
        /**
         *注意：
         *   1.lambda体中调用方法的参数列表与返回值类型，要与函数式接口中抽象方法的函数列表和返回值类型保持一致！
         *   2.若lambda参数列表中的第一个参数是实例方法的调用者，而第二个参数是实例方法的参数时，可以使用ClassName::method
         *
         */
        Consumer<Integer> con = (x) -> System.out.println(x);
        con.accept(100);

        // 方法引用-对象::实例方法
        Consumer<Integer> con2 = System.out::println;
        con2.accept(200);

        // 方法引用-类名::静态方法名
        BiFunction<Integer, Integer, Integer> biFun = (x, y) -> Integer.compare(x, y);
        BiFunction<Integer, Integer, Integer> biFun2 = Integer::compare;
        Integer result1 = biFun2.apply(100, 200);

        // 方法引用-类名::实例方法名
        BiFunction<String, String, Boolean> fun1 = (str1, str2) -> str1.equals(str2);
        BiFunction<String, String, Boolean> fun2 = String::equals;
        Boolean result2 = fun2.apply("hello", "word");
        System.out.println(result2);
    }

    //(b)构造器引用
    //格式：ClassName::new
    public void test2() {
        //supplier也是是用来创建对象的，但是不同于传统的创建对象语法：new:
        // 构造方法引用  类名::new
        Supplier<UserInfo> sup = () -> new UserInfo();
        System.out.println((sup.get()));
        //创建Supplier容器，声明为UserInfo类型，此时并不会调用对象的构造方法，即不会创建对象
        Supplier<UserInfo> sup2 = UserInfo::new;
        //调用get()方法，此时会调用对象的构造方法，即获得到真正对象
        //每次get都会调用构造方法，即获取的对象不同
        UserInfo userInfo = sup.get();
        System.out.println((sup2.get()));

        // 构造方法引用 类名::new （带一个参数）
        Function<Integer, UserInfo> function = (x) -> new UserInfo(1);
        Function<Integer, UserInfo> function1 = UserInfo::new;
        UserInfo info = function1.apply(2);
        System.out.println((info.toString()));
    }

    //(c)数组引用
    //格式：Type[]::new
    public void test3() {
        // 数组引用
        Function<Integer, String[]> fun1 = (x) -> new String[x];
        Function<Integer, String[]> fun2 = String[]::new;
        String[] strings = fun2.apply(3);
        Arrays.stream(strings).forEach(System.out::println);
    }


    //创建Stream流
    public void test4() {
        // 1，校验通过Collection 系列集合提供的stream()或者paralleStream()
        ArrayList<String> list = new ArrayList<>();
        Stream<String> stream = list.stream();

        // 2.通过Arrays的静态方法stream()获取数组流
        String[] strings = new String[10];
        Stream<String> stream1 = Arrays.stream(strings);

        // 3.通过Stream类中的静态方法of
        Stream<String> stream2 = Stream.of("aa", "bb", "cc");

        // 4.创建无限流
        // 迭代
        Stream<Integer> stream3 = Stream.iterate(0, (x) -> x + 1);

        //生成
        Stream<Double> stream4 = Stream.generate(Math::random);
    }

    //Stream的中间操作
    public void test5() {
        //筛选 过滤  去重
        ArrayList<UserInfo> userInfos = new ArrayList<>();
        userInfos.add(new UserInfo(1, "2", "3", "4"));
        userInfos.add(new UserInfo(2, "2", "3", "4"));
        userInfos.add(new UserInfo(3, "2", "3", "4"));
        userInfos.add(new UserInfo(4, "2", "3", "4"));
        userInfos.add(new UserInfo(5, "2", "3", "4"));
        userInfos.stream().filter(u -> u.getId() > 2).limit(2).skip(1).forEach(System.out::println);

        //生成新的流 通过map映射
        userInfos.stream().map(UserInfo::getUsername).forEach(System.out::println);

        //自然排序  定制排序
        userInfos.stream().sorted((u1, u2) -> {
            if (u1.getId() > u2.getId()) {
                return u1.getUsername().compareTo(u2.getUsername());
            }
            return u1.getId().compareTo(u2.getId());
        }).forEach(System.out::println);
    }

    //Stream的终止操作
    public void test6() {
        /**
         *      查找和匹配
         *      allMatch-检查是否匹配所有元素
         *      anyMatch-检查是否至少匹配一个元素
         *      noneMatch-检查是否没有匹配所有元素
         *      findFirst-返回第一个元素
         *      findAny-返回当前流中的任意元素
         *      count-返回流中元素的总个数
         *      max-返回流中最大值
         *      min-返回流中最小值
         * */
        ArrayList<UserInfo> userInfos = new ArrayList<>();
        userInfos.add(new UserInfo(1, "2", "3", "4"));
        userInfos.add(new UserInfo(2, "2", "3", "4"));
        userInfos.add(new UserInfo(3, "2", "3", "4"));
        userInfos.add(new UserInfo(4, "2", "3", "4"));
        userInfos.add(new UserInfo(5, "2", "3", "4"));
        //  检查是否匹配元素
        boolean b1 = userInfos.stream()
                .allMatch((e) -> e.getUsername().equals(NAME));
        System.out.println(b1);

        boolean b2 = userInfos.stream()
                .anyMatch((e) -> e.getUsername().equals(NAME));
        System.out.println(b2);

        boolean b3 = userInfos.stream()
                .noneMatch((e) -> e.getUsername().equals(NAME));
        System.out.println(b3);

        Optional<UserInfo> opt = userInfos.stream()
                .findFirst();
        System.out.println(opt.get());

        // 并行流
        Optional<UserInfo> opt2 = userInfos.parallelStream()
                .findAny();
        System.out.println(opt2.get());

        long count = userInfos.stream()
                .count();
        System.out.println(count);

        Optional<UserInfo> max = userInfos.stream()
                .max(Comparator.comparingDouble(UserInfo::getId));
        System.out.println(max.get());

        Optional<UserInfo> min = userInfos.stream()
                .min(Comparator.comparingDouble(UserInfo::getId));
        System.out.println(min.get());

        //还有功能比较强大的两个终止操作 reduce和collect

        //reduce操作： reduce:(T identity,BinaryOperator)/reduce(BinaryOperator)-可以将流中元素反复结合起来，得到一个值
        //reduce ：规约操作
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Integer count2 = list.stream().reduce(0, (x, y) -> x + y);
        System.out.println((count2));

        Optional<Integer> sum = userInfos.stream().map(UserInfo::getId).reduce(Integer::sum);
        System.out.println((sum));

        //collect操作：Collect-将流转换为其他形式，接收一个Collection接口的实现，用于给Stream中元素做汇总的方法
        List<Integer> integers = userInfos.stream().map(UserInfo::getId).collect(Collectors.toList());
        integers.stream().forEach(System.out::println);
    }

    //展示多线程的效果
    public void test7() {
        //并行流 多个线程执行
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        numbers.parallelStream().forEach(System.out::println);

        System.out.println(("======================="));
        //串行流
        numbers.stream().sequential().forEach(System.out::println);
    }


    /**
     * 创建一个Optional实例  Optional.of(T t)
     * 创建一个空的Optional实例  Optional.empty()
     * 若T不为null，创建一个Optional实例，否则创建一个空实例  Optional.ofNullable(T t)
     * 判断是否包含值  isPresent()
     * 如果调用对象包含值，返回该值，否则返回T  orElse(T t)
     * 如果调用对象包含值，返回该值，否则返回s中获取的值   orElseGet(Supplier s)
     * map(Function f): // 如果有值对其处理，并返回处理后的Optional，否则返回Optional.empty();
     * flatMap(Function mapper);// 与map类似。返回值是Optional
     * <p>
     * 总结：Optional.of(null)  会直接报NPE
     */
    public void test8() {
        Optional<UserInfo> op = Optional.of(new UserInfo(1, "2", "3", "5"));
        System.out.println((op.get()));

        // NPE空指针
        Optional<Object> op1 = Optional.of(null);
        System.out.println((op1));

        Optional<Object> op2 = Optional.empty();
        System.out.println((op2));
        // No value present
        System.out.println((op2.get()));

        Optional<UserInfo> op3 = Optional.ofNullable(new UserInfo(2, "2", "3", "4"));
        System.out.println((op3.orElse(new UserInfo())));
        System.out.println((op3.orElse(null)));

        Optional<UserInfo> op4;
        op4 = Optional.empty();
        UserInfo userInfo = op4.orElseGet(UserInfo::new);
        System.out.println((userInfo));

        Optional<UserInfo> op5 = Optional.of(new UserInfo(1, "2", "3", "5"));
        System.out.println((op5.map(UserInfo::getUsername).get()));
    }


    public void test9() {
        // 从默认时区的系统时钟获取当前的日期时间。不用考虑时区差
        LocalDateTime date = LocalDateTime.now();
        //2018-07-15T14:22:39.759
        System.out.println((date));

        System.out.println((date.getYear()));
        System.out.println((date.getMonthValue()));
        System.out.println((date.getDayOfMonth()));
        System.out.println((date.getHour()));
        System.out.println((date.getMinute()));
        System.out.println((date.getSecond()));
        System.out.println((date.getNano()));//它返回的纳秒为0到999、999、999之间。

        // 手动创建一个LocalDateTime实例
        LocalDateTime date2 = LocalDateTime.of(2021, 7, 14, 5, 56, 20, 20);
        System.out.println((date2));
        // 进行加操作，得到新的日期实例
        LocalDateTime date3 = date2.plusDays(2);
        // 进行减操作，得到新的日期实例
        LocalDateTime date4 = date3.minusDays(2);
        System.out.println((date4));
    }

    public void test10() {
        // 时间戳  1970年1月1日00：00：00 到某一个时间点的毫秒值
        // 默认获取UTC时区
        Instant ins = Instant.now();
        System.out.println((ins));

        System.out.println((LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()));
        System.out.println((System.currentTimeMillis()));

        //Instant类的toEpochMilli()方法用于将此瞬间转换为从1970-01-01T00：00：00Z的纪元到long值的毫秒数。此方法返回该long值。
        System.out.println((Instant.now().toEpochMilli()));
        System.out.println((Instant.now().atOffset(ZoneOffset.ofHours(8)).toInstant().toEpochMilli()));
    }

    public void test11() {
        // temperalAdjust 时间校验器
        // 例如获取下周日  下一个工作日
        LocalDateTime ldt = LocalDateTime.now();
        System.out.println((ldt));

        // 获取一年中的第一天
        LocalDateTime ldt1 = ldt.withDayOfYear(1);
        System.out.println((ldt1));
        // 获取一个月中的第一天
        LocalDateTime ldt2 = ldt.withDayOfMonth(1);
        System.out.println(ldt2);

        LocalDateTime ldt3 = ldt.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        System.out.println((ldt3));

        // 获取下一个工作日
        LocalDateTime ldt4 = ldt1.with((t) -> {
            LocalDateTime ldt5 = (LocalDateTime) t;
            DayOfWeek dayOfWeek = ldt5.getDayOfWeek();
            if (DayOfWeek.FRIDAY.equals(dayOfWeek)) {
                return ldt5.plusDays(3);
            } else if (DayOfWeek.SATURDAY.equals(dayOfWeek)) {
                return ldt5.plusDays(2);
            } else {
                return ldt5.plusDays(1);
            }
        });
        System.out.println(ldt4);
    }

    public void test12() {
        // DateTimeFormatter: 格式化时间/日期
        // 自定义格式
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        String strDate1 = ldt.format(formatter);
        String strDate = formatter.format(ldt);
        System.out.println((strDate));
        System.out.println((strDate1));

        // 使用api提供的格式
        DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE;
        LocalDateTime ldt2 = LocalDateTime.now();
        String strDate3 = dtf.format(ldt2);
        System.out.println(strDate3);

        // 解析字符串to时间
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        String localTime = df.format(time);
        LocalDateTime ldt4 = LocalDateTime.parse("2017-09-28 17:07:05", df);
        System.out.println("LocalDateTime转成String类型的时间：" + localTime);
        System.out.println("String类型的时间转成LocalDateTime：" + ldt4);

    }

    // ZoneTime  ZoneDate  ZoneDateTime
    public void test13() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        System.out.println(now);

        LocalDateTime now2 = LocalDateTime.now();
        ZonedDateTime zdt = now2.atZone(ZoneId.of("Asia/Shanghai"));
        System.out.println(zdt);

        Set<String> set = ZoneId.getAvailableZoneIds();
        set.stream().forEach(System.out::println);
    }

    //LocalDate
    public static void test14() {
        //获取当前日期,只含年月日 固定格式 yyyy-MM-dd    2018-05-04
        LocalDate today = LocalDate.now();

        // 根据年月日取日期，5月就是5，
        LocalDate oldDate = LocalDate.of(2018, 5, 1);

        // 根据字符串取：默认格式yyyy-MM-dd，02不能写成2
        LocalDate yesterday = LocalDate.parse("2018-05-03");

        // 如果不是闰年 传入29号也会报错
        LocalDate.parse("2018-02-29");
    }

    //LocalDate常用转化
    //日期转换常用,第一天或者最后一天...
    public static void test15() {
        //2018-05-04
        LocalDate today = LocalDate.now();
        // 取本月第1天： 2018-05-01
        LocalDate firstDayOfThisMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        // 取本月第2天：2018-05-02
        LocalDate secondDayOfThisMonth = today.withDayOfMonth(2);
        // 取本月最后一天，再也不用计算是28，29，30还是31： 2018-05-31
        LocalDate lastDayOfThisMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        // 取下一天：2018-06-01
        LocalDate firstDayOf2015 = lastDayOfThisMonth.plusDays(1);
        // 取2018年10月第一个周三 so easy?：  2018-10-03
        LocalDate thirdMondayOf2018 = LocalDate.parse("2018-10-01").with(TemporalAdjusters.firstInMonth(DayOfWeek.WEDNESDAY));
    }

    //LocalTime
    public static void test16() {
        //16:25:46.448(纳秒值)
        LocalTime todayTimeWithMillisTime = LocalTime.now();
        //16:28:48 不带纳秒值
        LocalTime todayTimeWithNoMillisTime = LocalTime.now().withNano(0);
        LocalTime time1 = LocalTime.parse("23:59:59");
    }

    //LocalDateTime
    public static void test17() {
        //转化为时间戳  毫秒值
        long time1 = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long time2 = System.currentTimeMillis();

        //时间戳转化为localDatetime
        DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS");
        System.out.println(df.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time1), ZoneId.of("Asia/Shanghai"))));
    }
}
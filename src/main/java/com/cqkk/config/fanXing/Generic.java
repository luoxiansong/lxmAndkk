package com.cqkk.config.fanXing;

//泛型类的最基本写法（这么看可能会有点晕，会在下面的例子中详解）:
/*class 类名称<泛型标识：可以随便写任意标识号，标识指定的泛型的类型>{
    private 泛型标识 *//*（成员变量类型）*//* var;
        .....
        }*/


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//一个最普通的泛型类：
//此处T可以随便写为任意标识，常见的如T、E、K、V等形式的参数常用于表示泛型
//在实例化泛型类时，必须指定T的具体类型
public class Generic<T> {
    //key这个成员变量的类型为T,T的类型由外部指定
    private T key;

    public Generic(T key) { //泛型构造方法形参key的类型也为T，T的类型由外部指定
        this.key = key;
    }

    //我想说的其实是这个，虽然在方法中使用了泛型，但是这并不是一个泛型方法。
    //这只是类中一个普通的成员方法，只不过他的返回值是在声明泛型类已经声明过的泛型。
    //所以在这个方法中才可以继续使用 T 这个泛型。
    public T getKey() { //泛型方法getKey的返回值类型为T，T的类型由外部指定
        return key;
    }

    //这也不是一个泛型方法，这就是一个普通的方法，只是使用了Generic<Number>这个泛型类做形参而已。
//    public static void showKeyValue(Generic<Number> obj){
//        System.out.println("泛型测试"+"key value is " + obj.getKey());
//    }

    //这也不是一个泛型方法，这也是一个普通的方法，只不过使用了泛型通配符?
    //同时这也印证了泛型通配符章节所描述的，?是一种类型实参，可以看做为Number等所有类的父类
    public static void showKeyValue1(Generic<?> obj) {
        System.out.println("泛型测试" + "key value is " + obj.getKey());
    }

    /**
     * 泛型方法的基本介绍
     *
     * @param tClass 传入的泛型实参
     * @return T 返回值为T类型
     * 说明：
     * 1）public 与 返回值中间<T>非常重要，可以理解为声明此方法为泛型方法。
     * 2）只有声明了<T>的方法才是泛型方法，泛型类中的使用了泛型的成员方法并不是泛型方法。
     * 3）<T>表明该方法将使用泛型类型T，此时才可以在方法中使用泛型类型T。
     * 4）与泛型类的定义一样，此处T可以随便写为任意标识，常见的如T、E、K、V等形式的参数常用于表示泛型。
     */
    public static <T> T genericMethod(Class<T> tClass) throws InstantiationException,
            IllegalAccessException {
        T instance = tClass.newInstance();
        return instance;
    }

    /**
     * 这才是一个真正的泛型方法。
     * 首先在public与返回值之间的<T>必不可少，这表明这是一个泛型方法，并且声明了一个泛型T
     * 这个T可以出现在这个泛型方法的任意位置.
     * 泛型的数量也可以为任意多个
     * 如：public <T,K> K showKeyName(Generic<T> container){
     * ...
     * }
     */
    public <T> T showKeyName(GenericTest.Generic<T> container) {
        System.out.println("container key :" + container.getKey());
        //当然这个例子举的不太合适，只是为了说明泛型方法的特性。
        T test = container.getKey();
        return test;
    }

    /**
     * 这个方法是有问题的，编译器会为我们提示错误信息："UnKnown class 'E' "
     * 虽然我们声明了<T>,也表明了这是一个可以处理泛型的类型的泛型方法。
     * 但是只声明了泛型类型T，并未声明泛型类型E，因此编译器并不知道该如何处理E这个类型。
     public <T> T showKeyName(Generic<E> container){
     ...
     }
     */

    /**
     * 这个方法也是有问题的，编译器会为我们提示错误信息："UnKnown class 'T' "
     * 对于编译器来说T这个类型并未项目中声明过，因此编译也不知道该如何编译这个类。
     * 所以这也不是一个正确的泛型方法声明。
     * public void showkey(T genericObj){
     * <p>
     * }
     */

    //泛型方法与可变参数
    public static <T> void printMsg(T... args) {
        for (T t : args) {
            System.out.println("泛型测试" + "t is " + t);
        }
    }

    //为泛型添加上边界，即传入的类型实参必须是指定类型的子类型
    public void showKeyValue3(Generic<? extends Number> obj) {
        System.out.println("泛型测试" + "key value is " + obj.getKey());
    }

    //在泛型方法中添加上下边界限制的时候，必须在权限声明与返回值之间的<T>上添加上下边界，即在泛型声明的时候添加
    //public <T> T showKeyName(Generic<T extends Number> container)，编译器会报错："Unexpected bound"
    public <T extends Number> T showKeyName(Generic<T> container){
        System.out.println("container key :" + container.getKey());
        T test = container.getKey();
        return test;
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        //泛型的类型参数只能是类类型（包括自定义类），不能是简单类型
        //传入的实参类型需与泛型的类型参数类型相同，即为Integer.
        Generic<Integer> genericInteger = new Generic<Integer>(123456);

        //传入的实参类型需与泛型的类型参数类型相同，即为String.
        Generic<String> genericString = new Generic<String>("key_vlaue");
        System.out.println("泛型测试" + "key is " + genericInteger.getKey());
        System.out.println("泛型测试" + "key is " + genericString.getKey());
        //泛型测试key is 123456
        //泛型测试key is key_vlaue

        //定义的泛型类，就一定要传入泛型类型实参么？并不是这样，在使用泛型的时候如果传入泛型实参，则会根据传入的泛型实参做相应的限制，
        //此时泛型才会起到本应起到的限制作用。如果不传入泛型类型实参的话，在泛型类中使用泛型的方法或成员变量定义的类型可以为任何的类型。
        Generic generic = new Generic("111111");
        Generic generic1 = new Generic(4444);
        Generic generic2 = new Generic(55.55);
        Generic generic3 = new Generic(false);

        System.out.println("泛型测试" + "key is " + generic.getKey());
        System.out.println("泛型测试" + "key is " + generic1.getKey());
        System.out.println("泛型测试" + "key is " + generic2.getKey());
        System.out.println("泛型测试" + "key is " + generic3.getKey());
        //泛型测试key is 111111
        //泛型测试key is 4444
        //泛型测试key is 55.55
        //泛型测试key is false

        Generic<Integer> gInteger = new Generic<Integer>(123);
        Generic<Number> gNumber = new Generic<Number>(456);

        //showKeyValue(gInteger);
        //java: 不兼容的类型: com.cqkk.config.fanXing.Generic<java.lang.Integer>
        //无法转换为com.cqkk.config.fanXing.Generic<java.lang.Number>
        //通过提示信息我们可以看到Generic<Integer>不能被看作为`Generic<Number>的子类。
        // 由此可以看出:同一种泛型可以对应多个版本（因为参数类型是不确定的），不同版本的泛型类实例是不兼容的。
        //
        //回到上面的例子，如何解决上面的问题？总不能为了定义一个新的方法来处理Generic<Integer>类型的类，这显然与java中的多台理念相违背
        //因此我们需要一个在逻辑上可以表示同时是Generic<Integer>和Generic<Number>父类的引用类型。由此类型通配符应运而生。

        showKeyValue1(gInteger);
        //类型通配符一般是使用？代替具体的类型实参，注意了，此处’？’是类型实参，而不是类型形参 。重要说三遍！此处’？’是类型实参，
        // 而不是类型形参 ！ 此处’？’是类型实参，而不是类型形参 ！再直白点的意思就是，此处的？
        // 和Number、String、Integer一样都是一种实际的类型，可以把？看成所有类型的父类。是一种真实的类型。
        //
        //可以解决当具体类型不确定的时候，这个通配符就是 ?  ；当操作类型时，不需要使用类型的具体功能时，只使用Object类中的功能。
        // 那么可以用 ? 通配符来表未知类型。

//        Object obj = genericMethod(Class.forName("com.cqkk.config.fanXing.Generic"));

        printMsg("111", 222, "aaaa", "2323.4", 55.55);

        //这一行代码编译器会提示错误，因为String类型并不是Number类型的子类
        //showKeyValue1(generic1);

        //Generic<String> generic1 = new Generic<String>("11111");
        //这一行代码编译器会提示错误，因为String类型并不是Number类型的子类
        //showKeyValue3(generic1);
        //通过上面的两个例子可以看出：泛型的上下边界添加，必须与泛型的声明在一起

    }
}

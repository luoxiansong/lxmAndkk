#JAVA泛型
泛型，即“参数化类型”。一提到参数，最熟悉的就是定义方法时有形参，然后调用此方法时传递实参。那么参数化类型怎么理解呢？
顾名思义，就是将类型由原来的具体的类型参数化，类似于方法中的变量参数，
此时类型也定义成参数形式（可以称之为类型形参），然后在使用/调用时传入具体的类型（类型实参）。

泛型的本质是为了参数化类型（在不创建新的类型的情况下，通过泛型指定的不同类型来控制形参具体限制的类型）。
也就是说在泛型使用过程中，操作的数据类型被指定为一个参数，这种参数类型可以用在类、接口和方法中，分别被称为泛型类、泛型接口、泛型方法。

未使用泛型时，可以通过Object来实现参数的“任意化”，但这样做的缺点就是需要显式的强制类型转换，这就需要开发者知道实际的类型。
而强制类型转换是会出现错误的，比如Object将实际类型为String，强转成Integer。
编译期是不会提示错误的，而在运行时就会抛出异常，很明显的安全隐患。
Java通过引入泛型机制，将上述的隐患提前到编译期进行检查，开发人员既可明确的知道实际类型，
又可以通过编译期的检查提示错误，从而提升代码的安全性和健壮性。

#特性  使用泛型前后的对比
泛型只在编译阶段有效。
拿一个经典的例子来演示一下未使用泛型会出现的问题。
        List list = new ArrayList();
        list.add(1);
        list.add("zhuan2quan");
        list.add("程序新视界");

        for (int i = 0; i < list.size(); i++) {
        String value = (String) list.get(i);
        System.out.println("value=" + value);
        }
上述代码在编译器并不会报任何错误，但当执行时会抛出如下异常：
java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.String

那么，是否可以在编译器就解决这个问题，而不是在运行期抛出异常呢？泛型应运而生。上述代码通过泛型来写之后，变成如下形式：
        List<String> list = new ArrayList<>();
        list.add(1);
        list.add("zhuan2quan");
        list.add("程序新视界");
        
        for (String value : list) {
        System.out.println("value=" + value);
        }
可以看出，代码变得更加清爽简单，而且list.add(1)这行代码在IDE中直接会提示错误信息：
Required type: String
Provided: int
提示错误信息便是泛型对向List中添加的数据产生了约束，只能是String类型。

#泛型中通配符
在使用泛型时经常会看到T、E、K、V这些通配符，它们代表着什么含义呢？
本质上它们都是通配符，并没有什么区别，换成A-Z之间的任何字母都可以。不过在开发者之间倒是有些不成文的约定：
T (type) 表示具体的一个java类型；
K V (key value) 分别代表java键值中的Key Value；
E (element) 代表Element

#为什么Java的泛型是假泛型
List<String> stringArrayList = new ArrayList<String>();
List<Integer> integerArrayList = new ArrayList<Integer>();
Class classStringArrayList = stringArrayList.getClass();
Class classIntegerArrayList = integerArrayList.getClass();

if(classStringArrayList.equals(classIntegerArrayList)){
    Log.d("泛型测试","类型相同");
}
输出结果：D/泛型测试: 类型相同。
通过上面的例子可以证明，在编译之后程序会采取去泛型化的措施。也就是说Java中的泛型，只在编译阶段有效。
在编译过程中，正确检验泛型结果后，也就是说所有的泛型参数类型在编译后都会被清除掉。这就是我们经常说的类型擦除。
因此，也可以说：泛型类型在逻辑上看以看成是多个不同的类型，实际上都是相同的基本类型。

#泛型的使用
泛型有三种使用方式，分别为：泛型类、泛型接口、泛型方法
我们需要明确一个基本准则，那就是泛型的声明通常都是通过<>配合大写字母来定义的，比如<T>。只不过不同类型，声明的位置不同，使用的方式也有所不同。

#泛型类
泛型类的语法形式:
class name<T1, T2, ..., Tn> { /* ... */ }
泛型类的声明和非泛型类的声明类似，只是在类名后面添加了类型参数声明部分。由尖括号（<>）分隔的类型参数部分跟在类名后面。
它指定类型参数（也称为类型变量）T1，T2，…和 Tn。一般将泛型中的类名称为原型，而将<>指定的参数称为类型参数。
使用示例:
// T为任意标识，比如用T、E、K、V等表示泛型
public class Foo<T> {

    // 泛化的成员变量，T的类型由外部指定
    private T info;

    // 构造方法类型为T，T的类型由外部指定
    public Foo(T info){
        this.info = info;
    }

    // 方法返回值类型为T，T的类型由外部指定
    public T getInfo() {
        return info;
    }

    public static void main(String[] args) {
        // 实例化泛型类时，必须指定T的具体类型，这里为String。
        // 传入的实参类型需与泛型的类型参数类型相同，这里为String。
        Foo<String> foo = new Foo<>("程序新视界");
        System.out.println(foo.getInfo());
    }
}
当然，上述示例中在使用泛型类时也可以不指定实际类型，语法上支持，那么此时与未定义泛型一样，不推荐这种方式。
Foo foo11 = new Foo(1);
比如上述写法，也是可行的，但时区了定义泛型的意义了。

#泛型接口
泛型接口的声明与泛型类一致，泛型接口语法形式:
public interface Context<T> {
    T getContext();
}
泛型接口有两种实现方式: 子类明确声明泛型类型和子类不明确声明泛型类型。
先看子类明确声明泛型类型的示例:
// 实现泛型接口时已传入实参类型，则所有使用泛型的地方都要替换成传入的实参类型
public class TomcatContext implements Context<String> {
    @Override
    public String getContext() {
        return "Tomcat";
    }
}
子类不明确声明泛型类型:
// 未传入泛型实参时，与泛型类的定义相同，在声明类的时候，需将泛型的声明也一起加到类中
public class SpringContext<T> implements Context<T>{
        @Override
        public T getContext() {
            return null;
        }
}
当然，还有一种情况，就是我们把定义为泛型的类像前面讲的一样当做普通类使用。
上面的示例中泛型参数都是一个，当然也可以指定两个或多个：
public interface GenericInterfaceSeveralTypes< T, R > {
        R performAction( final T action );
}
多个泛型参数可以用逗号（,）进行分割。

#泛型方法
泛型类是在实例化类时指明泛型的具体类型；泛型方法是在调用方法时指明泛型的具体类型。
泛型方法可以是普通方法、静态方法、抽象方法、final修饰的方法以及构造方法。
泛型方法语法形式如下:
public <T> T func(T obj) {}
尖括号内为类型参数列表，位于方法返回值T或void关键字之前。尖括号内定义的T，可以用在方法的任何地方，比如参数、方法内和返回值。
protected abstract<T, R> R performAction( final T action );

static<T, R> R performActionOn( final Collection< T > action ) {
    final R result = ...;
    // Implementation here
    return result;
}
上述实例中可以看出泛型方法同样可以定义多个泛型类型。
再看一个示例代码:
public class GenericsMethodDemo1 {

    //1、public与返回值中间<T>，声明此方法的泛型类型。
    //2、只有声明了<T>的方法才是泛型方法，泛型类中的使用了泛型的成员方法并不是泛型方法。
    //3、<T>表明该方法将使用泛型类型T，此时才可以在方法中使用泛型类型T。
    //4、T可以为任意标识，如T、E、K、V等。
    public static <T> T printClass(T obj) {
        System.out.println(obj);
        return obj;
    }

    public static void main(String[] args) {
        printClass("abc");
        printClass(123);
    }
}
需要注意的是，泛型方法与类是否是泛型无关。
另外，静态方法无法访问类上定义的泛型；如果静态方法操作的引用数据类型不确定的时候，必须要将泛型定义在方法上。
上述示例如果GenericsMethodDemo1定义为GenericsMethodDemo1<T>，
则printClass方法是无法直接使用到类上的T的，只能像上面代码那样访问自身定义的T。

#泛型方法与普通方法的区别
下面，我们对比一下泛型方法和非泛型方法的区别:
// 方法一
public T getKey(){
    return key;
}

// 方法二
public <T> T showKeyName(T t){
    return t;
}
其中方法一虽然使用了T这个泛型声明，但它用的是泛型类中定义的变量，因此这个方法并不是泛型方法。而像方法二中通过两个尖括号声明了T，这个才是真正的泛型方法。
对于方法二，还有一种情况，那就是类中也声明了T，那么该方法参数的T指的只是此方法的T，而并不是类的T。

#泛型方法与可变参数
@SafeVarargs
public final <T> void print(T... args){
    for(T t : args){
        System.out.println("t=" + t);
    }
}

public static void main(String[] args) {
    GenericDemo2 demo2 = new GenericDemo2();
    demo2.print("abc",123);
}
print方法打印出可变参数args中的结果，而且可变参数可以传递不同的具体类型。
打印结果:
t=abc
t=123
关于泛型方法总结一下就是：如果能使用泛型方法尽量使用泛型方法，这样能将泛型所需到最需要的范围内。
如果使用泛型类，则整个类都进行了泛化处理。

#泛型通配符
类型通配符一般是使用?代替具体的类型实参（此处是类型实参，而不是类型形参）。
当操作类型时不需要使用类型的具体功能时，只使用Object类中的功能，那么可以用?通配符来表未知类型。
例如List<?>在逻辑上是List<String>、List<Integer>等所有List<具体类型实参>的父类。
        
         //在使用List<Number>作为形参的方法中，不能使用List<Ingeter>的实例传入，
         //也就是说不能把List<Integer>看作为List<Number>的子类；
          public static void getNumberData(List<Number> data) {
          System.out.println("data :" + data.get(0));
          }
        
          //在使用List<String>作为形参的方法中，不能使用List<Number>的实例传入;
          //public static void getStringData(List<String> data) {
          //System.out.println("data :" + data.get(0));
          }

         //使用类型通配符可以表示同时是List<Integer>和List<Number>、List<String>的引用类型。
         //类型通配符一般是使用?代替具体的类型实参，注意此处是类型实参；
         //和Number、String、Integer一样都是一种实际的类型，可以把？看成所有类型的父类。
          public static void getData(List<?> data) {
          System.out.println("data :" + data.get(0));
          }
上述三个方法中，getNumberData只能传递List<Number>类型的参数，getStringData只能传递List<String>类型的参数。
如果它们都只使用了Object类的功能，则可以通过getData方法的形式进行声明，则同时支持各种类型。
上述这种类型的通配符也称作无界通配符，有两种应用场景:
1.可以使用Object类中提供的功能来实现的方法。
2.使用不依赖于类型参数的泛型类中的方法。
在getData中使用了？作为通配符，但在某些场景下，需要对泛型类型实参进行上下边界的限制。
如：类型实参只准传入某种类型的父类或某种类型的子类。
      //类型通配符上限通过形如List来定义，如此定义就是通配符泛型值接受Number及其下层子类类型。
      public static void getUperNumber(List<? extends Number> data) {
            System.out.println("data :" + data.get(0));
      }
通过extends限制了通配符的上边界，也就是只接受Number及其子类类型。接口的实现和类的集成都可以通过extends来表示。
而这里的Number也可以替换为T，表示该通配符所代表的类型是T类型的子类。
    public static void getData(List<? extends T> data) {
            System.out.println("data :" + data.get(0));
    }
与上界通配符示对照也有下界通配符:
    public static void getData(List<? super Integer> data) {
            System.out.println("data :" + data.get(0));
    }
下界通配符表示该通配符所代表的类型是T类型的父类。

#泛型的限制
原始类型（比如：int,long,byte等）无法用于泛型，在使用的过程中需要通过它们的包装类（比如：Integer, Long, Byte等）来替代。
final List< Long > longs = new ArrayList<>();
final Set< Integer > integers = new HashSet<>();

当然，在使用的过程中会涉及到自动拆箱和自动装箱的操作:
final List<Long> longs = new ArrayList<>();
longs.add(0L); // 'long' 包装为 'Long'

long value = longs.get( 0 ); // 'Long'解包'long'

#泛型的类型推断
当引入泛型之后，每处用到泛型的地方都需要开发人员加入对应的泛型类型，比如：
final Map<String, Collection<String>> map = new HashMap<String, Collection<String>>();

for(final Map.Entry< String, Collection<String> > entry: map.entrySet()) {
}
为了解决上述问题，在Java7中引入了运算符<>，编译器可以推断出该运算符所代表的原始类型。
因此，Java7及以后，泛型对象的创建变为如下形式:
final Map< String, Collection<String>> map = new HashMap<>();













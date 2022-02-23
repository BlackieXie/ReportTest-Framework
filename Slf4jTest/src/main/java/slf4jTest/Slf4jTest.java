package slf4jTest;

//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class Slf4jTest {
    @Test
    public void test01(){
        /*
                SLF4J对日志的级别划分
                trace、debug、info、warn、error五个级别
                trace：日志追踪信息  debug：日志详细信息
                info：日志的关键信息 默认打印级别
                warn：日志警告信息 error：日志错误信息
                在没有任何其他日志实现框架集成的基础之上
                slf4j使用的就是自带的框架slf4j-simple
                slf4j-simple也必须以单独依赖的形式导入进来
         */
        Logger logger = LoggerFactory.getLogger(Slf4jTest.class);
        logger.trace("trace信息");
        logger.debug("debug信息");
        logger.info("info信息");
        logger.warn("warn信息");
        logger.error("error信息");

    }

    @Test
    public void test02(){
        /*
            使用占位符的形式来代替字符串的拼接
         */
        Logger logger = LoggerFactory.getLogger(Slf4jTest.class);
        String name = "Kobe";
        int age = 24;
        logger.info("学生信息-姓名："+name+"；年龄："+age);
        logger.info("学生信息-姓名：{}，年龄：{}",new Object[]{name,age});
        logger.info("学生信息-姓名：{}，年龄：{}",name,age);

    }

    @Test
    public void test03(){
        /*
            日志对于异常信息的处理 没有用logback
         */
        Logger logger = LoggerFactory.getLogger(Slf4jTest.class);
        try {
            Class.forName("aaa");
        } catch (ClassNotFoundException e) {
            //打印栈追踪信息
            //e.printStackTrace();
            logger.info("XXX类中的XXX方法出现了异常，请及时关注信息");
            //e是引用类型对象，不能根前面的{}做有效的字符串拼接
            //logger.info("具体错误是：{}",e);
            //我们不用加{},直接后面加上异常对象e即可
            logger.info("具体错误是：",e);
        }
    }
    @Test
    public void test04(){
        /* 使用logback打印异常信息
            SLF4J日志门面，共有3种情况对日志实现进行绑定
            1.在没有绑定任何日志实现的基础之上，日志是不能够绑定实现任何功能的
                slf4j-simple是slf4j官方提供，使用需要导入依赖，自动绑定到slf4j门面上
                如果不导入，slf4j 核心依赖是不提供任何实现的
            2.logback和simple（包括nop）
                都是slf4j门面时间线后面提供的日志实现，所以API完全遵循slf4j进行的设计
                只需要导入想要使用的日志实现依赖，即可与slf4j无缝衔接
                nop虽然也划分到实现中了，但是是指不实现日志记录
            3.log4j和JUL
                都是slf4j门面时间线前面的日志实现，所以API不遵循slf4j进行设计
                通过适配桥接的技术，完成的与日志门面的衔接


            测试1：
                在原有slf4j-simple日志实现的基础上，集成logback
                通过测试，日志是打印出来了 java.lang.ClassNotFoundException: aaa
                通过这一句可以发现SLF4J: Actual binding is of type [org.slf4j.impl.SimpleLoggerFactory]
                虽然集成了logback，但是我们现在使用的仍然是slf4j-simple
                事实上只要出现了这个提示
                SLF4J: Class path contains multiple SLF4J bindings.
                在slf4j环境下，证明同时出现了多个日志实现
                如果先导入logback依赖，后导入slf4j-simple依赖
                那么默认使用的就是logback依赖
                如果有多个日志实现的话，默认使用先导入的实现
            测试：
                将slf4j-simple注释掉
                只留下logback，那么slf4j门面使用的就是logback日志实现
                值得一提的是，这一次没有多余的提示信息
                所以在实际应用的时候，一般情况下，仅仅只是做一种日志实现的集成就可以了
         */

        Logger logger = LoggerFactory.getLogger(Slf4jTest.class);

        try {
            Class.forName("aaa");
        } catch (ClassNotFoundException e) {
            logger.info("具体错误是：",e);
        }

    }

    @Test
    public void test05(){
        /*
            使用slf4j-nop 表示不记录日志
                首先还是导入实现依赖
                这个实现依赖，根据我们之前所总结出来的日志日志实现种类的第二种
                与logback和simple是属于一类的
                通过集成依赖的顺序而定
                所以如果想要让nop发挥效果，禁止所有日志的打印
                那么就必须要将slf4j-nop的依赖放在所有日志实现依赖的上方
         */
        Logger logger = LoggerFactory.getLogger(Slf4jTest.class);

        try {
            Class.forName("aaa");
        } catch (ClassNotFoundException e) {
            logger.info("具体错误是：",e);
        }

    }

    @Test
    public void test06(){
        /* 绑定log4j
            想要使用，需要绑定一个适配器
            叫做slf4j-log4j12
            再导入log4j的实现
            测试：
            log4j:WARN No appenders could be found for logger (com.bjpowernode.slf4j.test01.SLF4JTest01).
            log4j:WARN Please initialize the log4j system properly.
            log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
            虽然日志信息没有打印出来，那么根据警告信息可以得出：
            使用了log4j日志实现框架
            提示appender没有加载，需要在执行日志之前做相应的加载工作（初始化）
            我们可以将log4j的配置文件导入使用
            测试结果为log4j的日志打印，而且格式和级别完全是遵循log4j的配置文件进行的输出
         */
        Logger logger = LoggerFactory.getLogger(Slf4jTest.class);
        logger.trace("trace信息");
        logger.debug("debug信息");
        logger.info("info信息");
        logger.warn("warn信息");
        logger.error("error信息");
    }

    @Test
    public void test07(){
        /*   适配JDK14
                适配器导入之后，JUL日志实现是不用导入依赖的
                因为JUL，是JDK内置的
                从测试结果来看，是JUL的日志打印，默认是info级别日志的输出
         */
        Logger logger = LoggerFactory.getLogger(Slf4jTest.class);
        logger.trace("trace信息");
        logger.debug("debug信息");
        logger.info("info信息");
        logger.warn("warn信息");
        logger.error("error信息");
    }

    @Test
    public void test08(){
        /*
            绑定多个日志实现，会出现警告信息
            通过源码来看看其原理（看看slf4j的执行原理）
            进入到getLogger
            看到Logger logger = getLogger(clazz.getName());
            进入重载的getLogger
            ILoggerFactory iLoggerFactory = getILoggerFactory(); 用来取得Logger工厂实现的方法
            进入getILoggerFactory()
            看到以双重检查锁的方式去做判断
            执行performInitialization(); 工厂的初始化方法
            进入performInitialization()
            bind()就是用来绑定具体日志实现的方法
            进入bind()
            看到Set集合Set<URL> staticLoggerBinderPathSet = null;
            因为当前有可能会有N多个日志框架的实现
            看到staticLoggerBinderPathSet = findPossibleStaticLoggerBinderPathSet();
            进入findPossibleStaticLoggerBinderPathSet()
            看到创建了一个有序不可重复的集合对象
            LinkedHashSet staticLoggerBinderPathSet = new LinkedHashSet();
            声明了枚举类的路径，经过if else判断，以获取系统中都有哪些日志实现
            看到Enumeration paths;
            if (loggerFactoryClassLoader == null) {
                paths = ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH);
            } else {
                paths = loggerFactoryClassLoader.getResources(STATIC_LOGGER_BINDER_PATH);
            }
            主要观察常量STATIC_LOGGER_BINDER_PATH
            通过常量我们会找到类StaticLoggerBinder
            这个类是以静态的方式绑定Logger实现的类
            来自slf4j-JDK14的适配器
            进入StaticLoggerBinder
            看到new JDK14LoggerFactory();
            进入JDK14LoggerFactory类的无参构造方法
            看到java.util.logging.Logger.getLogger("");
            使用的就是jul的Logger

            接着观察findPossibleStaticLoggerBinderPathSet
            看到以下代码，表示如果还有其他的日志实现
            while(paths.hasMoreElements()) {
                URL path = (URL)paths.nextElement();
                将路径添加进入
                staticLoggerBinderPathSet.add(path);
            }

            回到bind方法
            表示对于绑定多实现的处理
            reportMultipleBindingAmbiguity(staticLoggerBinderPathSet);
            如果出现多日志实现的情况
            则会打印
            Util.report("Class path contains multiple SLF4J bindings.");

            总结：
                在真实生产环境中，slf4j只绑定一个日志实现框架就可以了
                绑定多个，默认使用导入依赖的第一个，而且会产生没有必要的警告信息
         */
        Logger logger = LoggerFactory.getLogger(Slf4jTest.class);
        logger.trace("trace信息");
        logger.debug("debug信息");
        logger.info("info信息");
        logger.warn("warn信息");
        logger.error("error信息");
    }

    @Test
    public void test09(){
        /*  slf4j+logback的组合
                将所有关于其他日志实现和门面依赖全部去除
                仅仅只留下log4j的依赖
                测试的过程中，只能使用log4j相关的组件

                此时需要将日志替换为slf4j+logback
                我们既然不用log4j了，就将log4j去除
                将slf4j日志门面和logback的日志实现依赖加入进来
                这样做，没有了log4j环境的支持，编译报错

                这个时候就需要使用桥接器来做这个需求了
                桥接器解决的是项目中日志的重构问题，当前系统中存在之前的日志API，可以通过桥接转换到slf4j的实现

                桥接器的使用步骤：
                1.去除之前旧的日志框架依赖
                    <dependency>
                        <groupId>log4j</groupId>
                        <artifactId>log4j</artifactId>
                        <version>1.2.17</version>
                    </dependency>
                2.添加slf4j提供的桥接组件
                    log4j相关的桥接器
                    <dependency>
                        <groupId>org.slf4j</groupId>
                        <artifactId>log4j-over-slf4j</artifactId>
                        <version>1.7.25</version>
                    </dependency>
                  桥接器加入后，代码编译就不报错了
                  测试：
                    日志信息输出
                    输出格式为logback
                    证明了现在使用的确实是slf4j门面+logback实现

                    在重构之后，就会为我们造成这样一种假象
                    使用的明明是log4j包下的日志组件资源
                    但是真正日志的实现，却是使用slf4j门面+logback实现
                    这就是桥接器给我们带来的效果

                  注意：
                      在桥接器加入之后，适配器就没有必要加入了
                      桥接器和适配器不能同时导入依赖
                      桥接器如果配置在适配器的上方，则运行报错，不同同时出现
                      桥接器如果配置在适配器的下方，则不会执行桥接器，没有任何的意义

         */
//        Logger logger = LogManager.getLogger(SLF4JTest01.class);

//        logger.info("info信息");


    }
    @Test
    public void test10(){

        /*

            在配置了桥接器之后，底层就是使用slf4j实现的日志
            分析其中原理

            通过getLogger
            进入Log4jLoggerFactory
            Logger newInstance = new Logger(name); 新建logger对象

            进入构造方法
            protected Logger(String name) {
                super(name);
            }

            点击进入父类的构造方法
            Category(String name) {
                this.name = name;
                this.slf4jLogger = LoggerFactory.getLogger(name);
                if (this.slf4jLogger instanceof LocationAwareLogger) {
                    this.locationAwareLogger = (LocationAwareLogger)this.slf4jLogger;
                }
            }

            在这个Category构造方法中，核心代码
                this.slf4jLogger = LoggerFactory.getLogger(name);

            LoggerFactory来自于org.slf4j

         */
//        Logger logger = LogManager.getLogger(SLF4JTest01.class);
    }
}

package log4j;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.LogLog;
import org.junit.Test;

public class log4jTest {
    @Test
    public void test01(){
        /*
            日志级别说明： Log4j提供了8个级别的日志输出，分别为
                ALL 最低等级 用于打开所有级别的日志记录
                TRACE 程序推进下的追踪信息，这个追踪信息的日志级别非常低，一般情况下是不会使用的
                DEBUG 指出细粒度信息事件对调试应用程序是非常有帮助的，主要是配合开发，在开发过程中打印一些重要的运行信息
                INFO 消息的粗粒度级别运行信息
                WARN 表示警告，程序在运行过程中会出现的有可能会发生的隐形的错误
                        注意，有些信息不是错误，但是这个级别的输出目的就是为了给程序员以提示
                ERROR 系统的错误信息，发生的错误不影响系统的运行
                        一般情况下，如果不想输出太多的日志，则使用该级别即可
                FATAL 表示严重错误，它是那种一旦发生系统就不可能继续运行的严重错误
                        如果这种级别的错误出现了，表示程序可以停止运行了
                OFF 最高等级的级别，用户关闭所有的日志记录
                其中debug是我们在没有进行设置的情况下，默认的日志输出级别
         */
        //加载初始化配置 注意加载初始化信息：BasicConfigurator.configure();
        BasicConfigurator.configure();
        Logger logger = Logger.getLogger(log4jTest.class);
        logger.fatal("fatal信息");
        logger.error("error信息");
        logger.warn("warn信息");
        logger.info("info信息");
        logger.debug("debug信息");
        logger.trace("trace信息");
    }


    @Test
    public void test02(){

        /*
            配置文件的使用
                1.观察源码BasicConfigurator.configure();
                    可以得到两条信息：
                    （1）创建了根节点的对象，Logger root = Logger.getRootLogger();
                    （2）根节点添加了ConsoleAppender对象（表示默认打印到控制台，自定义的格式化输出）
                2.不使用BasicConfigurator.configure();
                    使用自定义的配置文件来实现功能 通过我们对于以上第一点的分析
                    我们的配置文件需要提供Logger、Appender、Layout这3个组件信息（通过配置来代替以上的代码）
                    分析：
                    Logger logger = Logger.getLogger(log4jTest.class);
                    进入到getLogger方法，会看到代码：
                    LogManager.getLogger(clazz.getName());
                    LogManager：日志管理器
                    点击LogManager，看看这个日志管理器中都实现了什么
                    看到很多常量信息，他们代表的就是不同形式（后缀名不同）的配置文件
                    最常使用到的是log4j.properties属性文件（语法简单，使用方便）

                    继续观察LogManager，找到其中的静态代码块static
                    在static代码块中，我们找到
                    Loader.getResource("log4j.properties");
                    这行代码给我们最大的一个提示信息就是
                    系统默认要从当前的类路径下找到log4j.properties
                    对于我们当前的项目是maven工程，那么理应在resources路径下去找

                    加载完毕后我们来观察配置文件是如何读取的？
                    继续观察LogManager 找到
                    OptionConverter.selectAndConfigure(url, configuratorClassName, getLoggerRepository());
                    作为属性文件的加载，执行相应的properties配置对象：configurator = new PropertyConfigurator();

                    进入到PropertyConfigurator类中，观察到里面的常量信息
                    这些常量信息就是我们在properties属性文件中的各种属性配置项
                    其中，我们看到了如下两项信息，这两项信息是必须要进行配置的
                    static final String ROOT_LOGGER_PREFIX = "log4j.rootLogger";
                    static final String APPENDER_PREFIX = "log4j.appender.";

                    通过代码：
                    String prefix = "log4j.appender." + appenderName;
                    我们需要自定义一个appendername，我们起名叫做console
                    （起名字也需要见名知意，console那么我们到时候的配置应该配置的就是控制台输出）
                    log4j.appender.console
                    取值就是log4j中为我们提供的appender类
                    例如：
                        log4j.appender.console=org.apache.log4j.ConsoleAppender

                    在appender输出的过程中，还可以同时指定输出的格式
                    通过代码：
                    String layoutPrefix = prefix + ".layout";
                    配置：
                    log4j.appender.console.layout=org.apache.log4j.SimpleLayout

                    通过log4j.rootLogger继续在类中搜索
                    找到void configureRootCategory方法
                    在这个方法中执行了this.parseCategory方法
                    观察该方法：
                    找打代码StringTokenizer st = new StringTokenizer(value, ",");
                    表示要以逗号的方式来切割字符串，证明了log4j.rootLogger的取值，其中可以有多个值，使用逗号进行分隔

                    通过代码：
                    String levelStr = st.nextToken();
                    表示切割后的第一个值是日志的级别

                    通过代码：
                    while(st.hasMoreTokens())
                    表示接下来的值，是可以通过while循环遍历得到的
                    第2~第n个值，就是我们配置的其他的信息，这个信息就是appenderName
                    证明了我们配置的方式
                    log4j.rootLogger=日志级别,appenderName1,appenderName2,appenderName3....
                    表示可以同时在根节点上配置多个日志输出的途径

                    通过我们自己的配置文件，就可以将原有的加载代码去除掉了
         */
        //BasicConfigurator.configure();
        Logger logger = Logger.getLogger(log4jTest.class);
        logger.fatal("fatal信息");
        logger.error("error信息");
        logger.warn("warn信息");
        logger.info("info信息");
        logger.debug("debug信息");
        logger.trace("trace信息");
    }

    @Test
    public void test03(){
        /*
            通过Logger中的开关
                打开日志输出的详细信息
                查看LogManager类中的方法
                getLoggerRepository()
                找到代码LogLog.debug(msg, ex);
                LogLog会使用debug级别的输出为我们展现日志输出详细信息
                Logger是记录系统的日志，那么LogLog是用来记录Logger的日志

                进入到LogLog.debug(msg, ex);方法中
                通过代码：if (debugEnabled && !quietMode) {
                观察到if判断中的这两个开关都必须开启才行
                !quietMode是已经启动的状态，不需要我们去管
                debugEnabled默认是关闭的  所以我们只需要设置debugEnabled为true就可以了
         */
        LogLog.setInternalDebugging(true);
        Logger logger = Logger.getLogger(log4jTest.class);
        logger.fatal("fatal信息");
        logger.error("error信息");
        logger.warn("warn信息");
        logger.info("info信息");
        logger.debug("debug信息");
        logger.trace("trace信息");
    }

    @Test
    public void test04(){
        /*
            关于log4j.properties layout属性的配置
                其中PatternLayout是日常使用最多的方式
                查看其源码
                setConversionPattern这个方法就是该PatternLayout的核心方法
                conversionPattern

                在log4j.properties中将layout设置为PatternLayout
                我们主要配置的是conversionPattern属性

                %m 输出代码中指定的日志信息
                %p 输出优先级，及 DEBUG、INFO 等
                %n 换行符（Windows平台的换行符为 "\n"，Unix 平台为 "\n"）
                %r 输出自应用启动到输出该 log 信息耗费的毫秒数
                %c 输出打印语句所属的类的全名
                %t 输出产生该日志的线程全名
                %d 输出服务器当前时间，默认为 ISO8601，也可以指定格式，如：%d{yyyy年MM月dd日 HH:mm:ss}
                %l 输出日志时间发生的位置，包括类名、线程、及在代码中的行数。如：Test.main(Test.java:10)
                %F 输出日志消息产生时所在的文件名称
                %L 输出代码中的行号
                %% 输出一个 "%" 字符
                [%p]%r %c%t%d{yyyy-MM-dd HH:mm:ss:SSS} %m%n
                可以在 % 与字符之间加上修饰符来控制最小宽度、最大宽度和文本的对其方式
                [%10p]：[]中必须有10个字符，由空格来进行补齐，信息右对齐
                [%-10p]：[]中必须有10个字符，由空格来进行补齐，信息左对齐
         */
        LogLog.setInternalDebugging(true);
        Logger logger = Logger.getLogger(log4jTest.class);
        logger.fatal("fatal信息");
        logger.error("error信息");
        logger.warn("warn信息");
        logger.info("info信息");
        logger.debug("debug信息");
        logger.trace("trace信息");
    }

    @Test
    public void test05(){
        /*
            将日志信息输出到文件中

            日志文件要保存到哪个磁盘相关的配置
            查看FileAppender的源码
            看到属性信息
            protected boolean fileAppend; 表示是否追加信息，通过构造方法赋值为true
            protected int bufferSize; 缓冲区的大小，通过构造方法赋值为8192

            找到setFile方法，这个方法就是用来指定文件位置的方法
            通过ognl，可以推断setFile方法操作的属性就是file

            如果有输出中文的需求怎么办
            观察FileAppender的父类
            找到protected String encoding;属性
         */
        Logger logger = Logger.getLogger(log4jTest.class);
        logger.fatal("fatal信息");
        logger.error("error信息");
        logger.warn("warn信息");
        logger.info("info信息");
        logger.debug("debug信息");
        logger.trace("trace信息");
    }

    @Test
    public void test06(){
        /*
            将日志输出到文件中
               FileAppender为我们提供了好用的子类来进一步的对于文件输出进行处理
               RollingFileAppender
               DailyRollingFileAppender

            1.RollingFileAppender
                这个类表示使用按照文件大小进行拆分的方式进行操作
                配置文件进行RollingFileAppender相关配置
                如何进行拆分，观察RollingFileAppender的源码
                protected long maxFileSize = 10485760L; 表示拆分文件的大小
                protected int maxBackupIndex = 1; 表示拆分文件的数量
                #指定日志文件内容大小
                log4j.appender.rollingFile.maxFileSize=1MB
                #指定日志文件的数量
                log4j.appender.rollingFile.maxBackupIndex=5
                只要文件超过1MB，那么则生成另外一个文件，文件的数量最多是5个
                文件1 记录日志 1MB
                文件2 记录日志 1MB
                ...
                ...
                文件5         1MB
                如果5个文件不够怎么办，作为日志管理来讲，也不可能让日志无休止的继续增长下去
                所以，覆盖文件的策略是，按照时间来进行覆盖，原则就是保留新的，覆盖旧的
            2.DailyRollingFileAppender
                按照时间来进行文件的拆分
                查看源码属性：
                private String datePattern = "'.'yyyy-MM-dd"; 默认是按照天来进行拆分的
         */

        Logger logger = Logger.getLogger(log4jTest.class);
        for (int i = 0; i < 10000; i++) {
            logger.fatal("fatal信息");
            logger.error("error信息");
            logger.warn("warn信息");
            logger.info("info信息");
            logger.debug("debug信息");
            logger.trace("trace信息");

        }

    }

    @Test
    public void test07(){

        /*
            将日志持久化到数据库表中
                创建表结构：（字段的制定可以根据需求进行调整）
                CREATE TABLE tbl_log(
                    id int(11) NOT NULL AUTO_INCREMENT,
                    name varchar(255) DEFAULT NULL COMMENT '项目名称',
                    createTime varchar(255) DEFAULT NULL COMMENT '创建时间',
                    level varchar(255) DEFAULT NULL COMMENT '日志级别',
                    category varchar(255) DEFAULT NULL COMMENT '所在类的全路径',
                    fileName varchar(255) DEFAULT NULL COMMENT '文件名称',
                    message varchar(255) DEFAULT NULL COMMENT '日志消息',
                    PRIMARY KEY(id)
                )
            对于数据库表的日志输出进行相应配置
            #配置appender输出方式 输出到数据库表
            log4j.appender.logDB=org.apache.log4j.jdbc.JDBCAppender
            log4j.appender.logDB.layout=org.apache.log4j.PatternLayout
            log4j.appender.logDB.Driver=com.mysql.jdbc.Driver
            log4j.appender.logDB.URL=jdbc:mysql://localhost:3306/test
            log4j.appender.logDB.User=root
            log4j.appender.logDB.Password=123456
            log4j.appender.logDB.Sql=INSERT INTO tbl_log(name,createTime,level,category,fileName,message) values('project_log','%d{yyyy-MM-dd HH:mm:ss}','%p','%c','%F','%m')
         */
        Logger logger = Logger.getLogger(log4jTest.class);
        logger.fatal("fatal信息");
        logger.error("error信息");
        logger.warn("warn信息");
        logger.info("info信息");
        logger.debug("debug信息");
        logger.trace("trace信息");
    }

    @Test
    public void test08(){

        /*
            Log4j的自定义logger
                我们以前所创建出来的Logger对象，默认都是继承rootLogger的
                我们也可以自定义logger，让其他logger来继承这个logger

                这种继承关系就是按照包结构的关系来进行指定的
                例如我们一直使用的
                Logger logger = Logger.getLogger(log4jTest.class);
                路径就是：log4j.log4jTest
                它的父logger就是上层的路径或者是更上层的路径
                例如：
                    log4j.log4jTest
                    log4j
                参照logger是如何加载配置文件的
                查看PropertyConfigurator的源码
                得到信息log4j.logger.
                这个属性值log4j.logger.就是我们在配置文件中对于自定义logger的配置属性

                假设我们现在的配置是这样的：
                #配置根节点logger
                log4j.rootLogger=trace,console
                #配置自定义logger
                log4j.logger.log4j.log4jTest=info,file

                观察结果：
                    从输出位置来看，控制台输出了信息，日志文件也输出了信息
                    所以可以得出结论，如果根节点的logger和自定义父logger配置的输出位置是不同的
                    则取二者的并集，配置的位置都会进行输出操作
                    如果二者配置的日志级别不同，以按照我们自定的父logger的级别输出为主
         */
        Logger logger = Logger.getLogger(log4jTest.class);
        logger.fatal("fatal信息");
        logger.error("error信息");
        logger.warn("warn信息");
        logger.info("info信息");
        logger.debug("debug信息");
        logger.trace("trace信息");
    }

    @Test
    public void test09(){
        /*
            自定义logger的应用场景
                例如：
                    在原有案例的基础之上，加上一个apache的日志输出
                    log4j.logger.org.apache=error,console

                    我们现在在配置文件中配置的logger有如下3项
                    log4j.rootLogger=trace,console
                    log4j.logger.log4j.log4jTest=info,file
                    log4j.logger.org.apache=error,console
         */

        /*
            当前的类路径log4j.log4jTest
            在配置文件中所找到的能够作用的父logger和根logger
            log4j.rootLogger=trace,console
            log4j.logger.log4j.log4jTest=info,file
            我们刚才配置的apache的路径和我们的log4j.log4jTest不相符
            不构成父子关系，所以没有执行apache相关的配置
         */
        Logger logger = Logger.getLogger(log4jTest.class);
        logger.fatal("fatal信息");
        logger.error("error信息");
        logger.warn("warn信息");
        logger.info("info信息");
        logger.debug("debug信息");
        logger.trace("trace信息");

        /*
            org.apache.log4j.Logger
                console在根节点中进行了配置
                在apache中也进行了配置
                由于输出的位置appender取的是并集
                所以，既然都配置了，那么就输出了两次
         */
        Logger logger1 = Logger.getLogger(Logger.class);
        logger1.fatal("fatal信息 --");
        logger1.error("error信息 --");
        logger1.warn("warn信息 --");
        logger1.info("info信息 --");
        logger1.debug("debug信息 --");
        logger1.trace("trace信息 --");
    }
}

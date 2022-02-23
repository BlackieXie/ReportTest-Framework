package Test01;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

public class log4j2Test {
    @Test
    public void test01(){
        /*
            入门案例
                单纯的使用Log4j2的 门面+实现
                Log4j2和log4j提供了相同的日志级别输出
                默认为error级别信息的打印

                ERROR StatusLogger No Log4j 2 configuration file found. Using default configuration
                表示我们还没有建立自己的配置文件
                如果没有建立配置文件，则使用默认的配置
         */
//        Logger logger = LogManager.getLogger(log4j2Test.class);
//        logger.fatal("fatal信息");
//        logger.error("error信息");
//        logger.warn("warn信息");
//        logger.info("info信息");
//        logger.debug("debug信息");
//        logger.trace("trace信息");
    }
    @Test
    public void test02(){
        /*
            使用配置文件
                log4j2是参考logback创作出来的，所以配置文件也是使用xml
                log4j2同样是默认加载类路径（resources）下的log4j2.xml文件中的配置

                根标签，所有日志相关信息，都是在根标签中进行配置
                <Configuration status="debug" monitorInterval="数值"></Configuration>
                在根标签中，可以加属性
                status="debug" 日志框架本身的日志输出级别
                monitorInterval="5" 自动加载配置文件的间隔时间，不低于5秒
         */
//        Logger logger = LogManager.getLogger(log4j2Test.class);
//        logger.fatal("fatal信息");
//        logger.error("error信息");
//        logger.warn("warn信息");
//        logger.info("info信息");
//        logger.debug("debug信息");
//        logger.trace("trace信息");
    }

    @Test
    public void test03(){
        /*
            slf4j+log4j2
            1.导入slf4j的日志门面
            2.导入log4j2的适配器
            3.导入log4j2的日志门面
            4.导入log4j2的日志实现
            执行原理：
                slf4j门面调用的是log4j2的门面，再由log4j2的门面调用log4j2的实现
         */
        Logger logger = LoggerFactory.getLogger(log4j2Test.class);
        logger.error("error信息");
        logger.warn("warn信息");
        logger.info("info信息");
        logger.debug("debug信息");
        logger.trace("trace信息");
    }
    @Test
    public void test04(){
        /*
            将日志输出到文件中
         */
        Logger logger = LoggerFactory.getLogger(log4j2Test.class);
        logger.error("error信息");
        logger.warn("warn信息");
        logger.info("info信息");
        logger.debug("debug信息");
        logger.trace("trace信息");
    }

    @Test
    public void test05(){
        /*
            日志的拆分
         */
        Logger logger = LoggerFactory.getLogger(log4j2Test.class);
        for (int i = 0; i < 2000; i++) {
            logger.error("error信息");
            logger.warn("warn信息");
            logger.info("info信息");
            logger.debug("debug信息");
            logger.trace("trace信息");
        }
    }

    @Test
    public void test06(){
        /*
            异步日志实现（单独分配线程做日志的记录）
                方式1：使用AsyncAppender的方式
                1.添加异步日志依赖
                2.在Appenders标签中，对于异步进行配置
                    使用Async标签
                3.rootlogger引用Async
         */
        Logger logger = LoggerFactory.getLogger(log4j2Test.class);
        //日志的记录
        for (int i = 0; i < 2000; i++) {
            logger.error("error信息");
            logger.warn("warn信息");
            logger.info("info信息");
            logger.debug("debug信息");
            logger.trace("trace信息");
        }
        //系统业务逻辑
        for (int i = 0; i < 1000; i++) {
            System.out.println("------------------");
        }
    }
    @Test
    public void test07(){
        /*
            异步日志实现（单独分配线程做日志的记录）
                方式2：使用AsyncLogger的方式
                全局异步
                    所有的日志都是异步的日志记录，在配置文件上不用做任何的改动
                    只需要在类路径resources下添加一个properties属性文件，做一步配置即可
                    文件名要求是：log4j2.component.properties
                    Log4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector
         */
        Logger logger = LoggerFactory.getLogger(log4j2Test.class);
        //日志的记录
        for (int i = 0; i < 2000; i++) {
            logger.error("error信息");
            logger.warn("warn信息");
            logger.info("info信息");
            logger.debug("debug信息");
            logger.trace("trace信息");
        }
        //系统业务逻辑
        for (int i = 0; i < 1000; i++) {
            System.out.println("------------------");
        }
    }

    @Test
    public void test08(){
        /*
            异步日志实现（单独分配线程做日志的记录）
                方式2：使用AsyncLogger的方式
                混合异步：
                    可以在应用中同时使用同步日志和异步日志，这使得日志的配置及输出会更加的灵活
                需求：
                    假设我们现在有自定义的logger -- Allen-log4j2Test
                    让自定义的logger是异步的
                    让rootlogger是同步的
                注意：
                    在做测试前，一定要将全局的异步配置注释掉

                对于当前的logger，Log4j2Test01.class
                Log4j2Test01本身就是在我们自定义的logger路径下的

                注意：
                    如果使用异步日志
                        AsyncAppender、AsyncLogger不要同时出现，没有这个需求，效果也不会叠加
                        如果同时出现，那么效率会以AsyncAppender为主
                        AsyncLogger的全局异步和混合异步也不要同时出现，没有这个需求，效果也不会叠加
         */
        Logger logger = LoggerFactory.getLogger(log4j2Test.class);
        //日志的记录
        for (int i = 0; i < 2000; i++) {
            logger.error("error信息");
            logger.warn("warn信息");
            logger.info("info信息");
            logger.debug("debug信息");
            logger.trace("trace信息");
        }
        //系统业务逻辑
        for (int i = 0; i < 1000; i++) {
            System.out.println("------------------");
        }
    }
}

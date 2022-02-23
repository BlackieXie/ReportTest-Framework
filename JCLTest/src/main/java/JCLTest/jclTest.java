package JCLTest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class jclTest {
    @Test
    public void test01(){

        /*
            默认的情况下，会使用JUL日志框架做日志的记录操作
            JCL使用原则：
                如果有log4j，优先使用log4j
                如果没有任何第三方日志框架的时候，我们使用的就是JUL
         */
        Log log = LogFactory.getLog(jclTest.class);
        log.info("info信息");
    }

    @Test
    public void test02(){
        /*
            导入log4j依赖，继续测试原有程序
            总结：
                我们上一个案例，使用的是JUL，但是在集成了log4j环境后，使用的又是log4j
                通过测试观察，虽然日志框架发生了变化，但是代码完全没有改变
            日志门面技术的好处：
                门面技术是面向接口的开发，不再依赖具体的实现类，减少代码的耦合性
                可以根据实际需求，灵活的切换日志框架
                统一的API，方便开发者学习和使用
                统一的配置管理便于项目日志的维护工作
            查看源码：
                Log接口的4个实现类
                JDk13
                JDK14 正常java.util.logging
                Log4j 我们集成的log4j
                Simple JCL自带实现类
                （1）查看Jdk14Logger证明里面使用的是JUL日志框架
                （2）查看Log4JLogger证明里面使用的是Log4j日志框架
                （3）观察LogFactory，看看如何加载的Logger对象
                     这是一个抽象类，无法实例化
                     需要观察其实现类LogFactoryImpl
                （4）观察LogFactoryImpl
                    真正加载日志实现使用的就是这个实现类LogFactoryImpl
                （5）进入getLog
                    进入getInstance
                    找到instance = this.newInstance(name);，继续进入
                    找到instance = this.discoverLogImplementation(name); 表示发现一个日志的实现
                    for(int i = 0; i < classesToDiscover.length && result == null; ++i) {
                        result = this.createLogFromClass(classesToDiscover[i], logCategory, true);
                    }
                    遍历我们拥有的日志实现框架
                    遍历的是一个数组，这个数组是按照
                    log4j
                    jdk14
                    jdk13
                    SimpleLogger
                    的顺序依次遍历
                    表示的是，第一个要遍历的就是log4j，如果有log4j则执行该日志框架
                    如果没有，则遍历出来第二个，使用jdk14的JUL日志框架
                    以此类推
                    result = this.createLogFromClass(classesToDiscover[i], logCategory, true);
                    表示帮我们创建Logger对象
                    在这个方法中，我们看到了
                    c = Class.forName(logAdapterClassName, true, currentCL);
                    是取得该类型的反射类型对象
                    使用反射的形式帮我们创建logger对象
                    constructor = c.getConstructor(this.logConstructorSignature);
         */
        Log log = LogFactory.getLog(jclTest.class);
        log.info("info信息");
    }
}

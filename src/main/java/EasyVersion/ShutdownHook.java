package EasyVersion;
import EasyVersion.Server.CommonDubboServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class ShutdownHook {
    private Logger logger=LoggerFactory.getLogger(ShutdownHook.class);
    private final ExecutorService threadPool=new ThreadPoolExecutor(5,50,60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(100), Executors.defaultThreadFactory());
    private static final ShutdownHook shutdownHook = new ShutdownHook();

    public static ShutdownHook getShutdownHook() {
        return shutdownHook;
    }
    public void addClearAllHook(CommonDubboServer server){
        logger.info("关闭后将自动注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            server.clearRegistry();
            threadPool.shutdown();
        }));
    }
}

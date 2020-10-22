package EasyVersion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class RpcServer {
    private final ExecutorService threadPool;
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);
    private static final int CORE_POOL_SIZE=5;
    private static final int MAXIMUM_POOL_SIZE=50;
    private static final int KEEP_ALIVE_TIME=60;
    private static final int BLOCKING_QUEUE_CAPACITY=100;
    private final ServiceRegistry serviceRegistry;
    private RequestHandler requestHandler = new RequestHandler();
    public RpcServer(ServiceRegistry serviceRegistry) {
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        this.serviceRegistry=serviceRegistry;
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workingQueue, threadFactory);
    }
    //由于服务的注册不再由RpcServer处理了，该服务器只需要启动就好了。
    public void start(int port){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("服务器正在启动.....");
            Socket socket;
            while((socket=serverSocket.accept())!=null){
                logger.info("消费者连接：{}：{}",socket.getInetAddress(),socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket, requestHandler, serviceRegistry));
            }
        } catch (IOException e) {
            logger.error("连接时有错误发生:"+e);
        }
    }
}

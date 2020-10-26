package EasyVersion;

import EasyVersion.register.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class SocketServer {
    private final ExecutorService threadPool;
    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);
    private static final int CORE_POOL_SIZE=5;
    private static final int MAXIMUM_POOL_SIZE=50;
    private static final int KEEP_ALIVE_TIME=60;
    private static final int BLOCKING_QUEUE_CAPACITY=100;
    private final ServiceProvider serviceProvider;
    private RequestHandler requestHandler = new RequestHandler();
    public SocketServer(ServiceProvider serviceProvider) {
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        this.serviceProvider = serviceProvider;
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workingQueue, threadFactory);
    }
    //由于服务的注册不再由RpcServer处理了，该服务器只需要启动就好了。
    public void start(int port){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("服务器正在启动.....");
            Socket socket;
            while((socket=serverSocket.accept())!=null){
                logger.info("消费者连接：{}：{}",socket.getInetAddress(),socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket, requestHandler, serviceProvider));
            }
        } catch (IOException e) {
            logger.error("连接时有错误发生:"+e);
        }
    }
}

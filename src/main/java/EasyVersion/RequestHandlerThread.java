package EasyVersion;

import EasyVersion.register.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import EasyVersion.Exception.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class RequestHandlerThread implements Runnable{
    private static final Logger logger= LoggerFactory.getLogger(RequestHandlerThread.class);

    private Socket socket;
    private RequestHandler requestHandler;
    private ServiceProvider serviceProvider;

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceProvider serviceProvider) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void run() {
        try(ObjectInputStream objectInputStream=new ObjectInputStream(socket.getInputStream())){
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
            RpcRequest request =(RpcRequest) objectInputStream.readObject();
            String interfaceName = request.getInterfaceName();
            Object service = serviceProvider.getService(interfaceName);
            Object result = requestHandler.handle(request, service);
            objectOutputStream.writeObject(RpcResponse.success(result));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException | RpcException|IllegalAccessException|InvocationTargetException e) {
            logger.error("调用或发送时有错误发生：", e);
        }
    }
}

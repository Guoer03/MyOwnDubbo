package EasyVersion;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
@Getter
@Setter
public class RpcClient {
    private String host;
    private int port;
    private static final Logger logger= LoggerFactory.getLogger(RpcClient.class);
    public  Object sendRequest(RpcRequest request,String host,int port){
        try(Socket socket = new Socket(host,port)){
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
            /*
            部分outputstream的子类实现了缓存机制，为了提高效率当write()的时候不一定直接发过去，有可能先缓存起来一起发。
            flush()的作用就是强制性地将缓存中的数据发出去。
            */
            return objectInputStream.readObject();
        }catch (Exception e){
            logger.error("调用时有错误发生：",e);
            return null;
        }
    }
}

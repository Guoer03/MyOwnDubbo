package EasyVersion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public Object handle(RpcRequest rpcRequest,Object service) throws InvocationTargetException, IllegalAccessException {
        Object result=null;
        try{
            result=invokeTargetMethod(rpcRequest,service);
            logger.info("服务：{}成功调用方法：{}",rpcRequest.getInterfaceName(),rpcRequest.getMethodName());
        } catch (IllegalAccessException |InvocationTargetException e) {
            logger.error("调用服务或发送时有错误发生：",e);
        }
        return result;
    }
    private Object invokeTargetMethod(RpcRequest rpcRequest,Object service) throws InvocationTargetException, IllegalAccessException {
        Method method = null;
        try {
            method=service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Class<?> clazz=rpcRequest.getParamTypes()[0];
        System.out.println(clazz);
        HelloObject object=(HelloObject) rpcRequest.getParameters()[0];
        System.out.println(object.getMessage());
        return method.invoke(service,rpcRequest.getParameters());
    }
}

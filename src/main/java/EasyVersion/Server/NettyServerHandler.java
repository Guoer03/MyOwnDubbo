package EasyVersion.Server;

import EasyVersion.*;
import EasyVersion.register.DefaultServiceProvider;
import EasyVersion.register.ServiceProvider;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import EasyVersion.Exception.*;
//NettyServerHandler用于接收RpcRequest，并且执行调用，将调用结果返回封装成RpcResponse发送出去
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static RequestHandler requestHandler;
    private static ServiceProvider serviceProvider;

    static{
        requestHandler=new RequestHandler();
        serviceProvider =new DefaultServiceProvider();
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        try{
            logger.info("生产者接收到请求:{}",msg);
            String interfaceName = msg.getInterfaceName();//拿到需要调用的服务名称
            final Object service = serviceProvider.getService(interfaceName);
            final Object result = requestHandler.handle(msg, service);
            final ChannelFuture future = ctx.channel().writeAndFlush(RpcResponse.success(result));logger.info("服务端回送消息："+RpcResponse.success(result).getData()+"?????");
            future.addListener(ChannelFutureListener.CLOSE);
        } catch (RpcException |InvocationTargetException|IllegalAccessException e) {
            e.printStackTrace();
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }
}

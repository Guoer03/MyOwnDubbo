package EasyVersion.Client;

import EasyVersion.RpcRequest;
import EasyVersion.RpcResponse;
import EasyVersion.Serializers.CommonSerializer;
import EasyVersion.Serializers.JsonSerializer;
import EasyVersion.codec.CommonDecoder;
import EasyVersion.codec.CommonEncoder;
import EasyVersion.register.ServiceRegistry;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

public class NettyClient extends CommonDubboClient{
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    public NettyClient(ServiceRegistry serviceRegistry, CommonSerializer serializer) {
        super(serviceRegistry, serializer);
    }


    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        AtomicReference<Object> result=new AtomicReference<>(null);
        try{
            InetSocketAddress inetSocketAddress=serviceRegistry.lookupService(rpcRequest.getInterfaceName());
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);
            if(channel != null) {
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if(future1.isSuccess()) {
                        logger.info(String.format("客户端发送消息: %s", rpcRequest.toString()));
                    } else {
                        logger.error("发送消息时有错误发生: ", future1.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse rpcResponse = channel.attr(key).get();
                return rpcResponse;
            }
        }catch (InterruptedException e){
            logger.error("发送消息时有错误发生",e);
        }
        return null;
    }
}

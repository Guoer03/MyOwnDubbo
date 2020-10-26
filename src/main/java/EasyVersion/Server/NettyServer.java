package EasyVersion.Server;

import EasyVersion.Exception.RpcError;
import EasyVersion.Exception.RpcException;
import EasyVersion.Serializers.CommonSerializer;
import EasyVersion.ShutdownHook;
import EasyVersion.codec.CommonDecoder;
import EasyVersion.codec.CommonEncoder;
import EasyVersion.register.ServiceProvider;
import EasyVersion.register.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class NettyServer extends CommonDubboServer{
    private static final Logger logger=LoggerFactory.getLogger(NettyServer.class);
    public NettyServer(String host,int port,ServiceProvider serviceProvider,ServiceRegistry serviceRegistry,CommonSerializer serializer){
        super(host,port,serviceRegistry,serializer,serviceProvider);
    }
    @Override
    public void start() {
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap=new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .handler(new LoggingHandler(LogLevel.INFO))
                            .option(ChannelOption.SO_BACKLOG,256)
                            .option(ChannelOption.SO_KEEPALIVE,true)
                            .childOption(ChannelOption.TCP_NODELAY,true)
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ChannelPipeline pipeline = ch.pipeline();
                                    pipeline.addLast(new CommonEncoder(serializer));
                                    pipeline.addLast(new CommonDecoder());
                                    pipeline.addLast(new NettyServerHandler());
                                }
                            });
            ChannelFuture future=serverBootstrap.bind(port).sync();
            ShutdownHook.getShutdownHook().addClearAllHook(this);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public <T> CommonDubboServer publishService(Object service, Class<T> serviceClass) throws RpcException {
        if(this.serializer==null){
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        serviceProvider.register(service);
        serviceRegistry.register(serviceClass.getCanonicalName(),new InetSocketAddress(host,port));
        return this;
    }
}

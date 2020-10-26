package EasyVersion.Client;
import EasyVersion.Serializers.CommonSerializer;
import EasyVersion.Serializers.JsonSerializer;
import EasyVersion.codec.CommonDecoder;
import EasyVersion.codec.CommonEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class ChannelProvider {
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    public static Channel get(InetSocketAddress inetSocketAddress, CommonSerializer serializer){
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap boostrap = new Bootstrap();
        boostrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new CommonDecoder())
                                .addLast(new CommonEncoder(serializer))
                                .addLast(new NettyClientHandler());
                    }

                });
        try {
            ChannelFuture future = boostrap.connect(inetSocketAddress.getAddress(), inetSocketAddress.getPort()).sync();
            logger.info("客户端连接到服务器 {}:{}", inetSocketAddress.getAddress(), inetSocketAddress.getPort());
            final Channel channel = future.channel();
            return channel;
        } catch (InterruptedException e) {
            logger.error("连接到服务器发生错误:",e);
        }
        return null;
    }
}

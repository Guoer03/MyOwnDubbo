package EasyVersion.codec;

import EasyVersion.PackageType;
import EasyVersion.RpcRequest;
import EasyVersion.Serializers.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class CommonEncoder extends MessageToByteEncoder {
    private static final int MAGIC_NUMBER=0xCAFEBABE;
    private final CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        out.writeInt(MAGIC_NUMBER);
        if(msg instanceof RpcRequest){
            out.writeInt(PackageType.RPCREQUEST);
        }else{
            out.writeInt(PackageType.RPCRESPONSE);
        }
        out.writeInt(serializer.getCode());
        byte[] bytes=serializer.serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}

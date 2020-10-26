package EasyVersion.codec;

import EasyVersion.PackageType;
import EasyVersion.RpcRequest;
import EasyVersion.RpcResponse;
import EasyVersion.Serializers.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import EasyVersion.Exception.*;

public class CommonDecoder extends ReplayingDecoder {
    private static final Logger logger=LoggerFactory.getLogger(CommonDecoder.class);
    private static final int MAGIC_NUMBER=0xCAFEBABE;
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magic=in.readInt();
        if(magic!=MAGIC_NUMBER){
            logger.error("无法识别的协议包：",magic);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }
        int packageCode=in.readInt();
        Class<?> packageClass;
        if(packageCode== PackageType.RPCREQUEST){
            packageClass= RpcRequest.class;
        }else if(packageCode==PackageType.RPCRESPONSE){
            packageClass= RpcResponse.class;
        }else{
            logger.error("无法识别的数据包",packageCode);
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }
        int serializerCode=in.readInt();
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if(serializer==null){
            logger.error("无法识别的反序列化器");
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }
        int length=in.readInt();
        byte[] bytes=new byte[length];
        in.readBytes(bytes);
        Object obj = serializer.deserialize(bytes, packageClass);
        out.add(obj);
    }
}

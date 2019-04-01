package org.luban.transports.codec;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.luban.rpc.RpcRequest;
import org.luban.rpc.RpcResponse;
import org.luban.transports.protocol.LubanRpcProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * @author liguolin
 * 2019-03-28 17:36:42
 */
public class RpcCodec extends ByteToMessageCodec {

    private Logger logger = LoggerFactory.getLogger(RpcCodec.class);


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {

        logger.info("RpcCodec encode invoke");

        byteBuf.writeByte(o instanceof RpcRequest ? (byte)1:(byte)2);
        byte[] bytes = JSON.toJSONString(o).getBytes();
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List list) throws Exception {

        logger.info("RpcCodec encode decode");

        if(byteBuf.readableBytes() < 1){
            return;
        }
        byteBuf.markReaderIndex();
        LubanRpcProtocol lubanRpcProtocol = new LubanRpcProtocol();

        lubanRpcProtocol.setType(byteBuf.readByte());
        if(byteBuf.readableBytes() < 4){
            byteBuf.resetReaderIndex();
            return;
        }

        lubanRpcProtocol.setLength(byteBuf.readInt());

        if(byteBuf.readableBytes() < lubanRpcProtocol.getLength()){
            byteBuf.resetReaderIndex();
        }

        byte[] contents = new byte[lubanRpcProtocol.getLength()];
        byteBuf.readBytes(contents);

        if(lubanRpcProtocol.getType() == (byte)1){
            RpcRequest request = JSON.parseObject(contents,RpcRequest.class);
            list.add(request);
        }else if(lubanRpcProtocol.getType() == (byte)2){
            RpcResponse response = JSON.parseObject(contents,RpcResponse.class);
            list.add(response);
        }
    }
}

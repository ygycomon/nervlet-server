package com.xunlei.nervlet.connector;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

/**
 * decoder byte array to string
 * @author Davis Yang
 * ÏÂÎç2:55:37
 */
public class NervletDecoder extends OneToOneDecoder {

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        ChannelBuffer data = (ChannelBuffer) msg;
        int size = data.readableBytes();
        byte[] resultBytes = new byte[size];
        data.readBytes(resultBytes);
        String result = new String(resultBytes);
        return result;
    }

}

package com.xunlei.nervlet.connector;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xunlei.nervlet.core.NervletResult;

/**
 * encoder 
 * @author Davis Yang
 * ÏÂÎç6:59:48
 */
public class NervletEncoder extends OneToOneEncoder {

    /* (non-Javadoc)
     * @see org.jboss.netty.handler.codec.oneone.OneToOneEncoder#encode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, java.lang.Object)
     */
    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        if (msg instanceof NervletResult) {
            System.out.println("[INFO] server encode message");
            NervletResult nervletResult = (NervletResult) msg;
            String response = JSON.toJSONString(nervletResult, SerializerFeature.UseSingleQuotes);
            ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer();
            channelBuffer.writeBytes(response.getBytes());
            return channelBuffer;
        }
        return msg;
    }

}

package com.xunlei.nervlet;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

public class TestClient {
    public static void main(String[] args) {
        ClientBootstrap bootstrap = new ClientBootstrap();
        bootstrap.setFactory(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline cp = Channels.pipeline();
                cp.addLast("decoder", new StringDecoder());  
                cp.addLast("encoder", new StringEncoder());  
                cp.addLast("tset", new TestHandler());
                return cp;
            }
        });
        System.out.println("[INFO] client sent msg");
        ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1", 12345));

        future.getChannel().getCloseFuture().awaitUninterruptibly();  
        bootstrap.releaseExternalResources();  
        System.out.println("out");
    }

    static class TestHandler extends SimpleChannelHandler {

        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            e.getChannel().write("{'params':{'articles':[{'name':'test1','path':'test1'},{'name':'test2','path':'test2'},{'name':'test3','path':'test3'},{'name':'test4','path':'test4'}],'code':101},'service':'login'}");
            System.out.println("write complete");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            System.out.println(e.getCause());
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            System.out.println("server response:" + e.getMessage());
        }

    }
    
    static class TestDncoder extends OneToOneDecoder {

        @Override
        protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
            ChannelBuffer buffer = (ChannelBuffer) msg;
            byte[] response = new byte[buffer.readableBytes()];
            buffer.readBytes(response);
            return new String(response);
        }
        
    }
}

package com.xunlei.nervlet.connector;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.xunlei.nervlet.util.ApplicationContext;

/**
 * Connector for server
 * @author Davis Yang
 * ÏÂÎç5:15:33
 */
public class Connector {
    private ApplicationContext context;
    
    private Host host;
    private ServerBootstrap bootstrap;
    private ChannelPipelineFactory pipelineFactory;
    
    public Connector(ApplicationContext context, String host, int port) {
        this.context = context;
        this.host = new Host(host, port);
    }
    
    /**
     * inin server
     */
    public void init() {
        bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        pipelineFactory = new NervletPipelineFactory(context);
        bootstrap.setPipelineFactory(pipelineFactory);
        
        bootstrap.setOption("child.tcpNoDelay", true);  
        bootstrap.setOption("child.keepAlive", true);  
    }
    
    public void bind() {
        System.out.println("[INFO] server start up ");
        bootstrap.bind(new InetSocketAddress(host.getHost(), host.getPort())); 
    }
}

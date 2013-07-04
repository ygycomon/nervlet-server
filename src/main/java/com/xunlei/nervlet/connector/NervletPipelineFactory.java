package com.xunlei.nervlet.connector;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

import com.xunlei.nervlet.util.ApplicationContext;

public class NervletPipelineFactory implements ChannelPipelineFactory{
    private ApplicationContext context;
    private ChannelPipeline nervletPipeline;
    
    public NervletPipelineFactory(ApplicationContext context) {
        this.context = context;
    }
    
    public ChannelPipeline getPipeline() throws Exception {
        this.nervletPipeline = Channels.pipeline();
        nervletPipeline.addLast("decoder", new NervletDecoder());
        nervletPipeline.addLast("encoder", new NervletEncoder());
        nervletPipeline.addLast("nervletHandler", new NervletHandler(context));
        return nervletPipeline;
    }

}

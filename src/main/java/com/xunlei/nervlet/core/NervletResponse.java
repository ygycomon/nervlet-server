package com.xunlei.nervlet.core;

import org.jboss.netty.channel.Channel;

/**
 * response sent to client
 * 
 * @author Davis Yang ÏÂÎç7:59:38
 */
public class NervletResponse {
    private Channel channel;
    private NervletResult result;

    public NervletResponse(Channel channel) {
        this.channel = channel;
        result = new NervletResult();
    }
    
    /**
     * return response to client
     */
    public void response() {
        System.out.println("[INFO] server sent response to client");
        channel.write(result);
    }
    
    /**
     * write response data in
     * @param key
     * @param data
     */
    public void write(String key, Object data) {
        result.putData(key, data);
    }

    /**
     * reset result
     */
    public void reset() {
        result.reset();
    }
    
    public NervletResult getResult() {
        return result;
    }

    public void setResult(NervletResult result) {
        this.result = result;
    }

}

package com.xunlei.nervlet.connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xunlei.nervlet.core.NervletRequest;
import com.xunlei.nervlet.core.NervletResponse;
import com.xunlei.nervlet.core.NervletSession;
import com.xunlei.nervlet.util.ApplicationConstant;
import com.xunlei.nervlet.util.ApplicationContext;

/**
 * handler requests
 * @author Davis Yang
 * ÏÂÎç2:57:25
 */
public class NervletHandler extends SimpleChannelHandler {
    private ApplicationContext context;
    /**
     * cache current active channels
     */
    private Map<Long, Channel> channels;
    /**
     * cache current active sessions
     */
    private Map<Long, NervletSession> sessions;
    
    public NervletHandler(ApplicationContext context) {
        this.context = context;
        
        channels = new HashMap<Long, Channel>();
        sessions = new HashMap<Long, NervletSession>();
    }
    
    /* 
     * once receive msg, start up a thread and notify core to handle that request 
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (e.getMessage() instanceof String) {
            String message = (String) e.getMessage();
            NervletRequest request = parse(message);
            
            System.out.println("[INFO] receive message:" + message);
            
            // cache the channel
            Channel channel = e.getChannel();
            long sessionId = request.getSession().getSessionId();
            channels.put(sessionId, channel);
            ctx.setAttachment(sessionId);
            
            // init a response
            NervletResponse response = new NervletResponse(channel);
            context.getDispatcher().requestDispatcher(request, response);
        }
    }

    /* (non-Javadoc)
     * @see org.jboss.netty.channel.SimpleChannelHandler#channelDisconnected(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
     * 
     * client disconnect, remove the session
     */
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelDisconnected(ctx, e);
        
        release((Long) ctx.getAttachment());
    }

    /**
     * once a channel has disconnected, release the resource
     * @param sessionId
     */
    public void release(long sessionId) {
        channels.remove(sessionId);
        sessions.remove(sessionId);
    }
    
    /**
     * parse a string message to nervlet request
     * @param message
     */
    public NervletRequest parse(String message) {
        // get request's params
        JSONObject result = (JSONObject) JSON.parse(message);
        Map<String, Object> requestParams = (Map<String, Object>) parse(result.get("params"));
        NervletRequest request = new NervletRequest(result.getString("service"));
        request.setParam(requestParams);
        
        // get request's session
        NervletSession session = null;
        Object sessionId = requestParams.get(ApplicationConstant.REQUEST_SESSION_ID);
        if (sessionId != null) {
            session = sessions.get(sessionId);
        }
        
        if (session == null) {
            // current session is a new one or has expired
            session = new NervletSession();
        } 
        
        session.init();
        request.setSession(session);
        sessions.put(session.getSessionId(), session);
        
        return request;
    }
    
    /**
     * recursion parse json object into a map
     * @param obj
     * @return
     */
    public Object parse(Object obj) {
        if (obj instanceof JSONObject) {
            JSONObject target = (JSONObject) obj;
            Map<String, Object> result = new HashMap<String, Object>();
            for (Entry<String, Object> entry : target.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                
                result.put(key, parse(value));
            }
            return result;
        } else if (obj instanceof JSONArray) {
            JSONArray target = (JSONArray) obj;
            List<Object> result = new ArrayList<Object>();
            Iterator<Object> iterator = target.iterator();
            
            while (iterator.hasNext()) {
                result.add(parse(iterator.next()));
            }
            return result;
        } else {
            return obj.toString();
        }
    }
}

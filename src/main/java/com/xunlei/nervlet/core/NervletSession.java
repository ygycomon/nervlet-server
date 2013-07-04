package com.xunlei.nervlet.core;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * session of a request
 * @author Davis Yang
 * ÏÂÎç7:37:24
 */
public class NervletSession {
    private static AtomicLong sessionIdGenerator = new AtomicLong(1);
    
    private long sessionId;
    private Map<String, Object> data;
    
    /**
     * the time that current session last visit 
     */
    private long lastActiveTime;
    
    public NervletSession() {
        data = new HashMap<String, Object>();
        sessionId = sessionIdGenerator.incrementAndGet();
        
        init();
    }
    
    /**
     * init session:
     * reset active time
     */
    public void init() {
        lastActiveTime = new Date().getTime();
    }
    
    /**
     * get data
     * @param key
     * @return data
     */
    public Object getData(String key) {
        return data.get(key);
    }
    
    /**
     * set data
     * @param key
     * @param value
     */
    public void setData(String key, String value) {
        data.put(key, value);
    }
    
    /**
     * get current session id
     * @return
     */
    public long getSessionId() {
        return sessionId;
    }
}


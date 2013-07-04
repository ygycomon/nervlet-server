package com.xunlei.nervlet.core;

import java.util.Map;

/**
 * request from client
 * @author Davis Yang
 * ÏÂÎç7:58:29
 */
public class NervletRequest {
    private NervletSession session;
    private Map<String, Object> params;
    /**
     * request target
     */
    private String service; 
    
    public NervletRequest(String service) {
        this.service = service;
    }
    
    /**
     * get target service
     * @return service
     */
    public String getService() {
        return this.service;
    }
    
    /**
     * set params
     * @param params
     */
    public void setParam(Map<String, Object> params) {
        this.params = params;
    }
    
    /**
     * @param key
     * @return request param
     */
    public Object getParam(String key) {
        return params.get(key);
    }
    
    public Map<String, Object> getParams() {
        return params;
    }
    
    /**
     * get session
     * @return session of current request
     */
    public NervletSession getSession() {
        return session;
    }
    
    /**
     * set session
     * @param session
     */
    public void setSession(NervletSession session) {
        this.session = session;
    }
}

package com.xunlei.nervlet.core;

import java.util.HashMap;
import java.util.Map;

/**
 * represents result of one serve
 * @author Davis Yang
 * ÉÏÎç10:26:36
 */
public class NervletResult {
    private Map<String, Object> data;
    
    public NervletResult() {
        data = new HashMap<String, Object>();
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    /**
     * insert value into data
     * @param key
     * @param value
     */
    public void putData(String key, Object value) {
        data.put(key, value);
    }
    
    /**
     * clear the data result
     */
    public void reset() {
        data.clear();
    }
}

package com.xunlei.nervlet.core;

import com.xunlei.nervlet.util.ApplicationConstant;

/**
 * base class of nervlet order service
 * @author Davis Yang
 * ÉÏÎç11:21:59
 */
public abstract class Nervlet {
    /**
     * @param request
     * @param response
     */
    public void service(NervletRequest request, NervletResponse response) {
        try {
            doService(request, response);
        } catch (Exception e) {
            response.reset();
            response.write(ApplicationConstant.RESPONSE_CODE, ApplicationConstant.RESPONSE_SERVER_ERROR);
        }
    }
    
    /**
     * template of nervlet
     * @param request
     * @param response
     */
    public abstract void doService(NervletRequest request, NervletResponse response);
}

package com.xunlei.nervlet.core;

public abstract class NervletFilter {
    public abstract boolean doFilter(NervletRequest request, NervletResponse response);
}

package com.xunlei.nervlet;

import com.xunlei.nervlet.core.Dispatcher;

public class TestCore {
    public static void main(String[] args) {
        Dispatcher dispatcher = new Dispatcher(null);
        dispatcher.init();
        dispatcher.requestDispatcher(null, null);
    }
}

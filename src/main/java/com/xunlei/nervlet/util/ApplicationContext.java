package com.xunlei.nervlet.util;

import com.xunlei.nervlet.connector.Connector;
import com.xunlei.nervlet.core.Dispatcher;

/**
 * the whole context
 * 
 * @author Davis Yang ÏÂÎç6:26:11
 */
public class ApplicationContext {
    private int port;
    private String name;
    private String docBase;

    private Connector connector;
    private Dispatcher dispatcher;

    public ApplicationContext(int port, String name, String docBase) {
        this.port = port;
        this.name = name;
        this.docBase = docBase;
    }

    /**
     * start up the context
     */
    public void bootstrap() {
        System.out.println("[INFO] bootstrap context: " + name);
        Connector connector = new Connector(this, "127.0.0.1", port);
        Dispatcher dispatcher = new Dispatcher(this);

        setConnector(connector);
        setDispatcher(dispatcher);

        connector.init();
        connector.bind();
        dispatcher.init();
    }

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public String getDocBase() {
        return docBase;
    }

}

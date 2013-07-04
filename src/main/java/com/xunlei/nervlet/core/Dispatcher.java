package com.xunlei.nervlet.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.xunlei.nervlet.util.ApplicationContext;

/**
 * dispatch request to core and sent response back to client
 * @author Davis Yang
 * ÏÂÎç6:40:56
 */
public class Dispatcher {
    private ApplicationContext context;
    
    private Map<String, String> nervletConfig;
    private Map<String, Nervlet> nervlets;
    private Map<String, String> filterConfig;
    private Map<String, NervletFilter> filters;
    
    public Dispatcher(ApplicationContext context) {
        this.context = context;
    }
    
    /**
     * init dispatcher
     * 1. read config file to get name-nervlet map
     */
    public void init() {
        nervletConfig = new HashMap<String, String>();
        nervlets = new HashMap<String, Nervlet>();
        filterConfig = new HashMap<String, String>();
        filters = new HashMap<String, NervletFilter>();
        
        loadServiceConfig();
    }
    
    private void loadServiceConfig() {
        System.out.println("[INFO] load services");
        String path = context.getDocBase();
        
        // add classpath dynamicly
        File file = new File(path + "/bin/"); 
        
        URLClassLoader classloader = (URLClassLoader) ClassLoader.getSystemClassLoader(); 
        Method add;
        try {
            add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
            add.setAccessible(true); 
            add.invoke(classloader, new Object[] { file.toURI().toURL() }); 
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } 
        
        try {
            InputStream in = new FileInputStream(path + "/conf/service.xml");
            
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(in);
            
            Element root = document.getRootElement();
            
            for (Iterator ie = root.elementIterator(); ie.hasNext();) {
                Element element = (Element) ie.next();

                String elementName = element.getName();
                if (elementName.equals("nervlet")) {
                    String service = element.attribute("service").getValue();
                    String clazz = element.attribute("class").getValue();
                    nervletConfig.put(service, clazz);
                    nervlets.put(service, (Nervlet) Class.forName(clazz).newInstance());
                }
                
                if (elementName.equals("filter")) {
                    String name = element.attribute("name").getValue();
                    String pattern = element.attribute("pattern").getValue();
                    String clazz = element.attribute("class").getValue();
                    
                    filterConfig.put(pattern, name);
                    filters.put(name, (NervletFilter) Class.forName(clazz).newInstance());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void requestDispatcher(NervletRequest request, NervletResponse response) {
        System.out.println("[INFO] dispatcher request to nervlet, do service");
        String service = request.getService();

        Nervlet target = nervlets.get(service);
        target.service(request, response);
        response.response();
    }
    
}

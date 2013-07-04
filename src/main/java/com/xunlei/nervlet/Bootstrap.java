package com.xunlei.nervlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.xunlei.nervlet.util.ApplicationContext;

/**
 * @author Davis Yang
 * ÏÂÎç4:05:27
 */
public class Bootstrap {
    private List<ApplicationContext> applicationContexts = new ArrayList<ApplicationContext>();
    
    /**
     * load server config, get every context and init
     */
    public void loadConfig() {
        try {
            InputStream in = new FileInputStream("conf/server.xml");
            
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(in);
            
            Element root = document.getRootElement();
            
            for (Iterator ie = root.elementIterator(); ie.hasNext();) {
                Element element = (Element) ie.next();

                System.out.println("[INFO] load context:" + element.attribute("name").getValue());
                ApplicationContext context = new ApplicationContext(Integer.valueOf(element.attribute("port").getValue()), element.attribute("name").getValue(), element.attribute("docBase").getValue());
                applicationContexts.add(context);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * bootstrap
     */
    public void bootstrap() {
        loadConfig();
        for (ApplicationContext context : applicationContexts) {
            context.bootstrap();
        }
    }
    
    public static void main(String[] args) {
        new Bootstrap().bootstrap();
    }
}

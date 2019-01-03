package com;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

public class LogServer {
    public synchronized static Logger getLog(Class clazz) {
        PropertyConfigurator.configure("propertyInfo\\log4j.properties");
        BasicConfigurator.configure();
        return Logger.getLogger(clazz);
    }

    public synchronized static void logInfo(Class clazz, Object obj) {
        getLog(clazz).info(obj.toString());
    }

    public synchronized static void logError(Class clazz, Object obj) {
        getLog(clazz).error(obj.toString());
    }

}

package com.platform.iot.api.monitoring;

import com.platform.iot.api.exception.ApplicationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.EnumSet;

/**
 */
public class MonitoringServiceLocator {

    private ApplicationContext applicationContext;

    private static MonitoringServiceLocator INSTANCE;

    public enum DynamicBeanName {powerUpMonitor, tableMonitor}

    private MonitoringServiceLocator() {
        this.applicationContext = new ClassPathXmlApplicationContext("monitoring-config.xml");
    }

    public static MonitoringServiceLocator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MonitoringServiceLocator();
        }
        return INSTANCE;
    }

    public void monitorApplicationException(String userToken, ApplicationException applicationException) {
        EnumSet<DynamicBeanName> beanNames = EnumSet.allOf(DynamicBeanName.class);
        AbstractMonitor monitor;
        for (DynamicBeanName beanName : beanNames) {
            monitor = (AbstractMonitor) applicationContext.getBean(beanName.name());
            if (monitor.getExceptions() != null && monitor.getExceptions().contains(applicationException.getType())) {
                monitor.monitorException(userToken, applicationException);
            }
        }
    }

    public ServerMonitor getServerMonitor() {
        try {
            return (ServerMonitor) applicationContext.getBean("serverMonitor");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Object getBean(String key) {
        return applicationContext.getBean(key);
    }
}

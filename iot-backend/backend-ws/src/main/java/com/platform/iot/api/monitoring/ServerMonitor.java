package com.platform.iot.api.monitoring;

import com.platform.iot.api.balancing.Server;
import com.platform.iot.api.bussiness.MemoryStorage;
import com.platform.iot.api.exception.ApplicationException;
import com.platform.iot.api.exception.ExceptionType;
import org.springframework.jmx.export.MBeanExporter;

import javax.management.*;
import javax.naming.OperationNotSupportedException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 */
public class ServerMonitor extends AbstractMonitor {

    public static final String SERVER_ADDRESS = "serverAddress";
    public static final String SERVER_PORT = "serverPort";
    public static final String SERVER_ACTIVE_USERS = "activeUsersNumber";
    public static final String SERVER_INACTIVE_USERS = "inactiveUsersNumber";
    public static final String SERVER_PRODUCERS = "producersNumber";
    public static final String SERVER_FREEZED = "freezed";
    private static Map<String, Map<String, String>> errorsMap = new HashMap<String, Map<String, String>>();
    private static Map<String, Map<String, String>> warningsMap = new HashMap<String, Map<String, String>>();
    private static EnumSet<ExceptionType> exceptions;

    private Server server;

    private static Map<String, String> attributesMap = new HashMap<String, String>();

    public ServerMonitor() throws MBeanException {
        super(attributesMap);
        errorsMap.put(ERRORS, new HashMap<String, String>());
        warningsMap.put(WARNINGS, new HashMap<String, String>());
        exceptions = EnumSet.allOf(ExceptionType.class);
    }

    public void initMonitor() {
        attributesMap.put(SERVER_ADDRESS, String.valueOf(server.getAddress()));
        attributesMap.put(SERVER_PORT, String.valueOf(server.getPort()));
        attributesMap.put(SERVER_ACTIVE_USERS, "0");
        attributesMap.put(SERVER_INACTIVE_USERS, String.valueOf(MemoryStorage.INSTANCE.getInactiveUsers().size()));
        attributesMap.put(SERVER_PRODUCERS, String.valueOf(MemoryStorage.INSTANCE.getProducers().size()));
        if (attributesMap.get(SERVER_FREEZED) == null) {
            attributesMap.put(SERVER_FREEZED, "NO");
        }
    }

    public void updateMonitor() {
        try {

            attributesMap.put(SERVER_ADDRESS, String.valueOf(server.getAddress()));
            attributesMap.put(SERVER_PORT, String.valueOf(server.getPort()));
            attributesMap.put(SERVER_ACTIVE_USERS, String.valueOf(MemoryStorage.INSTANCE.getActiveUsers().size()));
            attributesMap.put(SERVER_INACTIVE_USERS, String.valueOf(MemoryStorage.INSTANCE.getInactiveUsers().size()));
            if (attributesMap.get(SERVER_FREEZED) == null) {
                attributesMap.put(SERVER_FREEZED, "NO");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProducersNumber(){
        attributesMap.put(SERVER_PRODUCERS, String.valueOf(MemoryStorage.INSTANCE.getProducers().size()));
    }

    public void updateFreezed(String value){
        attributesMap.put(SERVER_FREEZED, value);
    }

    public Object getValue(String key) {
        return attributesMap.get(key);
    }

    public Set getKeys() {
        return attributesMap.keySet();
    }

    public void setServerIsFreezed() {
        attributesMap.put(SERVER_FREEZED, "YES");
    }

    @Override
    public EnumSet<ExceptionType> getExceptions() {
        return null;
    }

    @Override
    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        throw new MBeanException(new OperationNotSupportedException("Not supported"));
    }


    public static ServerMonitor createAndRegister(Server server) {
        MonitoringServiceLocator serviceLocator = MonitoringServiceLocator.getInstance();
        MBeanExporter mBeanExporter = (MBeanExporter) serviceLocator.getBean("simpleJmxExporter");
        String mbeanName = "Local:type=Monitoring,subtype=Servers,name=Server" + server.getAddress() + "-" + server.getPort();
        ObjectName objectName = null;
        try {
            objectName = new ObjectName(mbeanName);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        ServerMonitor serverMonitor = null;
        serverMonitor = serviceLocator.getServerMonitor();
        serverMonitor.setServer(server);
        mBeanExporter.registerManagedResource(serverMonitor, objectName);
        return serverMonitor;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void updateVariationsCount() {
        super.incVariationsCount(attributesMap);
    }

    @Override
    public void monitorException(String userToken, ApplicationException exception) {
        switch (exception.getType().getSeverity()) {
            case ERROR:
                Map<String, String> values = errorsMap.get(ERRORS);
                values.put(userToken, exception.getMessage());
                errorsMap.put(ERRORS, values);
                super.updateLastError(attributesMap, exception.toString());
                super.incErrorsCount(attributesMap);
                break;
            case WARNING:
                values = warningsMap.get(WARNINGS);
                values.put(userToken, exception.getMessage());
                warningsMap.put(WARNINGS, values);
                super.incWarningsCount(attributesMap);
                break;
            case INFO:
                break;
        }
    }
}

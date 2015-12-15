package com.platform.iot.api.monitoring;

import com.platform.iot.api.exception.ApplicationException;
import com.platform.iot.api.exception.ExceptionType;
import org.springframework.context.annotation.ImportResource;

import javax.management.*;
import javax.naming.OperationNotSupportedException;
import java.text.AttributedCharacterIterator;
import java.util.*;

/**
 * Created by Magda Gherasim
 */
@ImportResource("classpath:monitoring-config.xml")
public abstract class AbstractMonitor implements DynamicMBean {

//    private Map<String, String> attributesMap;

    public static final String TOPIC_VARIATION_COUNT = "variationsCount";
    public static final String ERRORS_COUNT = "errorsCount";
    public static final String WARNINGS_COUNT = "warningsCount";
    public static final String AVERAGE_RESPONSE_TIME = "averageResponseTime";
    public static final String LAST_ERROR = "lastError";

    public static final String ERRORS = "errors";
    public static final String WARNINGS = "warnings";

    public AbstractMonitor() {
    }

    public AbstractMonitor(Map<String, String> attributesMap)
            throws MBeanException, RuntimeOperationsException {
        super();
//        this.attributesMap = attributesMap;
        attributesMap.put(TOPIC_VARIATION_COUNT, "0");
        attributesMap.put(ERRORS_COUNT, "0");
        attributesMap.put(AVERAGE_RESPONSE_TIME, "0");
        attributesMap.put(WARNINGS_COUNT, "0");
        attributesMap.put(LAST_ERROR, "none");
    }

    public void incVariationsCount(Map<String, String> attributesMap) {
        int value = Integer.parseInt(attributesMap.get(TOPIC_VARIATION_COUNT));
        attributesMap.put(TOPIC_VARIATION_COUNT, String.valueOf(value + 1));
    }

    public void incErrorsCount(Map<String, String> attributesMap) {
        int value = Integer.parseInt(attributesMap.get(ERRORS_COUNT));
        attributesMap.put(ERRORS_COUNT, String.valueOf(value + 1));
    }

    public void incWarningsCount(Map<String, String> attributesMap) {
        int value = Integer.parseInt(attributesMap.get(WARNINGS_COUNT));
        attributesMap.put(WARNINGS_COUNT, String.valueOf(value + 1));
    }

    public void updateLastError(Map<String, String> attributesMap, String error) {
        attributesMap.put(LAST_ERROR, error);
    }

    public void updateAverageResponseTime(Map<String, String> attributesMap, double newResponseTime) {
        double value = Double.parseDouble(attributesMap.get(AVERAGE_RESPONSE_TIME));
        attributesMap.put(AVERAGE_RESPONSE_TIME, String.valueOf(calculateAverage(value, Integer.parseInt(attributesMap.get(TOPIC_VARIATION_COUNT)), newResponseTime)));
    }

//    public Map<String, String> getAttributesMap() {
//        return attributesMap;
//    }
//
//    public void setAttributesMap(Map<String, String> attributesMap) {
//        this.attributesMap = attributesMap;
//    }

    public abstract EnumSet<ExceptionType> getExceptions();

    public void monitorException(String playerToken, ApplicationException exception) {
    }

    public abstract Object getValue(String key);

    public abstract Set getKeys();

    public Object getAttribute(String name)
            throws AttributeNotFoundException {
        Object counter = getValue(name);
        if (counter != null)
            return counter;
        else
            throw new AttributeNotFoundException("No such property: " + name);
    }

    public void setAttribute(AttributedCharacterIterator.Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        throw new MBeanException(new OperationNotSupportedException("Not supported"));
    }

    public AttributeList getAttributes(String[] names) {
        AttributeList list = new AttributeList();
        for (String name : names) {
            Object counter = getValue(name);
            if (counter != null)
                list.add(new Attribute(name, counter));
        }
        return list;
    }

    public AttributeList setAttributes(AttributeList attributes) {
        throw new RuntimeException("Not supported!");
    }

    public Object invoke(String actionName, Object params[], String signature[]) throws MBeanException, ReflectionException {
        throw new MBeanException(new OperationNotSupportedException("Not supported"));
    }

    public MBeanInfo getMBeanInfo() {
        try {
            SortedSet<String> names = new TreeSet<String>();
            for (Object name : getKeys()) {
                names.add((String) name);
            }
            MBeanAttributeInfo[] attrs = new MBeanAttributeInfo[names.size()];
            Iterator<String> it = names.iterator();

            for (int i = 0; i < attrs.length; i++) {
                Object attrValue = it.next();
                if (attrValue instanceof String) {
                    attrs[i] = new MBeanAttributeInfo(
                            (String) attrValue,
                            "java.lang.String",
                            "Property " + (String) attrValue,
                            true,   // isReadable
                            false,   // isWritable
                            false); // isIs
                } else {
                    attrs[i] = new MBeanAttributeInfo(
                            "to be added",
                            "java.lang.String",
                            "Property " + "to be added",
                            true,   // isReadable
                            false,   // isWritable
                            false); // isIs
                }
            }


            return new MBeanInfo(
                    this.getClass().getName(),
                    this.getClass().getName(),
                    attrs,
                    null,  // constructors
                    null,  // operations
                    null); // notifications
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public static double calculateAverage(double oldAverage, int nrOfTerms, double newTerm) {
        if (nrOfTerms == 1) {
            return newTerm;
        } else {
            double oldSumOfTerms = (nrOfTerms - 1) * oldAverage;
            return (oldSumOfTerms + newTerm) / (nrOfTerms);
        }
    }


//    public synchronized MBeanInfo getMBeanInfo() {
//        SortedSet<String> names = new TreeSet<String>();
//        for (Object name : getKeys()) {
//            names.add((String) name);
//        }
//        MBeanAttributeInfo[] attrs = new MBeanAttributeInfo[names.size()];
//        Iterator<String> it = names.iterator();
//        for (int i = 0; i < attrs.length; i++) {
//            String name = it.next();
//
//            attrs[i] = new MBeanAttributeInfo(
//                    name,
//                    "java.lang.String",
//                    "Property " + name,
//                    true,   // isReadable
//                    false,   // isWritable
//                    false); // isIs
//        }
//
//        return new MBeanInfo(
//                this.getClass().getName(),
//                this.getClass().getName(),
//                attrs,
//                null,  // constructors
//                null,  // operations
//                null); // notifications
//    }

}

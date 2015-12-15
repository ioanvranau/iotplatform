package com.platform.iot.api.bussiness.model;

import javax.persistence.*;

@Entity
@javax.persistence.Table(name = "devices")
public class Device {

    public static final String FIELD_ID = "id";
    public static final String FIELD_PARAM1 = "param1";
    public static final String FIELD_PARAM2 = "param2";
    public static final String FIELD_PARAM3 = "param3";
    public static final String FIELD_PARAM4 = "param4";
    public static final String FIELD_PARAM5 = "param5";
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "param1", length = 64, nullable = true)
    private String param1;
    @Column(name = "param2", length = 64, nullable = true)
    private String param2;
    @Column(name = "param3", length = 64, nullable = true)
    private String param3;
    @Column(name = "param4", length = 64, nullable = true)
    private String param4;
    @Column(name = "param5", length = 64, nullable = true)
    private String param5;

    public Device() {
    }

    public Device(String param1, String param2, String param3, String param4, String param5) {
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
        this.param5 = param5;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getParam4() {
        return param4;
    }

    public void setParam4(String param4) {
        this.param4 = param4;
    }

    public String getParam5() {
        return param5;
    }

    public void setParam5(String param5) {
        this.param5 = param5;
    }

    @Override
    public String toString() {
        return "Device{" +
                "param1='" + param1 + '\'' +
                ", param2='" + param2 + '\'' +
                ", param3='" + param3 + '\'' +
                ", param4='" + param4 + '\'' +
                ", param5='" + param5 + '\'' +
                '}';
    }
}

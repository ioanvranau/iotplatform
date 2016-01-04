package com.platform.iot.domain;

/**
 * Created by ioan.vranau on 12/15/2015.
 */

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {

    private String firstName;
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

package com.platform.iot.api.bussiness.model;

import javax.persistence.*;

/**
 * Created by Magda Gherasim
 */
@Entity
@javax.persistence.Table(name = "producers")
public class Producer {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", length = 60, nullable = false)
    private String name;

    public Producer() {
    }

    public Producer(String producerName) {
        this.name =  producerName;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

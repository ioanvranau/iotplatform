package com.platform.iot.api.storage;

import com.platform.iot.api.bussiness.model.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Magda on 6/1/2014.
 *
 */
public interface ProducerRepository extends JpaRepository<Producer, Long> {

    @Query("SELECT p FROM Producer p WHERE p.name= :name")
    public Producer findByName(@Param("name") String name);

}

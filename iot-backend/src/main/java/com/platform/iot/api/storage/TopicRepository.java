package com.platform.iot.api.storage;

import com.platform.iot.api.bussiness.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Magda on 6/1/2014.
 *
 */
public interface TopicRepository extends JpaRepository<Topic, Long> {

    @Query("SELECT p FROM Topic p WHERE p.producerId= :producerId AND p.code= :code")
    public Topic findByProducerIdAndCode(@Param("producerId") long producerId, @Param("code") String code);

}

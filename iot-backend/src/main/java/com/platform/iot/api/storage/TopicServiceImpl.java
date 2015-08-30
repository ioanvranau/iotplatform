package com.platform.iot.api.storage;

import com.platform.iot.api.bussiness.model.Topic;
import com.platform.iot.api.exception.ApplicationException;
import com.platform.iot.api.exception.ExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Magda on 6/1/2014.
 *
 */
@Service
public class TopicServiceImpl implements TopicService {
    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private TopicRepository topicRepository;

    @Override
    @Transactional
    public Topic create(Topic topic) {
        return topicRepository.save(topic);
    }

    @Override
    @Transactional
    public Topic findById(long id) {
        return topicRepository.findOne(id);
    }

    @Override
    public Topic findByProducerIdAndCode(long producerId, String code) {
        return topicRepository.findByProducerIdAndCode(producerId, code);
    }



    @Override
    @Transactional(rollbackFor = ApplicationException.class)
    public Topic delete(long id) throws ApplicationException {
        Topic deletedTopic = topicRepository.findOne(id);
        if (deletedTopic == null)
            throw new ApplicationException(ExceptionType.TOPIC_NOT_FOUND);
        topicRepository.delete(deletedTopic);
        return deletedTopic;
    }

    @Override
    @Transactional
    public List<Topic> findAll() {
        if (topicRepository != null) {
            return topicRepository.findAll();
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Topic update(Topic topic) {
        Topic updatedTopic = topicRepository.findOne(topic.getId());

        if (updatedTopic == null)
            throw new RuntimeException("No user found for id " + topic.getId());

        updatedTopic.setCode(topic.getCode());
        updatedTopic.setName(topic.getName());
        updatedTopic.setDefaultSubscribed(topic.isDefaultSubscribed());
        updatedTopic.setPrice(topic.getPrice());
        updatedTopic.setProducerId(topic.getProducerId());
        return updatedTopic;
    }
}

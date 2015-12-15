package com.platform.iot.api.storage;

import com.platform.iot.api.bussiness.model.Producer;
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
public class ProducerServiceImpl implements ProducerService {
    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private ProducerRepository producerRepository;

//    private EntityManagerFactory emf;
//
//    @PersistenceUnit
//    public void setEntityManagerFactory(EntityManagerFactory emf) {
//        this.emf = emf;
//    }

    @Override
    @Transactional
    public Producer create(Producer user) {
        return producerRepository.save(user);
    }

    @Override
    @Transactional
    public Producer findById(long id) {
        return producerRepository.findOne(id);
    }

    @Override
    @Transactional
    public Producer findByName(String name) {
        return producerRepository.findByName(name);
    }

    @Override
    @Transactional(rollbackFor = ApplicationException.class)
    public Producer delete(long id) throws ApplicationException {
        Producer deletedUser = producerRepository.findOne(id);
        if (deletedUser == null)
            throw new ApplicationException(ExceptionType.PRODUCER_NOT_FOUND);
        producerRepository.delete(deletedUser);
        return deletedUser;
    }

    @Override
    @Transactional
    public List<Producer> findAll() {
        if (producerRepository != null) {
            return producerRepository.findAll();
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Producer update(Producer producer) {
        Producer updatedProducer = producerRepository.findOne(producer.getId());

        if (updatedProducer == null)
            throw new RuntimeException("No user found for id " + producer.getId());

        updatedProducer.setName(producer.getName());
        return updatedProducer;
    }
}

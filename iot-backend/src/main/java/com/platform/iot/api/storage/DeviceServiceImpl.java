package com.platform.iot.api.storage;

import com.platform.iot.api.bussiness.model.Device;
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
public class DeviceServiceImpl implements DeviceService {
    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private DeviceRepository deviceRepository;

//    private EntityManagerFactory emf;
//
//    @PersistenceUnit
//    public void setEntityManagerFactory(EntityManagerFactory emf) {
//        this.emf = emf;
//    }

    @Override
    @Transactional
    public Device create(Device device) {

        return deviceRepository.save(device);
    }

    @Override
    @Transactional
    public Device findById(long id) {
        return deviceRepository.findOne(id);
    }


    @Override
    @Transactional(rollbackFor = ApplicationException.class)
    public Device delete(long id) throws ApplicationException {
        Device deletedUser = deviceRepository.findOne(id);
        if (deletedUser == null)
            throw new ApplicationException(ExceptionType.USER_NOT_FOUND);
        deviceRepository.delete(deletedUser);
        return deletedUser;
    }

    @Override
    @Transactional
    public List<Device> findAll() {
        if (deviceRepository != null) {
            return deviceRepository.findAll();
        }
        return null;
    }

 }

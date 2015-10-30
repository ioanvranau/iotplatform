package com.platform.data;

import com.platform.domain.*;

import java.util.Collection;
import java.util.Date;

/**
 * QuickTickets Dashboard backend API.
 */
public interface DataProvider {

    /**
     * @param userName
     * @param password
     * @return Authenticated used.
     */
    User authenticate(String userName, String password);

    /**
     * @return The number of unread notifications for the current user.
     */
    int getUnreadNotificationsCount();

    /**
     * @return Notifications for the current user.
     */
    Collection<DashboardNotification> getNotifications();

    void addNotification();


    Collection<Device> getDevices();

    public boolean addDevice(Device device);

}

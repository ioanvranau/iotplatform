package com.platform.view.utils;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

/**
 * Created by vranau on 10/27/2015.
 */
public class UiUtils {

    public static void displayNotification(String title, String text) {
        Notification notification = new Notification(title);

        notification
                .setDescription(text);
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("tray dark small closable login-help");
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(4000);
        notification.show(Page.getCurrent());
    }
}

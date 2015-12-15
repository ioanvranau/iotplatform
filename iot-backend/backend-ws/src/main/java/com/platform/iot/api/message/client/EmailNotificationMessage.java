package com.platform.iot.api.message.client;

import net.sf.json.JSONObject;

public class EmailNotificationMessage extends ClientMessage {
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_NOTIFICATION_TYPE = "notificationType";

    public enum NotificationType {
        SECURITY_EMAILS, SYSTEM_EMAILS

    }

    private NotificationType notificationType;
    private String email;


    public EmailNotificationMessage(JSONObject jsonObject) {
        try {
            create(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create(JSONObject object) throws Exception {
        this.setType(MESSAGE.EMAIL_NOTIFICATION);
        if (org.apache.commons.lang.StringUtils.isNotBlank(object.optString(FIELD_NOTIFICATION_TYPE))) {
            this.notificationType = NotificationType.valueOf(object.optString(FIELD_NOTIFICATION_TYPE));
        } else {
            System.out.println("Please fill in a notification type");
        }
        this.email = object.optString(FIELD_EMAIL);
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

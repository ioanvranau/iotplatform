package com.platform.iot.api.message.client;

import net.sf.json.JSONObject;

/**
 * Created by magdalena.gherasim on 6/23/2014.
 */
public class MigrationMessage extends RegisterMessage {

    public static final String FIELD_TOKEN = "token";

    public MigrationMessage(JSONObject object) {
      super(object);
    }
}


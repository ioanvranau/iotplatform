package backup.message.client;


import net.sf.json.JSONObject;

/**
 * Created by Magda Gherasim
 */
public class StartListeningMessage extends ClientMessage{
    public StartListeningMessage(JSONObject object) {
        setType(MESSAGE.START_LISTENING);
        if (object.optString("token") != null){
            setToken(object.optString("token"));
        }

    }
  }

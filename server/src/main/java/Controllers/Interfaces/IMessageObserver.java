package Controllers.Interfaces;

import org.json.JSONObject;

public interface IMessageObserver {
    void onMessageReceived(JSONObject messageJson);
}
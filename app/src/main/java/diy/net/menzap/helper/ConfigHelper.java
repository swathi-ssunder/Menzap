package diy.net.menzap.helper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by swathissunder on 27/09/16.
 */

public class ConfigHelper {
    private JSONObject trackingMap;

    public ConfigHelper() {
        trackingMap = new JSONObject();
        try {
            trackingMap.put("https://p11.cm.in.tum.de", "Location1");
            trackingMap.put("https://p12.cm.in.tum.de", "Location2");
            trackingMap.put("https://p13.cm.in.tum.de", "Location3");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getTrackingMap() {
        return trackingMap;
    }
}
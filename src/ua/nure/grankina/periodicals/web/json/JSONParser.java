package ua.nure.grankina.periodicals.web.json;

import org.apache.log4j.Logger;
import org.json.*;
import ua.nure.grankina.periodicals.web.captcha.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER-PC on 16.01.2017.
 */
public class JSONParser {
    private JSONObject obj;
    private static Logger log = Logger.getLogger("captcha");

    public JSONParser(String json){
        obj = new JSONObject(json);
    }

    public boolean getResult(){
        return obj.getBoolean(Constants.JSON_SUCCESS);
    }

    public String getString(String key){
        return obj.getString(key);
    }

    public List<String> getArray(String key){
        List<String> list = new ArrayList<>();
        JSONArray arr = obj.getJSONArray(key);
        for (int i = 0; i < arr.length(); i++){
            log.debug("adding error --> " + arr.getString(i));
            list.add(arr.getString(i));
        }
        return list;
    }

    public long getLong(String key){
        return obj.getLong(key);
    }

}

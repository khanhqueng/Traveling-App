package com.example.uddd.Parser;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {
    private HashMap<String,String> parseJsonObject(JSONObject object){
        HashMap<String,String> datalist= new HashMap<>();
        try {
            String name= object.getString("name");
            String latitude= object.getJSONObject("geometry").getString("lat");
            String longitude= object.getJSONObject("geometry").getString("lng");
            datalist.put("name",name);
            datalist.put("lat",latitude);
            datalist.put("lng",longitude);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return  datalist;

    }
    private List<HashMap<String,String>> parseJsonArray(JSONArray array){
        List<HashMap<String,String>> datalist = new ArrayList<>();
        for(int i=0;i<array.length();i++){
            try {
                HashMap<String,String> data= parseJsonObject((JSONObject) array.get(i));
                datalist.add(data);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return datalist;

    }
    public List<HashMap<String,String>> parseResult(JSONObject object){
        JSONArray jsonArray= null;
        try {
            jsonArray= object.getJSONArray("results");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return parseJsonArray(jsonArray);

    }
}

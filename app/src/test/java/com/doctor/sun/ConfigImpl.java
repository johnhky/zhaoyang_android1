package com.doctor.sun;

import java.util.HashMap;

/**
 * Created by rick on 21/6/2016.
 */
public class ConfigImpl implements Config {
    private HashMap<String, String> strings = new HashMap<>();

    private static ConfigImpl instance;

    public static ConfigImpl getInstance() {
        if (instance == null) {
            instance = new ConfigImpl();
        }
        return instance;
    }

    public void setToken(String token) {
        strings.put("token", token);
    }

    @Override
    public String getToken() {
        return strings.get("token");
    }
}

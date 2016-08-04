package com.doctor.sun.util;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JacksonUtils {

    private static ObjectMapper objectMapper;

    private JacksonUtils() {

    }

    public static ObjectMapper getInstance() {
        if (objectMapper == null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper = mapper;
        }

        return objectMapper;
    }

    /**
     * javaBean,list,array convert to json string
     */
    public static String toJson(Object obj) {
        try {
            return getInstance().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * json string convert to javaBean
     */
    public static <T> T fromJson(String jsonStr, JavaType clazz) {
        try {
            return getInstance().readValue(jsonStr, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * json string convert to javaBean
     */
    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        try {
            return getInstance().readValue(jsonStr, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * json string convert to map
     */
    public static <T> Map toMap(String jsonStr)
            throws Exception {
        return getInstance().readValue(jsonStr, Map.class);
    }

    /**
     * json string convert to map with javaBean
     */
    public static <T> Map<String, T> toMap(String jsonStr, Class<T> clazz)
            throws Exception {
        Map<String, Map<String, Object>> map = getInstance().readValue(jsonStr,
                new TypeReference<Map<String, T>>() {
                });
        Map<String, T> result = new HashMap<String, T>();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            result.put(entry.getKey(), fromMap(entry.getValue(), clazz));
        }
        return result;
    }

    /**
     * json array string convert to list with javaBean
     */
    public static <T> List<T> toList(String jsonArrayStr, Class<T> clazz)
            throws Exception {
        List<Map<String, Object>> list = getInstance().readValue(jsonArrayStr,
                new TypeReference<List<T>>() {
                });
        List<T> result = new ArrayList<T>();
        for (Map<String, Object> map : list) {
            result.add(fromMap(map, clazz));
        }
        return result;
    }

    /**
     * map convert to javaBean
     */
    public static <T> T fromMap(Map map, Class<T> clazz) {
        return getInstance().convertValue(map, clazz);
    }

    public static final <V> V fromResource(Context context, int resource, Class<V> type) {
        try {
            return fromJson(parseResource(context, resource), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final <V> V fromResource(Context context, int resource, JavaType type) {
        try {
            return fromJson(parseResource(context, resource), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final String parseResource(Context context, int resource) throws IOException {
        InputStream is = context.getResources().openRawResource(resource);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        return writer.toString();
    }
}
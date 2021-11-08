package com.gliese.noteedit.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 封装了Jackson的转换方法
 */
public class JsonUtils {
    public static String objectToJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            return json;
        }
    }

    public static Object jsonToObject(String json, Class objectClass) {
        ObjectMapper mapper = new ObjectMapper();
        Object object = null;
        try {
            object = mapper.readValue(json, objectClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            return object;
        }
    }
}

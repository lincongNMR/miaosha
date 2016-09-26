package org.seckill.utils.cache;

import com.alibaba.druid.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.ClassUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by mengwei1 on 2015/12/26.
 */
public class JSONUtil {
    private static final Logger LOG = Logger.getLogger(JSONUtil.class);
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        try {
            return gson.fromJson(jsonStr, clazz);
        } catch (JsonSyntaxException e) {
            LOG.error("gson.fromJson error", e);
        }
        return null;
    }

    public static <T> T fromJson(String jsonStr, Type type) {
        try {
            return gson.fromJson(jsonStr, type);
        } catch (JsonSyntaxException e) {
            LOG.error("gson.fromJson error", e);
        }
        return null;
    }

    public static String toJson(Object obj) {
        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            LOG.error("gson.toJson error", e);
        }
        return null;
    }

    private JSONUtil() {

    }

    public static Map<String, Object> toMap(JSONObject jsonObject) {
        Iterator<String> keyIt = jsonObject.keys();
        Map<String, Object> map = new HashMap<String, Object>();
        while (keyIt.hasNext()) {
            String key = keyIt.next();

            map.put(key, JSONUtil.toMap(jsonObject.getJSONObject(key)));

        }
        return map;

    }

    public static String getSetterMethod(String fieldName) {
        return "set" + firstLetterCaps(fieldName);
    }

    public static String getGetterMethod(String fieldName) {
        return "get" + firstLetterCaps(fieldName);
    }

    public static String firstLetterCaps(String fieldName) {
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String restLetters = fieldName.substring(1);
        return firstLetter + restLetters;
    }

    public static Object convert2Object(JSONArray jsonArray, Class infoClass) throws Exception {
        List list = new LinkedList();
        Iterator it = jsonArray.iterator();
        while (it.hasNext()) {
            Object obj = null;
            if (infoClass.isInstance(new String())) {
                obj = it.next().toString();
            } else if (infoClass.isPrimitive() || infoClass.isAssignableFrom(Number.class)) {
                obj = it.next();
            } else {
                JSONObject elJson = JSONObject.fromObject(it.next());
                obj = convert2Object(elJson, infoClass);
            }
            list.add(obj);
        }
        return list;
    }

    public static Object convert2Object(JSONObject jsonObject, Class infoClass) throws Exception {
        Object obj = infoClass.newInstance();
        Set<String> keySet = jsonObject.keySet();
        for (String e : keySet) {
            if (StringUtils.isEmpty(jsonObject.getString(e))) {
                continue;
            }
            String fieldName = e;
            String getterMethodName = getGetterMethod(fieldName);
            Method gmethod;
            try{
                gmethod = infoClass.getMethod(getterMethodName);
            }catch(Exception excp){
                LOG.error(excp);
                continue;
            }
            Class typeClass = gmethod.getReturnType();

            System.out.println(typeClass.getName());

            String setterMethodName = getSetterMethod(fieldName);
            Method smethod = infoClass.getMethod(setterMethodName, typeClass);

            if (typeClass.isInstance(new String())) {
                smethod.invoke(obj, jsonObject.getString(e));
            } else if (typeClass.isPrimitive()) {
                smethod.invoke(obj, jsonObject.getDouble(e));
            } else if (Date.class.isAssignableFrom(typeClass)) {
                JSONObject eleStr = jsonObject.getJSONObject(e);
                Date d = (Date) JSONObject.toBean(eleStr,Date.class);

                smethod.invoke(obj, d);

            } else if (Number.class.isAssignableFrom(typeClass)) {
                if (typeClass.equals(Integer.class)) {
                    smethod.invoke(obj, jsonObject.getInt(e));
                } else if (typeClass.equals(Float.class)) {
                    smethod.invoke(obj, (float) (jsonObject.getDouble(e)));
                } else if (typeClass.equals(Double.class)) {
                    smethod.invoke(obj, jsonObject.getDouble(e));
                } else if (typeClass.equals(BigDecimal.class)) {
                    smethod.invoke(obj, new BigDecimal(jsonObject.getString(e)));
                } else if (typeClass.equals(Long.class)) {
                    smethod.invoke(obj, jsonObject.getLong(e));
                }
            } else if (typeClass.isAssignableFrom(List.class)) {
                JSONArray jsonArray;

                try {
                    jsonArray = jsonObject.getJSONArray(e);
                } catch (Exception excp) {
                    LOG.error("json string " + jsonObject.getString(e) + " is not a json array", excp);
                    excp.printStackTrace();
                    continue;
                }
                Field field = infoClass.getDeclaredField(e);

                smethod.invoke(obj, convert2Object(jsonArray, retrieveActualTypeFromCollection(field)));
            } else {
                Object complexProperty = convert2Object(jsonObject.getJSONObject(e), typeClass);
                smethod.invoke(obj, complexProperty);
            }
        }
        return obj;

    }

    private Object initialize(Class clazz) {
        try {
            if (clazz.isAssignableFrom(List.class)) {
                return new LinkedList();
            } else return clazz.newInstance();
        } catch (Exception e) {
            LOG.error("fail to initialize " + clazz.getName() + ": not found non-parameterized constructor!!");
            return null;
        }
    }

    public static Class retrieveActualTypeFromCollection(Field parameter) {
        Type fc = parameter.getGenericType();
        if(null==fc) return null;
        if(fc instanceof ParameterizedType){
            ParameterizedType pt = (ParameterizedType)fc;
            Class genericClazz = (Class)pt.getActualTypeArguments()[0];
            return genericClazz;
        }
        return null;
    }
}
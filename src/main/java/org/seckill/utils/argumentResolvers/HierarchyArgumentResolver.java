package org.seckill.utils.argumentResolvers;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.seckill.utils.annotation.HierarchyArgument;
import org.seckill.utils.cache.JSONUtil;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by linyitian on 2016/7/22.
 */
public class HierarchyArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger log = Logger.getLogger(HierarchyArgumentResolver.class);

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(HierarchyArgument.class)!=null;
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        System.out.println("resolveArgument----------------------------"+parameter.getParameterName());
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        HierarchyArgument annotation = parameter.getParameterAnnotation(HierarchyArgument.class);
        String defaultValue = annotation.defaultValue();

        String parameterName = parameter.getParameterName();

        Class parameterClazz = parameter.getParameterType();
        Object originalObj = null;
        originalObj = this.initialize(parameterClazz);

        try{
            String bodyDataStr = request.getParameter("body");
            if(bodyDataStr==null){
                if(StringUtils.hasLength(defaultValue)){
                    bodyDataStr = new JSONObject().put(parameterName,defaultValue).toString();
                }else return null;
            }

            JSONObject entityJSONObject = JSONObject.fromObject(bodyDataStr);

            if(parameterClazz.isAssignableFrom(List.class)){
                Type fc = parameter.getGenericParameterType();
                if(fc == null) return originalObj;

                if(fc instanceof ParameterizedType) // 【3】如果是泛型参数的类型
                {
                    ParameterizedType pt = (ParameterizedType) fc;

                    Class elementType = (Class)pt.getActualTypeArguments()[0];

                    JSONArray parameterJSONArray = entityJSONObject.getJSONArray(parameterName);

                    return JSONUtil.convert2Object(parameterJSONArray,elementType);

                }
            }else if(parameterClazz.isAssignableFrom(Number.class)){
                if (parameterClazz.equals(Integer.class)) {
                    return entityJSONObject.getInt(parameterName);
                } else if (parameterClazz.equals(Float.class)) {
                    return entityJSONObject.getDouble(parameterName);
                } else if (parameterClazz.equals(Double.class)) {
                    return entityJSONObject.getDouble(parameterName);
                } else if (parameterClazz.equals(BigDecimal.class)) {
                    return new BigDecimal(entityJSONObject.getLong(parameterName));
                } else if (parameterClazz.equals(Long.class)) {
                    return entityJSONObject.getLong(parameterName);
                }
            }else if(parameterClazz.isAssignableFrom(String.class)){

                return entityJSONObject.getString(parameterName);
            }else if(parameterClazz.isPrimitive()){
                String parameterClazzName = parameterClazz.getName();
                if(parameterClazzName.equals(PRIMITIVE_INT)){
                    return entityJSONObject.getInt(parameterName);
                }else if(parameterClazzName.equals(PRIMITIVE_FLOAT)||parameterClazzName.equals(PRIMITIVE_DOUBLE)){
                    return entityJSONObject.getDouble(parameterName);
                }else if(parameterClazzName.equals(PRIMITIVE_LONG)){
                    return entityJSONObject.getLong(parameterName);
                }else if(parameterClazzName.equals(PRIMITIVE_BYTE)){
                    return new Byte(entityJSONObject.getString(parameterName));
                }else if(parameterClazzName.equals(PRIMITIVE_SHORT)){
                    return new Short(entityJSONObject.getString(parameterName));
                }else return 0;
            } else {
                JSONObject parameterJSONObject = entityJSONObject.getJSONObject(parameterName);
                return JSONUtil.convert2Object(parameterJSONObject, parameterClazz);
            }
        }catch(Exception e){
            return originalObj;
        }

        return originalObj;

    }
    private static final String PRIMITIVE_INT = "int";
    private static final String PRIMITIVE_FLOAT = "float";
    private static final String PRIMITIVE_LONG = "long";
    private static final String PRIMITIVE_BYTE = "byte";
    private static final String PRIMITIVE_DOUBLE = "double";
    private static final String PRIMITIVE_SHORT = "short";

    private Object initialize(Class clazz){
        try{
            if(clazz.isAssignableFrom(List.class)){
                return new LinkedList();
            }else if(clazz.isAssignableFrom(Number.class)||clazz.isPrimitive()){
                return 0;
            }
            else return clazz.newInstance();
        }catch(Exception e){
            log.error("fail to initialize "+clazz.getName()+": not found non-parameterized constructor!!");
            return null;
        }
    }



}

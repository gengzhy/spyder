package xin.cosmos.common.codec;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import xin.cosmos.common.anno.ApiDict;
import lombok.SneakyThrows;

import java.lang.reflect.Type;
import java.util.stream.Stream;

/**
 * 自定义fastjson枚举字典反序列化处理
 */
public class ApiDictDeserializer implements ObjectDeserializer {
    /**
     * 反序列化
     */
    @SneakyThrows
    @Override
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
        if (o == null) {
            return null;
        }
        Class<?> cls = Class.forName(type.getTypeName());
        Type[] is = cls.getGenericInterfaces();
        if (is == null || is.length == 0) {
            return null;
        }
        if (!Stream.of(is).anyMatch(i -> i.getTypeName().equals(ApiDict.class.getTypeName()))) {
            throw new RuntimeException(o + "字段类型必须实现ApiDict接口");
        }
        String code = defaultJSONParser.getLexer().stringVal();
        return (T) ApiDict.convert(code, (Class) type);
    }

    /**
     * 仅对字符串
     */
    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }

    /**
     * 递归反序列化拿到枚举字典code值
     *
     * @param jsonObject
     * @param key
     * @return
     */
    private Object getCode(JSONObject jsonObject, Object key) {
        if (jsonObject.containsKey(key)) {
            return jsonObject.get(key);
        }
        for (String k : jsonObject.keySet()) {
            Object o = jsonObject.get(k);
            if (o instanceof JSONObject) {
                return getCode((JSONObject) jsonObject.get(k), key);
            } else if (o instanceof JSONArray) {

            }
        }
        return null;
    }
}
package xin.cosmos.common.codec;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;

/**
 * 自定义fastjson反序列化数字处理
 */
public class NumberDeserializer implements ObjectDeserializer {
    /**反序列化*/
    @SneakyThrows
    @Override
    public String deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
        if (o == null) {
            return null;
        }
        String code = defaultJSONParser.getLexer().stringVal();
        code = StringUtils.isEmpty(code) ? code : code.replaceAll(",", "");
        return code;
    }

    /**仅对字符串*/
    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
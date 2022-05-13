package xin.cosmos.common.anno;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * api字典基类
 *
 */
public interface ApiDict {
    /**
     * 字典编码
     */
    String getCode();

    String getDesc();

    /**
     * 字典编码转字典
     *
     * @param <T>
     * @param code
     * @param dict 字典
     * @return
     */
    static <T extends Enum<T> & ApiDict> T convert(String code, Class<T> dict) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        Assert.notNull(dict, "没有指定的枚举字典");
        Optional<T> t = Stream.of(dict.getEnumConstants())
                .filter(e -> code.equals(e.getCode()))
                .findAny();
        if (t.isPresent()) {
            return t.get();
        }
        // 如果对应的code没找到，则匹配定义枚举的类型名称name
        t = Stream.of(dict.getEnumConstants())
                .filter(e -> code.equals(e.name()))
                .findAny();
        return t.isPresent() ? t.get() : null;
    }
}

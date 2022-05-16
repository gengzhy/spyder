package xin.cosmos.common.basic.dict;

import xin.cosmos.common.util.ObjectsUtil;

public interface IDict<E extends Enum<E>> {
    /**
     * 描述
     *
     * @return
     */
    String getDesc();

    /**
     * 枚举名称转枚举
     *
     * @param enumName 枚举名称
     * @param clazz 枚举类
     * @param <E>
     * @return
     */
    static <E extends Enum<E> & IDict> E findByName(String enumName, Class<E> clazz) {
        if (ObjectsUtil.isNull(enumName)) {
            return null;
        }
        return Enum.valueOf(clazz, enumName);
    }
}

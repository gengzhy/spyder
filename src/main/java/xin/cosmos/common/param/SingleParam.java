package xin.cosmos.common.param;

import lombok.Data;

import java.io.Serializable;

/**
 * 单参数
 */
@Data
public class SingleParam<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 请求参数（类型由传入参数类型确定）
     */
    private T key;
}

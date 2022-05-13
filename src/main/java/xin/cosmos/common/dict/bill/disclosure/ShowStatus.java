package xin.cosmos.common.dict.bill.disclosure;

import xin.cosmos.common.anno.ApiDict;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 票据承兑信用信息披露查询-披露状态
@AllArgsConstructor
@Getter
public enum ShowStatus implements ApiDict {
    SHOWED("1", "已披露"),
    NO_SHOW("2", "未披露"),
    ;

    private String code;
    private String desc;
}

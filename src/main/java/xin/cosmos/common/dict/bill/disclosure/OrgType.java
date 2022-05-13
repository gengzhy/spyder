package xin.cosmos.common.dict.bill.disclosure;

import xin.cosmos.common.anno.ApiDict;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 票据承兑信用信息披露查询-承兑人机构类别
@AllArgsConstructor
@Getter
public enum OrgType implements ApiDict {
    CORP("1", "企业"),
    OTHER("-1", "其他"),
    ;

    private String code;
    private String desc;
}

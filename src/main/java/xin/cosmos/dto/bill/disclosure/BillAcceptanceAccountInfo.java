package xin.cosmos.dto.bill.disclosure;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

// 票据承账号查询
@Data
public class BillAcceptanceAccountInfo {
    @JSONField(name = "code")
    private int code;// 响应码

    @JSONField(name = "success")
    private boolean success;// 是否响应成功

    @JSONField(name = "message")
    private String message;// 响应消息

    @JSONField(name = "data")
    private List<Body> data;// 响应数据体

    // 数据体
    @Data
    public static class Body {
        // 承兑人账号
        @JSONField(name = "entAccount")
        private String entAccount;

        // 承兑人名称
        @JSONField(name = "entName")
        private String entName;

        // 信用代码
        @JSONField(name = "soccode")
        private String creditCode;

    }
}

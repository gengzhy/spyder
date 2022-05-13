package xin.cosmos.dto.bill.disclosure;

import com.alibaba.fastjson.annotation.JSONField;
import xin.cosmos.common.codec.ApiDictDeserializer;
import xin.cosmos.common.dict.bill.disclosure.OrgType;
import xin.cosmos.common.dict.bill.disclosure.ShowStatus;
import lombok.Data;

import java.util.List;

// 票据承兑信用信息披露记录
@Data
public class BillAcceptanceInfo {
    @JSONField(name = "code")
    private int code;// 响应码

    @JSONField(name = "success")
    private boolean success;// 是否响应成功

    @JSONField(name = "message")
    private String message;// 响应消息

    @JSONField(name = "data")
    private Body data;// 响应数据体

    // 数据体
    @Data
    public static class Body {
        // 披露主体基本信息
        @JSONField(name = "entListInfoVo")
        private BaseInfo baseInfo;

        // 披露信息数据体
        @JSONField(name = "batchSettleInfoPage")
        private DetailInfo detailInfo;

    }

    // 披露主体基本信息
    @Data
    public static class BaseInfo {
        // 承兑人名称
        @JSONField(name = "entName")
        private String entName;

        // 信用代码
        @JSONField(name = "soccode")
        private String creditCode;

        // 机构类型（1-企业）
        @JSONField(name = "acptOrgType", deserializeUsing = ApiDictDeserializer.class)
        private OrgType orgType;

        // 注册日期
        @JSONField(name = "createTime")
        private String registerDate;

    }

    // 披露信息数据体
    @Data
    public static class DetailInfo {
        @JSONField(name = "total")
        private int total;

        @JSONField(name = "size")
        private int size;

        // 披露信息数据体 - 详细记录
        @JSONField(name = "records")
        private List<DetailInfoRecord> records;

    }

    // 披露信息数据体 - 详细记录
    @Data
    public static class DetailInfoRecord {
        @JSONField(name = "acptName")
        private String acptName;

        @JSONField(name = "dimAcptBranchName")
        private String dimAcptBranchName;

        @JSONField(name = "acptAmount")
        private String acptAmount;

        @JSONField(name = "acptOver")
        private String acptOver;

        @JSONField(name = "totalOverdueAmount")
        private String totalOverdueAmount;

        @JSONField(name = "overdueOver")
        private String overdueOver;

        @JSONField(name = "showDate")
        private String showDate;

        @JSONField(name = "relDate")
        private String relDate; // "",

        @JSONField(name = "remark")
        private String remark;

        @JSONField(name = "entRemark")
        private String entRemark;

        @JSONField(name = "entDetailRemark")
        private String entDetailRemark;

        @JSONField(name = "showStatus", deserializeUsing = ApiDictDeserializer.class)
        private ShowStatus showStatus;

    }
}

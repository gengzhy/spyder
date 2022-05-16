package xin.cosmos.common.basic.dict.global;

import lombok.Getter;

/**
 * 业务类型资源目标爬取网络地址
 */
@Getter
public enum BusinessTypeSourceSpiderTargetURL {
    BILL_ACCEPTANCE_DISCLOSURE_INFO_QUERY("票据承兑信用信息披露查询", "URL执行先后顺序：先查询承兑人账号，再查询票据承兑信用信息披露", new SourceURL(
            // 执行顺序1：查 entAccount（承兑人账号）
            "https://disclosure.shcpe.com.cn/ent/public/findAccInfoListByAcptName",
            // 执行顺序2：票据承兑信用信息披露查询
            "https://disclosure.shcpe.com.cn/ent/public/findSettlePage")),

    ;

    BusinessTypeSourceSpiderTargetURL(String name, String desc, SourceURL sourceURL) {
        this.name = name;
        this.desc = desc;
        this.sourceURL = sourceURL;
    }

    /**
     * 接口描述
     */
    private String name;
    /**
     * 备注
     */
    private String desc;
    /**
     * 资源URL地址
     */
    private SourceURL sourceURL;

    /**
     * 资源地址
     */
    @Getter
    public static class SourceURL {
        String[] urls;

        SourceURL(String... urls) {
            this.urls = urls;
        }
    }
}

package xin.cosmos.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 企业数据源文件信息
 */
@ConfigurationProperties(prefix = "corp")
@Data
public class CustomCorpProperties {
    /**
     * 企业Excel文件名是否截取小括号前内容作为模糊查询（官网默认使用该模糊匹配）
     */
    private boolean substrLeftBrackets;

    /**
     * 允许访问的url
     */
    private AllowedOrigin allowedOrigin;

    @Data
    public static class AllowedOrigin {
        private boolean enable;
        private String[] allowedUrls;
    }
}

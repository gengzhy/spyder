package xin.cosmos.dto.bill.disclosure;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 企业名称实体
 */
@Data
public class CorpEntity {
    @ExcelProperty(value = "序号")
    private Integer index;
    @ExcelProperty(value = "企业名称")
    private String corpName;
}

package xin.cosmos;

import com.alibaba.excel.EasyExcel;
import xin.cosmos.dto.bill.disclosure.BillAcceptanceVO;
import xin.cosmos.service.BillAcceptanceDisclosureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class JSpyderApplicationTests {

    @Autowired
    private BillAcceptanceDisclosureService billAcceptanceDisclosureService;
    @Test
    void contextLoads() {
        String showMonth = "2022-04";
        List<BillAcceptanceVO> acceptanceVOs = billAcceptanceDisclosureService.queryCorpBillAcceptance(showMonth);
        String xlsFile = "F:/贵州百强企业-202004票据承兑信用信息披露查询"+System.nanoTime()+".xlsx";
        if (!acceptanceVOs.isEmpty()) {
            EasyExcel.write(xlsFile, BillAcceptanceVO.class)
                    .sheet(showMonth+"披露信息")
                    .doWrite(acceptanceVOs);
        }
    }

}

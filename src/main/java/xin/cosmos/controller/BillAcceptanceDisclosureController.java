package xin.cosmos.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import xin.cosmos.common.R;
import xin.cosmos.common.util.FileUtils;
import xin.cosmos.dto.bill.disclosure.BillAcceptanceVO;
import xin.cosmos.dto.bill.disclosure.CorpEntity;
import xin.cosmos.service.BillAcceptanceDisclosureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 企业票据承兑信用信息披露查询controller
 */
@Controller
public class BillAcceptanceDisclosureController {
    @Autowired
    private BillAcceptanceDisclosureService billAcceptanceDisclosureService;

    /**
     * 源文件上传
     *
     * @param file
     * @return
     */
    @ResponseBody
    @PostMapping("/upload")
    public R<?> upload(@RequestParam(value = "file") MultipartFile file) {
        billAcceptanceDisclosureService.upload(file);
        return R.ok();
    }

    /**
     * 披露数据下载
     *
     * @param response
     * @param showMonth
     */
    @ResponseBody
    @PostMapping("/download")
    public void downloadExcelFile(HttpServletResponse response, @RequestParam("showMonth") String showMonth) {
        List<BillAcceptanceVO> data = billAcceptanceDisclosureService.queryCorpBillAcceptance(showMonth);
        String fileName = "【" + showMonth + "】票据承兑信用信息披露查询数据" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssS"));
        FileUtils.downloadExcel(fileName, response, data, BillAcceptanceVO.class);
    }

    /**
     * 模板下载
     *
     * @param response
     */
    @ResponseBody
    @PostMapping("/download/template")
    public void downloadExcelFile(HttpServletResponse response) throws IOException {
        String fileName = "票据承兑人源数据Excel模板" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssS"));
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("bill_source_data_template.xlsx");
        List<CorpEntity> corpEntities = new ArrayList<>();
        EasyExcel.read(inputStream, CorpEntity.class,
                new PageReadListener<CorpEntity>(list -> corpEntities.addAll(list))).sheet().doRead();
        FileUtils.downloadExcel(fileName, response, corpEntities, CorpEntity.class);
    }

}

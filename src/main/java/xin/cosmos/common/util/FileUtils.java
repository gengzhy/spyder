package xin.cosmos.common.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import xin.cosmos.common.R;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文件处理工具
 */
@Slf4j
public class FileUtils {

    /**
     * Excel数据浏览器下载
     * @param fileName
     * @param response
     * @param data
     * @param excelDataType
     */
    @SneakyThrows
    public static void downloadExcel(String fileName, HttpServletResponse response, List<?> data, Class<?> excelDataType) {
        if (ObjectsUtil.isNull(data)) {
            log.error("写文件错误：{}", "暂无可下载的数据");
            writeErrMsg(response, "暂无可下载的数据");
            return;
        }
        try {
            // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String urlFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + urlFileName + ".xlsx");
            response.addHeader("excel-file-name", urlFileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), excelDataType)
                    .excelType(ExcelTypeEnum.XLSX)
                    .autoCloseStream(true)
                    .sheet("sheet1")
                    .doWrite(data);
        } catch (Exception e) {
            log.error("写文件错误：{}", e.getMessage());
            writeErrMsg(response, e.getMessage());
        }
    }

    @SneakyThrows
    private static void writeErrMsg(HttpServletResponse response, String errMsg) {
        // 重置response
        response.reset();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().println(JSON.toJSONString(R.failed(false, errMsg)));
    }
}

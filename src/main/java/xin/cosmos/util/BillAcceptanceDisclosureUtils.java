package xin.cosmos.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import xin.cosmos.common.basic.dict.BusinessTypeSourceSpiderTargetURL;
import xin.cosmos.common.util.ObjectsUtil;
import xin.cosmos.dto.bill.disclosure.BillAcceptanceAccountInfo;
import xin.cosmos.dto.bill.disclosure.BillAcceptanceInfo;
import xin.cosmos.common.httpclient.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 票据承兑信用信息披露查询工具类
 */
@Slf4j
public final class BillAcceptanceDisclosureUtils {
    /**
     * 查 entAccount（承兑人账号）
     * <p>
     * 请求参数params：
     * acptName    -承兑人名称
     * random      -5位数字随机数
     */
    final static String ENT_ACCOUNT_URL;// = "https://disclosure.shcpe.com.cn/ent/public/findAccInfoListByAcptName";
    /**
     * 票据承兑信用信息披露查询
     * <p>
     * 请求参数params：
     * current=1&size=200                      -分页
     * acptName                                -承兑人名称
     * entAccount                              -承兑人账号（如：2021090800000089）
     * showMonth                               -披露信息时点日期（如：2022-04）
     * orderWay=DESC&orderField=acpt_amount    -排序
     */
    final static String BILL_ACCEPTANCE_CREDIT_URL;// = "https://disclosure.shcpe.com.cn/ent/public/findSettlePage";

    static {
        BusinessTypeSourceSpiderTargetURL sourceSpiderTargetURL = BusinessTypeSourceSpiderTargetURL.BILL_ACCEPTANCE_DISCLOSURE_INFO_QUERY;
        ENT_ACCOUNT_URL = sourceSpiderTargetURL.getSourceURL().getUrls()[0];
        BILL_ACCEPTANCE_CREDIT_URL = sourceSpiderTargetURL.getSourceURL().getUrls()[1];
    }

    /**
     * 票据承兑信用信息披露查询
     *
     * @param acptName   承兑人名称
     * @param entAccount 承兑人账号（通过{@linkplain BillAcceptanceDisclosureUtils#getShCPE_BillEntAccount}拿到）
     * @param showMonth  披露信息时点日期
     * @return
     */
    public static BillAcceptanceInfo getShCPE_AcceptanceInfo(String acptName, String entAccount, String showMonth) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("acptName", acptName);
        params.put("entAccount", entAccount);
        params.put("showMonth", showMonth);
        params.put("current", 1);
        params.put("size", 200);
        HttpClient httpClient = HttpClient.create();
        Map<String, String> headers = httpClient.defaultHeaders();
        String result = httpClient.get(BILL_ACCEPTANCE_CREDIT_URL, headers, params);
        log.info("承兑人票据信用披露信息===》原始响应报文：{}", result);

        JSONObject jsonObject = JSON.parseObject(result);
        BillAcceptanceInfo accountInfo = JSON.toJavaObject(jsonObject, BillAcceptanceInfo.class);
        return accountInfo;
    }

    /**
     * 票据承兑信用信息披露查询
     *
     * @param acptName  承兑人名称
     * @param showMonth 披露信息时点日期（月份）如：2022-04
     * @return
     */
    public static List<BillAcceptanceInfo> getShCPE_AcceptanceInfo(String acptName, String showMonth) {
        List<BillAcceptanceInfo> billAcceptanceInfoList = new LinkedList<>();
        BillAcceptanceAccountInfo account = getShCPE_BillEntAccount(acptName);
        if (!account.isSuccess()) {
            throw new RuntimeException("请求数据异常：" + account.getMessage());
        }
        List<BillAcceptanceAccountInfo.Body> datas = account.getData();
        if (datas == null || datas.isEmpty()) {
            log.error("{}-{}-响应数据为空", showMonth, acptName);
            return billAcceptanceInfoList;
        }
        datas.forEach(data -> {
            billAcceptanceInfoList.add(BillAcceptanceDisclosureUtils.getShCPE_AcceptanceInfo(acptName, data.getEntAccount(), showMonth));
        });
        return billAcceptanceInfoList;
    }

    /**
     * 根据承兑人名称查询承兑人账号信息
     *
     * @param acptName 承兑人名称
     * @return
     */
    public static BillAcceptanceAccountInfo getShCPE_BillEntAccount(String acptName) {
        if (StringUtils.isEmpty(acptName)) {
            throw new RuntimeException("承兑人名称{acptName}不能为空");
        }
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("acptName", acptName);//承兑人名称
        params.put("random", ObjectsUtil.randomNumber(5));// 随机数
        HttpClient httpClient = HttpClient.create();
        Map<String, String> headers = httpClient.defaultHeaders();
        String result = httpClient.get(ENT_ACCOUNT_URL, headers, params);
        log.info("承兑人账号信息===>原始响应报文：{}", result);

        JSONObject jsonObject = JSON.parseObject(result);
        BillAcceptanceAccountInfo accountInfo = JSON.toJavaObject(jsonObject, BillAcceptanceAccountInfo.class);
        return accountInfo;
    }

}

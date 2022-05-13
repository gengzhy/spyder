package xin.cosmos.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import xin.cosmos.common.Constant;
import xin.cosmos.common.basic.RedisService;
import xin.cosmos.common.dict.bill.disclosure.ShowStatus;
import xin.cosmos.common.properties.CustomCorpProperties;
import xin.cosmos.dto.bill.disclosure.BillAcceptanceInfo;
import xin.cosmos.dto.bill.disclosure.BillAcceptanceVO;
import xin.cosmos.dto.bill.disclosure.CorpEntity;
import xin.cosmos.util.BillAcceptanceDisclosureUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * 企业票据承兑信用信息披露查询service
 */
@Slf4j
@Service
public class BillAcceptanceDisclosureService {
    // 当前处理成功数据的标记位
    final static String HANDLE_OK_INDEX = "HANDLE_OK_INDEX";
    @Autowired
    private CustomCorpProperties customCorpProperties;
    @Autowired
    private RedisService redisService;

    /**
     * 文件上传（缓存到Redis）
     *
     * @param file
     */
    @SneakyThrows
    public void upload(MultipartFile file) {
        List<CorpEntity> corpEntities = new LinkedList<>();
        EasyExcel.read(file.getInputStream(), CorpEntity.class, new PageReadListener<CorpEntity>(list -> {
            list.sort(Comparator.comparing(CorpEntity::getIndex));
            corpEntities.addAll(list);
        })).sheet().doRead();
        redisService.setList(Constant.BILL_ACCEPTANCE_DISCLOSURE_META, corpEntities);
    }

    /**
     * 企业票据承兑信用信息披露查询
     * 当数据爬取失败时，不影响已经爬取成功的数据
     * 下次再进行爬取时，若缓存中存在爬取失败的数据标志位，仅会爬取失败的数据
     * 当全部爬取成功后，会删除缓存中爬取失败的标志（该标志默认过期时间为60分钟）
     *
     * @return
     */
    public List<BillAcceptanceVO> queryCorpBillAcceptance(String date) {
        if (StringUtils.isEmpty(date)) {
            throw new RuntimeException("披露信息时点日期不能为空");
        }
        String showMonth = (date.length() > 7) ? date.substring(0, 7) : date;
        final AtomicInteger index = new AtomicInteger(0);
        List<CorpEntity> corpEntities = redisService.getList(Constant.BILL_ACCEPTANCE_DISCLOSURE_META);
        Assert.notEmpty(corpEntities, "请先上传票据承兑人源数据后再进行下载");
        final List<BillAcceptanceVO> data = new LinkedList<>();

        // 企业名称模糊查询处理
        if (customCorpProperties.isSubstrLeftBrackets()) {
            Pattern p = Pattern.compile("[(（]+");
            corpEntities.forEach(e -> {
                String corpName = e.getCorpName();
                if (p.matcher(corpName).find()) {
                    int i = corpName.indexOf("(");
                    corpName = (i != -1) ? corpName.substring(0, i) : corpName.substring(0, corpName.indexOf("（"));
                    e.setCorpName(corpName);
                }
            });
        }

        // 异常断点数据处理
        final List<CorpEntity> finalNewDataSource = new LinkedList<>();
        Integer handleOkIndex = Optional.ofNullable((Integer) redisService.get(HANDLE_OK_INDEX)).orElse(0);
        if (handleOkIndex == 0) {
            finalNewDataSource.addAll(corpEntities);
        } else {
            for (int i = handleOkIndex, end = corpEntities.size(); i < end; i++) {
                finalNewDataSource.add(corpEntities.get(i));
            }
        }

        // 接口数据查询处理
        boolean isFullOk = true;
        String currentHandleCorp = "";
        for (CorpEntity corpEntity : finalNewDataSource) {
            currentHandleCorp = corpEntity.getCorpName();
            // 处理数据，捕获异常，防止成功的数据丢失
            try {
                List<BillAcceptanceInfo> acceptanceInfoList = BillAcceptanceDisclosureUtils.getShCPE_AcceptanceInfo(currentHandleCorp, showMonth);
                wrapper(acceptanceInfoList, data, index, showMonth);
                ++handleOkIndex;
            } catch (Exception e) {
                redisService.set(HANDLE_OK_INDEX, handleOkIndex, 3600L);
                log.error("处理到第几【{}】条数据【{}】，接口处理失败：{}", handleOkIndex, currentHandleCorp, e.getMessage());
                isFullOk = false;
                break;
            }
        }

        // 成功处理完成后清除标记位
        if (isFullOk) {
            redisService.delete(HANDLE_OK_INDEX);
        }
        return data;
    }

    /**
     * 构建响应数据
     *
     * @param acceptanceInfoList
     * @param data
     * @param index
     * @param showMonth
     */
    private void wrapper(List<BillAcceptanceInfo> acceptanceInfoList, List<BillAcceptanceVO> data,
                         AtomicInteger index, String showMonth) {
        if (acceptanceInfoList.isEmpty()) {
            return;
        }
        acceptanceInfoList.forEach(acceptanceInfo -> {
            BillAcceptanceInfo.Body body = acceptanceInfo.getData();
            BillAcceptanceInfo.BaseInfo baseInfo = body.getBaseInfo();
            List<BillAcceptanceInfo.DetailInfoRecord> records = body.getDetailInfo().getRecords();
            records.forEach(r -> {
                BillAcceptanceVO vo = new BillAcceptanceVO();
                vo.setIndex(index.addAndGet(1));
                vo.setShowMonth(showMonth);
                vo.setAcptName(baseInfo.getEntName());
                vo.setOrgType(baseInfo.getOrgType().getDesc());
                vo.setCreditCode(baseInfo.getCreditCode());
                vo.setRegisterDate(baseInfo.getRegisterDate());
                vo.setShowStatus(r.getShowStatus());
                vo.setDimAcptBranchName(r.getDimAcptBranchName());
                if (ShowStatus.NO_SHOW.equals(vo.getShowStatus())) {// 未披露
                    String msg = "未披露";
                    vo.setAcptAmount(msg);
                    vo.setAcptOver(msg);
                    vo.setTotalOverdueAmount(msg);
                    vo.setOverdueOver(msg);
                } else {
                    vo.setAcptAmount(r.getAcptAmount());
                    vo.setAcptOver(r.getAcptOver());
                    vo.setTotalOverdueAmount(r.getTotalOverdueAmount());
                    vo.setOverdueOver(r.getOverdueOver());
                }
                vo.setShowDate(r.getShowDate());
                vo.setRelDate(r.getRelDate());
                vo.setRemark(r.getRemark());
                vo.setEntRemark(r.getEntRemark());
                data.add(vo);
            });
        });
    }
}

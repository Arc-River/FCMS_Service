package com.fcms.web.controller.fcms;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fcms.common.annotation.Log;
import com.fcms.common.core.controller.BaseController;
import com.fcms.common.core.domain.AjaxResult;
import com.fcms.common.enums.BusinessType;
import com.fcms.system.domain.FcmsRecognitionLog;
import com.fcms.system.service.IFcmsRecognitionLogService;
import com.fcms.common.utils.poi.ExcelUtil;
import com.fcms.common.core.page.TableDataInfo;

/**
 * 客户识别记录Controller
 * 
 * @author fcms
 * @date 2022-06-06
 */
@RestController
@RequestMapping("/fcms/recoLog")
public class FcmsRecognitionLogController extends BaseController
{
    @Autowired
    private IFcmsRecognitionLogService fcmsRecognitionLogService;

    /**
     * 查询客户识别记录列表
     */
    @PreAuthorize("@ss.hasPermi('fcms:recoLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(FcmsRecognitionLog fcmsRecognitionLog)
    {
        startPage();
        List<FcmsRecognitionLog> list = fcmsRecognitionLogService.selectFcmsRecognitionLogList(fcmsRecognitionLog);
        return getDataTable(list);
    }

    /**
     * 导出客户识别记录列表
     */
    @PreAuthorize("@ss.hasPermi('fcms:recoLog:export')")
    @Log(title = "客户识别记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FcmsRecognitionLog fcmsRecognitionLog)
    {
        List<FcmsRecognitionLog> list = fcmsRecognitionLogService.selectFcmsRecognitionLogList(fcmsRecognitionLog);
        ExcelUtil<FcmsRecognitionLog> util = new ExcelUtil<FcmsRecognitionLog>(FcmsRecognitionLog.class);
        util.exportExcel(response, list, "客户识别记录数据");
    }

    /**
     * 获取客户识别记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('fcms:recoLog:query')")
    @GetMapping(value = "/{recoId}")
    public AjaxResult getInfo(@PathVariable("recoId") Long recoId)
    {
        return AjaxResult.success(fcmsRecognitionLogService.selectFcmsRecognitionLogByRecoId(recoId));
    }

    /**
     * 新增客户识别记录
     */
    @PreAuthorize("@ss.hasPermi('fcms:recoLog:add')")
    @Log(title = "客户识别记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FcmsRecognitionLog fcmsRecognitionLog)
    {
        return toAjax(fcmsRecognitionLogService.insertFcmsRecognitionLog(fcmsRecognitionLog));
    }

    /**
     * 修改客户识别记录
     */
    @PreAuthorize("@ss.hasPermi('fcms:recoLog:edit')")
    @Log(title = "客户识别记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FcmsRecognitionLog fcmsRecognitionLog)
    {
        return toAjax(fcmsRecognitionLogService.updateFcmsRecognitionLog(fcmsRecognitionLog));
    }

    /**
     * 删除客户识别记录
     */
    @PreAuthorize("@ss.hasPermi('fcms:recoLog:remove')")
    @Log(title = "客户识别记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recoIds}")
    public AjaxResult remove(@PathVariable Long[] recoIds)
    {
        return toAjax(fcmsRecognitionLogService.deleteFcmsRecognitionLogByRecoIds(recoIds));
    }
}

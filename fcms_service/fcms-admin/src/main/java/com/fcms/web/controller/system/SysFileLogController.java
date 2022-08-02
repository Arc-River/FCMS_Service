package com.fcms.web.controller.system;

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
import com.fcms.common.core.domain.entity.SysFileLog;
import com.fcms.system.service.ISysFileLogService;
import com.fcms.common.utils.poi.ExcelUtil;
import com.fcms.common.core.page.TableDataInfo;

/**
 * 文件上传日志Controller
 * 
 * @author fcms
 * @date 2022-06-01
 */
@RestController
@RequestMapping("/system/log")
public class SysFileLogController extends BaseController
{
    @Autowired
    private ISysFileLogService sysFileLogService;

    /**
     * 查询文件上传日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:filelog:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysFileLog sysFileLog)
    {
        startPage();
        List<SysFileLog> list = sysFileLogService.selectSysFileLogList(sysFileLog);
        return getDataTable(list);
    }

    /**
     * 导出文件上传日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:filelog:export')")
    @Log(title = "文件上传日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysFileLog sysFileLog)
    {
        List<SysFileLog> list = sysFileLogService.selectSysFileLogList(sysFileLog);
        ExcelUtil<SysFileLog> util = new ExcelUtil<SysFileLog>(SysFileLog.class);
        util.exportExcel(response, list, "文件上传日志数据");
    }

    /**
     * 获取文件上传日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:filelog:query')")
    @GetMapping(value = "/{fileId}")
    public AjaxResult getInfo(@PathVariable("fileId") Long fileId)
    {
        return AjaxResult.success(sysFileLogService.selectSysFileLogByFileId(fileId));
    }

    /**
     * 新增文件上传日志
     */
    @PreAuthorize("@ss.hasPermi('system:filelog:add')")
    @Log(title = "文件上传日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysFileLog sysFileLog)
    {
        return toAjax(sysFileLogService.insertSysFileLog(sysFileLog));
    }

    /**
     * 修改文件上传日志
     */
    @PreAuthorize("@ss.hasPermi('system:filelog:edit')")
    @Log(title = "文件上传日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysFileLog sysFileLog)
    {
        return toAjax(sysFileLogService.updateSysFileLog(sysFileLog));
    }

    /**
     * 删除文件上传日志
     */
    @PreAuthorize("@ss.hasPermi('system:filelog:remove')")
    @Log(title = "文件上传日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{fileIds}")
    public AjaxResult remove(@PathVariable Long[] fileIds)
    {
        return toAjax(sysFileLogService.deleteSysFileLogByFileIds(fileIds));
    }
}

package com.fcms.system.service.impl;

import java.util.List;
import com.fcms.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fcms.system.mapper.SysFileLogMapper;
import com.fcms.common.core.domain.entity.SysFileLog;
import com.fcms.system.service.ISysFileLogService;

/**
 * 文件上传日志Service业务层处理
 * 
 * @author fcms
 * @date 2022-06-01
 */
@Service
public class SysFileLogServiceImpl implements ISysFileLogService 
{
    @Autowired
    private SysFileLogMapper sysFileLogMapper;

    /**
     * 查询文件上传日志
     * 
     * @param fileId 文件上传日志主键
     * @return 文件上传日志
     */
    @Override
    public SysFileLog selectSysFileLogByFileId(Long fileId)
    {
        return sysFileLogMapper.selectSysFileLogByFileId(fileId);
    }

    /**
     * 查询文件上传日志列表
     * 
     * @param sysFileLog 文件上传日志
     * @return 文件上传日志
     */
    @Override
    public List<SysFileLog> selectSysFileLogList(SysFileLog sysFileLog)
    {
        return sysFileLogMapper.selectSysFileLogList(sysFileLog);
    }

    /**
     * 新增文件上传日志
     * 
     * @param sysFileLog 文件上传日志
     * @return 结果
     */
    @Override
    public int insertSysFileLog(SysFileLog sysFileLog)
    {
        sysFileLog.setCreateTime(DateUtils.getNowDate());
        return sysFileLogMapper.insertSysFileLog(sysFileLog);
    }

    /**
     * 修改文件上传日志
     * 
     * @param sysFileLog 文件上传日志
     * @return 结果
     */
    @Override
    public int updateSysFileLog(SysFileLog sysFileLog)
    {
        sysFileLog.setUpdateTime(DateUtils.getNowDate());
        return sysFileLogMapper.updateSysFileLog(sysFileLog);
    }

    /**
     * 批量删除文件上传日志
     * 
     * @param fileIds 需要删除的文件上传日志主键
     * @return 结果
     */
    @Override
    public int deleteSysFileLogByFileIds(Long[] fileIds)
    {
        return sysFileLogMapper.deleteSysFileLogByFileIds(fileIds);
    }

    /**
     * 删除文件上传日志信息
     * 
     * @param fileId 文件上传日志主键
     * @return 结果
     */
    @Override
    public int deleteSysFileLogByFileId(Long fileId)
    {
        return sysFileLogMapper.deleteSysFileLogByFileId(fileId);
    }
}

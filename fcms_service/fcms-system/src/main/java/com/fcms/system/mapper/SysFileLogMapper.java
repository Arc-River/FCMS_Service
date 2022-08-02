
package com.fcms.system.mapper;

import java.util.List;
import com.fcms.common.core.domain.entity.SysFileLog;

/**
 * 文件上传日志Mapper接口
 * 
 * @author fcms
 * @date 2022-06-01
 */
public interface SysFileLogMapper 
{
    /**
     * 查询文件上传日志
     * 
     * @param fileId 文件上传日志主键
     * @return 文件上传日志
     */
    public SysFileLog selectSysFileLogByFileId(Long fileId);

    /**
     * 查询文件上传日志列表
     * 
     * @param sysFileLog 文件上传日志
     * @return 文件上传日志集合
     */
    public List<SysFileLog> selectSysFileLogList(SysFileLog sysFileLog);

    /**
     * 新增文件上传日志
     * 
     * @param sysFileLog 文件上传日志
     * @return 结果
     */
    public int insertSysFileLog(SysFileLog sysFileLog);

    /**
     * 修改文件上传日志
     * 
     * @param sysFileLog 文件上传日志
     * @return 结果
     */
    public int updateSysFileLog(SysFileLog sysFileLog);

    /**
     * 删除文件上传日志
     * 
     * @param fileId 文件上传日志主键
     * @return 结果
     */
    public int deleteSysFileLogByFileId(Long fileId);

    /**
     * 批量删除文件上传日志
     * 
     * @param fileIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysFileLogByFileIds(Long[] fileIds);
}

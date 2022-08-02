package com.fcms.system.service.impl;

import java.util.List;

import com.fcms.common.annotation.DataScope;
import com.fcms.common.core.domain.entity.SysUser;
import com.fcms.common.utils.DateUtils;
import com.fcms.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fcms.system.mapper.FcmsRecognitionLogMapper;
import com.fcms.system.domain.FcmsRecognitionLog;
import com.fcms.system.service.IFcmsRecognitionLogService;

/**
 * 客户识别记录Service业务层处理
 * 
 * @author fcms
 * @date 2022-06-06
 */
@Service
public class FcmsRecognitionLogServiceImpl implements IFcmsRecognitionLogService 
{
    @Autowired
    private FcmsRecognitionLogMapper fcmsRecognitionLogMapper;

    /**
     * 查询客户识别记录
     * 
     * @param recoId 客户识别记录主键
     * @return 客户识别记录
     */
    @Override
    public FcmsRecognitionLog selectFcmsRecognitionLogByRecoId(Long recoId)
    {
        return fcmsRecognitionLogMapper.selectFcmsRecognitionLogByRecoId(recoId);
    }

    /**
     * 查询客户识别记录列表
     * 
     * @param fcmsRecognitionLog 客户识别记录
     * @return 客户识别记录
     */
    @Override
    @DataScope(userAlias = "frl", deptAlias = "frl")
    public List<FcmsRecognitionLog> selectFcmsRecognitionLogList(FcmsRecognitionLog fcmsRecognitionLog)
    {
        return fcmsRecognitionLogMapper.selectFcmsRecognitionLogList(fcmsRecognitionLog);
    }

    @Override
    @DataScope(userAlias = "frl", deptAlias = "frl")
    public Integer selectFcmsRecognitionLogCount(FcmsRecognitionLog recognitionLog) {
        return fcmsRecognitionLogMapper.selectFcmsRecognitionLogCount(recognitionLog);
    }

    /**
     * 新增客户识别记录
     * 
     * @param fcmsRecognitionLog 客户识别记录
     * @return 结果
     */
    @Override
    public int insertFcmsRecognitionLog(FcmsRecognitionLog fcmsRecognitionLog)
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        fcmsRecognitionLog.setDeptId(user.getDeptId());

        fcmsRecognitionLog.setCreateTime(DateUtils.getNowDate());
        return fcmsRecognitionLogMapper.insertFcmsRecognitionLog(fcmsRecognitionLog);
    }

    /**
     * 修改客户识别记录
     * 
     * @param fcmsRecognitionLog 客户识别记录
     * @return 结果
     */
    @Override
    public int updateFcmsRecognitionLog(FcmsRecognitionLog fcmsRecognitionLog)
    {
        fcmsRecognitionLog.setUpdateTime(DateUtils.getNowDate());
        return fcmsRecognitionLogMapper.updateFcmsRecognitionLog(fcmsRecognitionLog);
    }

    /**
     * 批量删除客户识别记录
     * 
     * @param recoIds 需要删除的客户识别记录主键
     * @return 结果
     */
    @Override
    public int deleteFcmsRecognitionLogByRecoIds(Long[] recoIds)
    {
        return fcmsRecognitionLogMapper.deleteFcmsRecognitionLogByRecoIds(recoIds);
    }

    /**
     * 删除客户识别记录信息
     * 
     * @param recoId 客户识别记录主键
     * @return 结果
     */
    @Override
    public int deleteFcmsRecognitionLogByRecoId(Long recoId)
    {
        return fcmsRecognitionLogMapper.deleteFcmsRecognitionLogByRecoId(recoId);
    }
}

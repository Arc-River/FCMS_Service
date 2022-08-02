package com.fcms.system.service.impl;

import java.util.List;

import cn.hutool.extra.pinyin.PinyinUtil;
import com.fcms.common.annotation.DataScope;
import com.fcms.common.core.domain.entity.SysUser;
import com.fcms.common.utils.DateUtils;
import com.fcms.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fcms.system.mapper.FcmsClientMapper;
import com.fcms.system.domain.FcmsClient;
import com.fcms.system.service.IFcmsClientService;

/**
 * 客户信息Service业务层处理
 * 
 * @author fcms
 * @date 2022-06-02
 */
@Service
public class FcmsClientServiceImpl implements IFcmsClientService 
{
    @Autowired
    private FcmsClientMapper fcmsClientMapper;

    /**
     * 查询客户信息
     * 
     * @param clientId 客户信息主键
     * @return 客户信息
     */
    @Override
    public FcmsClient selectFcmsClientByClientId(Long clientId)
    {
        return fcmsClientMapper.selectFcmsClientByClientId(clientId);
    }

    /**
     * 查询客户信息列表
     * 
     * @param fcmsClient 客户信息
     * @return 客户信息
     */
    @Override
    @DataScope(userAlias = "fc", deptAlias = "fc")
    public List<FcmsClient> selectFcmsClientList(FcmsClient fcmsClient)
    {
        return fcmsClientMapper.selectFcmsClientList(fcmsClient);
    }

    @Override
    @DataScope(userAlias = "fc", deptAlias = "fc")
    public Integer selectFcmsClientCount(FcmsClient fcmsClient) {
        return fcmsClientMapper.selectFcmsClientCount(fcmsClient);
    }

    /**
     * 新增客户信息
     * 
     * @param fcmsClient 客户信息
     * @return 结果
     */
    @Override
    public int insertFcmsClient(FcmsClient fcmsClient)
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        fcmsClient.setDeptId(user.getDeptId());

        fcmsClient.setCreateTime(DateUtils.getNowDate());
        fcmsClient.setClientPinyin(PinyinUtil.getPinyin(fcmsClient.getClientName()));
        return fcmsClientMapper.insertFcmsClient(fcmsClient);
    }

    /**
     * 修改客户信息
     * 
     * @param fcmsClient 客户信息
     * @return 结果
     */
    @Override
    public int updateFcmsClient(FcmsClient fcmsClient)
    {
        fcmsClient.setUpdateTime(DateUtils.getNowDate());
        return fcmsClientMapper.updateFcmsClient(fcmsClient);
    }

    /**
     * 批量删除客户信息
     * 
     * @param clientIds 需要删除的客户信息主键
     * @return 结果
     */
    @Override
    public int deleteFcmsClientByClientIds(Long[] clientIds)
    {
        return fcmsClientMapper.deleteFcmsClientByClientIds(clientIds);
    }

    /**
     * 删除客户信息信息
     * 
     * @param clientId 客户信息主键
     * @return 结果
     */
    @Override
    public int deleteFcmsClientByClientId(Long clientId)
    {
        return fcmsClientMapper.deleteFcmsClientByClientId(clientId);
    }
}

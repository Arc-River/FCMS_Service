package com.fcms.system.service;

import java.util.List;
import com.fcms.system.domain.FcmsClient;

/**
 * 客户信息Service接口
 * 
 * @author fcms
 * @date 2022-06-02
 */
public interface IFcmsClientService 
{
    /**
     * 查询客户信息
     * 
     * @param clientId 客户信息主键
     * @return 客户信息
     */
    public FcmsClient selectFcmsClientByClientId(Long clientId);

    public Integer selectFcmsClientCount(FcmsClient fcmsClient);

    /**
     * 查询客户信息列表
     * 
     * @param fcmsClient 客户信息
     * @return 客户信息集合
     */
    public List<FcmsClient> selectFcmsClientList(FcmsClient fcmsClient);

    /**
     * 新增客户信息
     * 
     * @param fcmsClient 客户信息
     * @return 结果
     */
    public int insertFcmsClient(FcmsClient fcmsClient);

    /**
     * 修改客户信息
     * 
     * @param fcmsClient 客户信息
     * @return 结果
     */
    public int updateFcmsClient(FcmsClient fcmsClient);

    /**
     * 批量删除客户信息
     * 
     * @param clientIds 需要删除的客户信息主键集合
     * @return 结果
     */
    public int deleteFcmsClientByClientIds(Long[] clientIds);

    /**
     * 删除客户信息信息
     * 
     * @param clientId 客户信息主键
     * @return 结果
     */
    public int deleteFcmsClientByClientId(Long clientId);
}

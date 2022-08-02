package com.fcms.system.mapper;

import java.util.List;

import com.fcms.system.domain.FcmsRecognitionLog;

/**
 * 客户识别记录Mapper接口
 *
 * @author fcms
 * @date 2022-06-06
 */
public interface FcmsRecognitionLogMapper {
    /**
     * 查询客户识别记录
     *
     * @param recoId 客户识别记录主键
     * @return 客户识别记录
     */
    public FcmsRecognitionLog selectFcmsRecognitionLogByRecoId(Long recoId);

    public Integer selectFcmsRecognitionLogCount(FcmsRecognitionLog recognitionLog);

    /**
     * 查询客户识别记录列表
     *
     * @param fcmsRecognitionLog 客户识别记录
     * @return 客户识别记录集合
     */
    public List<FcmsRecognitionLog> selectFcmsRecognitionLogList(FcmsRecognitionLog fcmsRecognitionLog);

    /**
     * 新增客户识别记录
     *
     * @param fcmsRecognitionLog 客户识别记录
     * @return 结果
     */
    public int insertFcmsRecognitionLog(FcmsRecognitionLog fcmsRecognitionLog);

    /**
     * 修改客户识别记录
     *
     * @param fcmsRecognitionLog 客户识别记录
     * @return 结果
     */
    public int updateFcmsRecognitionLog(FcmsRecognitionLog fcmsRecognitionLog);

    /**
     * 删除客户识别记录
     *
     * @param recoId 客户识别记录主键
     * @return 结果
     */
    public int deleteFcmsRecognitionLogByRecoId(Long recoId);

    /**
     * 批量删除客户识别记录
     *
     * @param recoIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFcmsRecognitionLogByRecoIds(Long[] recoIds);
}

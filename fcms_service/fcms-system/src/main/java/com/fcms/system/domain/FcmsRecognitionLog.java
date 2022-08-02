package com.fcms.system.domain;

import com.fcms.common.annotation.Excel;
import com.fcms.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 客户识别记录对象 fcms_recognition_log
 * 
 * @author fcms
 * @date 2022-06-06
 */
public class FcmsRecognitionLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    @Excel(name = "记录ID")
    private Long recoId;

    /** 客户ID */
    @Excel(name = "客户ID")
    private Long clientId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 所属部门ID */
    @Excel(name = "所属部门ID")
    private Long deptId;

    /** 客户姓名 */
    @Excel(name = "客户姓名")
    private String clientName;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phone;

    /** 检测是否活体*/
    @Excel(name = "检测是否活体")
    private Boolean liveness;

    /** 年龄 */
    @Excel(name = "年龄")
    private Integer age;

    /** 性别（0男 1女 2未知） */
    @Excel(name = "性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /** 人脸特征码 */
    private String faceCode;

    /** 人脸base64 */
    private String faceBase;

    /** 客户人脸特征码 */
    @Excel(name = "客户人脸特征码")
    private String faceCodeClient;

    /** 相似度 */
    @Excel(name = "相似度")
    private BigDecimal similar;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public Long getRecoId() {
        return recoId;
    }

    public void setRecoId(Long recoId) {
        this.recoId = recoId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getLiveness() {
        return liveness;
    }

    public void setLiveness(Boolean liveness) {
        this.liveness = liveness;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFaceCode() {
        return faceCode;
    }

    public void setFaceCode(String faceCode) {
        this.faceCode = faceCode;
    }

    public String getFaceBase() {
        return faceBase;
    }

    public void setFaceBase(String faceBase) {
        this.faceBase = faceBase;
    }

    public String getFaceCodeClient() {
        return faceCodeClient;
    }

    public void setFaceCodeClient(String faceCodeClient) {
        this.faceCodeClient = faceCodeClient;
    }

    public BigDecimal getSimilar() {
        return similar;
    }

    public void setSimilar(BigDecimal similar) {
        this.similar = similar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "FcmsRecognitionLog{" +
                "recoId=" + recoId +
                ", clientId=" + clientId +
                ", userId=" + userId +
                ", deptId=" + deptId +
                ", clientName='" + clientName + '\'' +
                ", phone='" + phone + '\'' +
                ", liveness=" + liveness +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", faceCode='" + faceCode + '\'' +
                ", faceBase='" + faceBase + '\'' +
                ", faceCodeClient='" + faceCodeClient + '\'' +
                ", similar=" + similar +
                ", status='" + status + '\'' +
                ", delFlag='" + delFlag + '\'' +
                '}';
    }
}
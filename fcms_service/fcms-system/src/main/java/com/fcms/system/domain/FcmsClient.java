package com.fcms.system.domain;

import com.fcms.common.annotation.Excel;
import com.fcms.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 客户信息对象 fcms_client
 *
 * @author fcms
 * @date 2022-06-04
 */
public class FcmsClient extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 客户ID */
    private Long clientId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 所属部门ID */
    @Excel(name = "所属部门ID")
    private Long deptId;

    /** 文件ID */
    @Excel(name = "文件ID")
    private Long fileId;

    /** 客户姓名 */
    @Excel(name = "客户姓名")
    private String clientName;

    /** 姓名拼音 */
    @Excel(name = "姓名拼音")
    private String clientPinyin;

    /** 备注名称 */
    @Excel(name = "备注名称")
    private String remarkName;

    /** 客户所属项目 */
    @Excel(name = "客户所属项目")
    private String projectName;

    /** 客户邮箱 */
    @Excel(name = "客户邮箱")
    private String email;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phone;

    /** 备用手机号码 */
    @Excel(name = "备用手机号码")
    private String phoneStandby;

    /** 活体检测 */
    @Excel(name = "活体检测")
    private Boolean liveness;

    /** 人脸特征码 */
    @Excel(name = "人脸特征码")
    private String faceCode;

    /** 年龄 */
    @Excel(name = "年龄")
    private Integer age;

    /** 性别（0男 1女 2未知） */
    @Excel(name = "性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /** 头像 */
    @Excel(name = "头像")
    private String avatar;

    /** 头像路径 */
    @Excel(name = "头像路径")
    private String avatarUrl;

    /** 头像Base64 */
    @Excel(name = "头像Base64")
    private String avatarBase;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

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

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPinyin() {
        return clientPinyin;
    }

    public void setClientPinyin(String clientPinyin) {
        this.clientPinyin = clientPinyin;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneStandby() {
        return phoneStandby;
    }

    public void setPhoneStandby(String phoneStandby) {
        this.phoneStandby = phoneStandby;
    }

    public Boolean getLiveness() {
        return liveness;
    }

    public void setLiveness(Boolean liveness) {
        this.liveness = liveness;
    }

    public String getFaceCode() {
        return faceCode;
    }

    public void setFaceCode(String faceCode) {
        this.faceCode = faceCode;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarBase() {
        return avatarBase;
    }

    public void setAvatarBase(String avatarBase) {
        this.avatarBase = avatarBase;
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
        return "FcmsClient{" +
                "clientId=" + clientId +
                ", userId=" + userId +
                ", deptId=" + deptId +
                ", fileId=" + fileId +
                ", clientName='" + clientName + '\'' +
                ", clientPinyin='" + clientPinyin + '\'' +
                ", remarkName='" + remarkName + '\'' +
                ", projectName='" + projectName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", phoneStandby='" + phoneStandby + '\'' +
                ", liveness=" + liveness +
                ", faceCode='" + faceCode + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", avatar='" + avatar + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", avatarBase='" + avatarBase + '\'' +
                ", status='" + status + '\'' +
                ", delFlag='" + delFlag + '\'' +
                '}';
    }
}
package com.fcms.common.core.domain.entity;

import com.fcms.common.annotation.Excel;
import com.fcms.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 文件上传日志对象 sys_file_log
 *
 * @author fcms
 * @date 2022-06-04
 */
public class SysFileLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文件ID */
    private Long fileId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 路径 */
    @Excel(name = "路径")
    private String url;

    /** 资源 */
    @Excel(name = "资源")
    private String uri;

    /** 文件描述 */
    @Excel(name = "文件描述")
    private String description;

    /** 文件类型（后缀） */
    @Excel(name = "文件类型", readConverterExp = "后=缀")
    private String fileType;

    /** 文件哈希值 */
    @Excel(name = "文件哈希值")
    private String hashCode;

    /** base64 */
    @Excel(name = "base64")
    private String baseCode;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setFileId(Long fileId)
    {
        this.fileId = fileId;
    }

    public Long getFileId()
    {
        return fileId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return url;
    }
    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public String getUri()
    {
        return uri;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }

    public String getFileType()
    {
        return fileType;
    }
    public void setHashCode(String hashCode)
    {
        this.hashCode = hashCode;
    }

    public String getHashCode()
    {
        return hashCode;
    }
    public void setBaseCode(String baseCode)
    {
        this.baseCode = baseCode;
    }

    public String getBaseCode()
    {
        return baseCode;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("fileId", getFileId())
                .append("userId", getUserId())
                .append("title", getTitle())
                .append("url", getUrl())
                .append("uri", getUri())
                .append("description", getDescription())
                .append("fileType", getFileType())
                .append("hashCode", getHashCode())
                .append("baseCode", getBaseCode())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
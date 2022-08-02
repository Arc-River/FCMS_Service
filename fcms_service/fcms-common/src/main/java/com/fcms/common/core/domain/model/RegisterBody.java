package com.fcms.common.core.domain.model;

/**
 * 用户注册对象
 * 
 * @author
 */
public class RegisterBody extends LoginBody
{
    /** 邀请用户ID */
    private Long inviteUserId;

    /** 邀请码 */
    private String inviteCode;

    public Long getInviteUserId() {
        return inviteUserId;
    }

    public void setInviteUserId(Long inviteUserId) {
        this.inviteUserId = inviteUserId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}

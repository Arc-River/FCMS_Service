package com.fcms.web.core.arcsoft;

import com.arcsoft.face.Face3DAngle;
import com.arcsoft.face.FaceInfo;

import java.io.Serializable;

/**
 * @author NYS
 * @date 2022/6/4
 */
public class ArcFaceEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long clientId;

    private Long faceId;

    private float similar;

    private String faceCode;

    private Integer age;

    private Integer sex;

    private Boolean liveness;

    private FaceInfo faceInfo;

    private Face3DAngle face3DAngle;

    public Long getFaceId() {
        return faceId;
    }

    public float getSimilar() {
        return similar;
    }

    public void setSimilar(float similar) {
        this.similar = similar;
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

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Boolean getLiveness() {
        return liveness;
    }

    public void setLiveness(Boolean liveness) {
        this.liveness = liveness;
    }

    public FaceInfo getFaceInfo() {
        return faceInfo;
    }

    public void setFaceInfo(FaceInfo faceInfo) {
        this.faceInfo = faceInfo;
    }

    public Face3DAngle getFace3DAngle() {
        return face3DAngle;
    }

    public void setFace3DAngle(Face3DAngle face3DAngle) {
        this.face3DAngle = face3DAngle;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setFaceId(Long faceId) {
        this.faceId = faceId;
    }
}

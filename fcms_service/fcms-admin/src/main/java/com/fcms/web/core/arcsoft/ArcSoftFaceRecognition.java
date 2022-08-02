package com.fcms.web.core.arcsoft;

import cn.hutool.core.util.StrUtil;
import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.toolkit.ImageInfo;
import com.fcms.common.exception.ServiceException;
import com.fcms.common.utils.file.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

/**
 * @author NYS
 * @date 2022/6/1
 */
@Component
public class ArcSoftFaceRecognition {
    static Logger log = LoggerFactory.getLogger(ArcSoftFaceRecognition.class);

    // 虹软APP_KEY
    @Value("${arcSoft.appID}")
    private String appId;

    // 虹软APP_KEY(linux)
    @Value("${arcSoft.sdkKey}")
    private String sdkKey;

    // 虹软静态库路径
    @Value("${arcSoft.dibUrl}")
    private String dibUrl;

    private FaceEngine initFaceEngine(String dibPath) {

        String path = dibPath;
        if (StrUtil.isBlank(dibPath)) {
            path = dibUrl;
//            path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "libs/LINUX64";
        }
        log.info("didPath:" + path);
        log.info("appId:" + appId);
        log.info("sdkKey:" + sdkKey);

        // 激活引擎
        FaceEngine faceEngine = new FaceEngine(path);
        int errorCode = faceEngine.activeOnline(appId, sdkKey);
        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            throw new ServiceException("引擎激活失败" + errorCode);
        }

        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
        errorCode = faceEngine.getActiveFileInfo(activeFileInfo);
        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            throw new ServiceException("获取激活文件信息失败" + errorCode);
        }

        // 引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
        engineConfiguration.setDetectFaceMaxNum(10);
        engineConfiguration.setDetectFaceScaleVal(16);
        // 功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(true);
        functionConfiguration.setSupportFace3dAngle(true);
        functionConfiguration.setSupportFaceDetect(true);
        functionConfiguration.setSupportFaceRecognition(true);
        functionConfiguration.setSupportGender(true);
        functionConfiguration.setSupportLiveness(true);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);

        // 初始化引擎
        errorCode = faceEngine.init(engineConfiguration);

        if (errorCode != ErrorInfo.MOK.getValue()) {
            throw new ServiceException("初始化引擎失败" + errorCode);
        } else {
            log.info("初始化引擎完成");
        }
        return faceEngine;
    }

    /**
     * 1:1特征码对比相似度
     *
     * @param faceCodeOne
     * @param faceCodeTwo
     * @return
     */
    public float compareFaceFeature(String faceCodeOne, String faceCodeTwo) {

        // 1.初始化引擎
        FaceEngine faceEngine = this.initFaceEngine(null);

        // 2.特征码
        byte[] featureData1 = Base64.getDecoder().decode(faceCodeOne);
        byte[] featureData2 = Base64.getDecoder().decode(faceCodeTwo);

        // 3.特征比对
        FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(featureData1);
        FaceFeature sourceFaceFeature = new FaceFeature();
        sourceFaceFeature.setFeatureData(featureData2);
        FaceSimilar faceSimilar = new FaceSimilar();

        int errorCode = faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            throw new ServiceException("相似度对比出错" + errorCode);
        }

        // 4.卸载引擎
        faceEngine.unInit();

        return faceSimilar.getScore();
    }

    /**
     * 1:N特征码对比相似度
     *
     * @param faceCode
     * @param faceList
     * @param minSimilar 最低相似度
     * @return
     */
    public ArcFaceEntity multiCompareFaceFeature(String faceCode, List<ArcFaceEntity> faceList, float minSimilar) {

        // 1.初始化引擎
        FaceEngine faceEngine = this.initFaceEngine(null);
        ArcFaceEntity faceEntity = null;

        // 2.特征码
        byte[] featureData = Base64.getDecoder().decode(faceCode);
        FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(featureData);

        // 3.特征比对
        FaceSimilar faceSimilar = new FaceSimilar();
        for (ArcFaceEntity face : faceList) {
            byte[] sourceFeatureData = Base64.getDecoder().decode(face.getFaceCode());
            FaceFeature sourceFaceFeature = new FaceFeature();
            sourceFaceFeature.setFeatureData(sourceFeatureData);

            int errorCode = faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
            if (errorCode != ErrorInfo.MOK.getValue()) {
                throw new ServiceException("相似度对比出错" + errorCode);
            }
            if (faceSimilar.getScore() >= minSimilar) {
                face.setSimilar(faceSimilar.getScore());
                faceEntity = face;
                break;
            }
        }

        // 4.卸载引擎
        faceEngine.unInit();

        return faceEntity;
    }

    /**
     * 人脸检测
     *
     * @param file
     * @return
     */
    public List<FaceInfo> getFaceInfo(File file) {
        // 初始化
        FaceEngine faceEngine = this.initFaceEngine(null);

        // 人脸检测
        ImageInfo imageInfo = getRGBData(file);
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        int errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            throw new ServiceException("人脸检查出错" + errorCode);
        }
        if (faceInfoList.size() < 1) {
            throw new ServiceException("没有检测到面部信息");
        }

        // 卸载引擎
        faceEngine.unInit();

        return faceInfoList;
    }

    /**
     * 提取人脸特征值
     */
    public String getFaceFeature(MultipartFile multFile) {
        File file = null;
        try {
            file = FileUtils.multipartFileToFile(multFile);
        } catch (Exception e) {
            throw new ServiceException("文件格式转换出错" + e.getMessage());
        }
        String faceCode = getFaceFeature(file);
        // 删除中间文件
        FileUtils.multipartFileToFileDeleteTempFile(multFile);
        return faceCode;
    }

    public String getFaceFeature(File file) {
        // 初始化
        FaceEngine faceEngine = this.initFaceEngine(null);

        // 人脸检测
        ImageInfo imageInfo = getRGBData(file);
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        int errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            throw new ServiceException("人脸检查出错" + errorCode);
        }
        if (faceInfoList.size() < 1) {
            throw new ServiceException("没有检测到面部信息");
        }

        // 特征提取
        FaceFeature faceFeature = new FaceFeature();
        errorCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            throw new ServiceException("人脸特征值提取出错" + errorCode);
        }
        String faceCode = Base64.getEncoder().encodeToString(faceFeature.getFeatureData());

        // 卸载引擎
        faceEngine.unInit();

        return faceCode;
    }

    /**
     * 获取详细人脸信息
     * (图片中包含多张人脸，仅获取最大的人脸)
     */
    public ArcFaceEntity getFaceDetailInfo(MultipartFile multFile) {
        File file = null;
        try {
            file = FileUtils.multipartFileToFile(multFile);
        } catch (Exception e) {
            throw new ServiceException("文件格式转换出错" + e.getMessage());
        }
        ArcFaceEntity arcFace = getFaceDetailInfo(file);
        // 删除中间文件
        FileUtils.multipartFileToFileDeleteTempFile(multFile);
        return arcFace;
    }

    public ArcFaceEntity getFaceDetailInfo(File file) {
        // 初始化
        FaceEngine faceEngine = this.initFaceEngine(null);
        ArcFaceEntity faceEntity = new ArcFaceEntity();

        //人脸检测
        ImageInfo imageInfo = getRGBData(file);
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        int errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            throw new ServiceException("人脸检查出错" + errorCode);
        }
        if (faceInfoList.size() < 1) {
            throw new ServiceException("没有检测到面部信息");
        }
        faceEntity.setFaceInfo(faceInfoList.get(0));

        // 设置活体测试
        errorCode = faceEngine.setLivenessParam(0.5f, 0.7f);
        // 人脸属性检测
        FunctionConfiguration configuration = new FunctionConfiguration();
        configuration.setSupportAge(true);
        configuration.setSupportFace3dAngle(true);
        configuration.setSupportGender(true);
        configuration.setSupportLiveness(true);
        errorCode = faceEngine.process(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList, configuration);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            throw new ServiceException("人脸属性检测出错" + errorCode);
        }

        // 性别检测
        List<GenderInfo> genderInfoList = new ArrayList<GenderInfo>();
        errorCode = faceEngine.getGender(genderInfoList);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            throw new ServiceException("性别检测出错" + errorCode);
        }
        faceEntity.setSex(genderInfoList.get(0).getGender());

        // 年龄检测
        List<AgeInfo> ageInfoList = new ArrayList<AgeInfo>();
        errorCode = faceEngine.getAge(ageInfoList);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            throw new ServiceException("年龄检测出错" + errorCode);
        }
        faceEntity.setAge(ageInfoList.get(0).getAge());

        // 3D信息检测
        List<Face3DAngle> face3DAngleList = new ArrayList<Face3DAngle>();
        errorCode = faceEngine.getFace3DAngle(face3DAngleList);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            throw new ServiceException("3D角度检测出错" + errorCode);
        }
        faceEntity.setFace3DAngle(face3DAngleList.get(0));

        // 活体检测
        List<LivenessInfo> livenessInfoList = new ArrayList<LivenessInfo>();
        errorCode = faceEngine.getLiveness(livenessInfoList);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            throw new ServiceException("活体检测出错" + errorCode);
        }
        faceEntity.setLiveness(livenessInfoList.get(0).getLiveness() == 1 ? true : false);

        // 特征提取
        FaceFeature faceFeature = new FaceFeature();
        errorCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            throw new ServiceException("人脸特征值提取出错" + errorCode);
        }
        String faceCode = Base64.getEncoder().encodeToString(faceFeature.getFeatureData());
        faceEntity.setFaceCode(faceCode);

        // 卸载引擎
        faceEngine.unInit();

        return faceEntity;
    }
}

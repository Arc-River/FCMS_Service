package com.fcms.web.controller.fcms;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.img.Img;
import cn.hutool.core.util.ObjectUtil;
import com.fcms.common.annotation.Log;
import com.fcms.common.core.controller.BaseController;
import com.fcms.common.core.domain.AjaxResult;
import com.fcms.common.core.domain.entity.SysUser;
import com.fcms.common.core.page.TableDataInfo;
import com.fcms.common.enums.BusinessType;
import com.fcms.common.exception.ServiceException;
import com.fcms.common.utils.SecurityUtils;
import com.fcms.common.utils.file.FileUtils;
import com.fcms.common.utils.poi.ExcelUtil;
import com.fcms.system.domain.FcmsClient;
import com.fcms.system.domain.FcmsRecognitionLog;
import com.fcms.system.service.IFcmsClientService;
import com.fcms.system.service.IFcmsRecognitionLogService;
import com.fcms.system.service.ISysUserService;
import com.fcms.web.core.arcsoft.ArcFaceEntity;
import com.fcms.web.core.arcsoft.ArcSoftFaceRecognition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户信息Controller
 *
 * @author fcms
 * @date 2022-06-02
 */
@RestController
@RequestMapping("/fcms/client")
public class FcmsClientController extends BaseController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IFcmsClientService fcmsClientService;

    @Autowired
    private ArcSoftFaceRecognition arcSoftFaceRecognition;

    @Autowired
    private IFcmsRecognitionLogService fcmsRecognitionLogService;

    /**
     * 查询客户信息列表
     */
    @PreAuthorize("@ss.hasPermi('fcms:client:list')")
    @GetMapping("/list")
    public TableDataInfo list(FcmsClient fcmsClient) {
        startPage();
        List<FcmsClient> list = fcmsClientService.selectFcmsClientList(fcmsClient);
        return getDataTable(list);
    }

    /**
     * 人脸检索
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PreAuthorize("@ss.hasPermi('fcms:client:searchFace')")
    @PostMapping("/searchFace")
    public AjaxResult searchFace(MultipartFile file) throws Exception {
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        SysUser user = userService.selectUserById(sysUser.getUserId());

        FcmsClient fcmsClient = new FcmsClient();
        List<FcmsClient> clientList = fcmsClientService.selectFcmsClientList(fcmsClient);
        if (clientList.size() == 0) {
            throw new ServiceException("没有客户数据，请先添加客户信息！");
        }
        List<ArcFaceEntity> faceList = new ArrayList<ArcFaceEntity>();
        for (FcmsClient client : clientList) {
            ArcFaceEntity faceEntity = new ArcFaceEntity();
            faceEntity.setClientId(client.getClientId());
            faceEntity.setFaceCode(client.getFaceCode());
            faceList.add(faceEntity);
        }

        // 人脸检索
        File faceFile = FileUtils.multipartFileToFile(file);
        ArcFaceEntity faceDetailInfo = arcSoftFaceRecognition.getFaceDetailInfo(faceFile);
        ArcFaceEntity arcFaceEntity = arcSoftFaceRecognition.multiCompareFaceFeature(faceDetailInfo.getFaceCode(), faceList, user.getMinSimilar().floatValue());

        // 写入检索记录
        FcmsRecognitionLog recognitionLog = new FcmsRecognitionLog();
        if (ObjectUtil.isNotNull(arcFaceEntity)) { // 对比结果不为空
            FcmsClient fClient = fcmsClientService.selectFcmsClientByClientId(arcFaceEntity.getClientId());
            recognitionLog.setClientId(fClient.getClientId());
            recognitionLog.setClientName(fClient.getClientName());
            recognitionLog.setPhone(fClient.getPhone());
            recognitionLog.setFaceCodeClient(fClient.getFaceCode());
            recognitionLog.setSimilar(new BigDecimal(Float.toString(arcFaceEntity.getSimilar())));
        }
        recognitionLog.setUserId(user.getUserId());
        recognitionLog.setAge(faceDetailInfo.getAge());
        recognitionLog.setLiveness(faceDetailInfo.getLiveness());
        recognitionLog.setFaceCode(faceDetailInfo.getFaceCode());
        recognitionLog.setSex(String.valueOf(faceDetailInfo.getSex()));
        Img.from(faceFile).setQuality(0.1).write(faceFile);
        String decodeStr = Base64.encode(faceFile);
        recognitionLog.setFaceBase(decodeStr);
        recognitionLog.setCreateBy(user.getNickName());
        fcmsRecognitionLogService.insertFcmsRecognitionLog(recognitionLog);

        // 删除中间文件
        FileUtils.multipartFileToFileDeleteTempFile(file);

        if (ObjectUtil.isNotNull(arcFaceEntity)) {
            return AjaxResult.success(arcFaceEntity);
        } else {
            return AjaxResult.error("没有找到该客户信息");
        }
    }

    /**
     * 导出客户信息列表
     */
    @PreAuthorize("@ss.hasPermi('fcms:client:export')")
    @Log(title = "客户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FcmsClient fcmsClient) {
        List<FcmsClient> list = fcmsClientService.selectFcmsClientList(fcmsClient);
        ExcelUtil<FcmsClient> util = new ExcelUtil<FcmsClient>(FcmsClient.class);
        util.exportExcel(response, list, "客户信息数据");
    }

    /**
     * 获取客户信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('fcms:client:query')")
    @GetMapping(value = "/{clientId}")
    public AjaxResult getInfo(@PathVariable("clientId") Long clientId) {
        return AjaxResult.success(fcmsClientService.selectFcmsClientByClientId(clientId));
    }

    /**
     * 新增客户信息
     */
    @PreAuthorize("@ss.hasPermi('fcms:client:add')")
    @Log(title = "客户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FcmsClient fcmsClient) {
        return toAjax(fcmsClientService.insertFcmsClient(fcmsClient));
    }

    /**
     * 修改客户信息
     */
    @PreAuthorize("@ss.hasPermi('fcms:client:edit')")
    @Log(title = "客户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FcmsClient fcmsClient) {
        return toAjax(fcmsClientService.updateFcmsClient(fcmsClient));
    }

    /**
     * 删除客户信息
     */
    @PreAuthorize("@ss.hasPermi('fcms:client:remove')")
    @Log(title = "客户信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{clientIds}")
    public AjaxResult remove(@PathVariable Long[] clientIds) {
        return toAjax(fcmsClientService.deleteFcmsClientByClientIds(clientIds));
    }
}

package com.fcms.web.controller.app;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fcms.common.config.SysConfig;
import com.fcms.common.constant.Constants;
import com.fcms.common.core.controller.BaseController;
import com.fcms.common.core.domain.AjaxResult;
import com.fcms.common.core.domain.entity.SysFileLog;
import com.fcms.common.core.domain.entity.SysUser;
import com.fcms.common.core.domain.model.LoginBody;
import com.fcms.common.core.domain.model.RegisterBody;
import com.fcms.common.exception.ServiceException;
import com.fcms.common.utils.SecurityUtils;
import com.fcms.common.utils.StringUtils;
import com.fcms.common.utils.file.FileUploadUtils;
import com.fcms.framework.config.ServerConfig;
import com.fcms.framework.web.service.SysLoginService;
import com.fcms.framework.web.service.SysPermissionService;
import com.fcms.framework.web.service.SysRegisterService;
import com.fcms.system.service.ISysConfigService;
import com.fcms.system.service.ISysFileLogService;
import com.fcms.system.service.ISysUserService;
import com.fcms.web.core.arcsoft.ArcFaceEntity;
import com.fcms.web.core.arcsoft.ArcSoftFaceRecognition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * @author NYS
 * @date 2022/5/26
 */
@RestController
@RequestMapping("/app/basics")
public class AppBasicsController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private ISysFileLogService sysFileLogService;

    @Autowired
    private ArcSoftFaceRecognition arcSoftFaceRecognition;

    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody user)
    {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return error("当前系统没有开启注册功能！");
        }

        if (StrUtil.isBlank(user.getInviteCode())) {
            return error("请输入邀请码！");
        } else {
            SysUser sysUser = userService.selectUserByInviteCode(user.getInviteCode());
            if (ObjectUtil.isEmpty(sysUser)) {
                return error("无效的邀请码！");
            }
            user.setInviteUserId(sysUser.getUserId());
            String uuid = IdUtil.randomUUID();
            user.setInviteCode(uuid);
        }

        String msg = registerService.register(user);

        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }


    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        // 仅允许member角色登录APP
        SysUser user = userService.selectUserByUserName(loginBody.getUsername());
        Set<String> roles = permissionService.getRolePermission(user);
        boolean isMember = roles.contains("member");
        if (!isMember) {
            throw new ServiceException("仅允许会员角色登录");
        }
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }


    /**
     * 检查APP更新
     * @param param
     * @return
     */
    @GetMapping(value = "/checkUpdate/{param}")
    public AjaxResult checkUpdate(@PathVariable String param) {

        String downloadUrl = "";
        String note = "版本更新，修复已知问题。\n请及时安装新版本";
        String version = "1.0.0";
        if (param.equals("ios")) {
            com.fcms.system.domain.SysConfig sysConfig = configService.selectConfigById(103L);
            downloadUrl = sysConfig.getConfigValue();
            version = sysConfig.getConfigContent();
            if (StrUtil.isNotBlank(sysConfig.getRemark())) {
                note = sysConfig.getRemark();
            }
        } else if (param.equals("android")) {
            com.fcms.system.domain.SysConfig sysConfig = configService.selectConfigById(102L);
            downloadUrl = sysConfig.getConfigValue();
            version = sysConfig.getConfigContent();
            if (StrUtil.isNotBlank(sysConfig.getRemark())) {
                note = sysConfig.getRemark();
            }
        } else {
            return AjaxResult.error(param+"平台不支持自动更新");
        }
        HashMap<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("note", note);
        updateInfo.put("version", version);
        updateInfo.put("now_url", downloadUrl);
        updateInfo.put("silent", 0); // 是否是静默更新
        updateInfo.put("force", 0); // 是否是强制更新
        updateInfo.put("net_check", 1); // 非WIfi是否提示

        return AjaxResult.success(updateInfo);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 用户信息
        SysUser sysUser = userService.selectUserById(user.getUserId());
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);

        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", sysUser);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 多人脸信息提取接口
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/feature/faces")
    public AjaxResult uploadFiles(List<MultipartFile> files) throws Exception
    {
        try {
            // 上传文件路径
            String filePath = SysConfig.getUploadPath();
            List<Map> urlList = new ArrayList<Map>();
            List<SysFileLog> fileList = new ArrayList<SysFileLog>();
            List<ArcFaceEntity> faceList = new ArrayList<ArcFaceEntity>();
            for (MultipartFile file : files)
            {
                // 人脸检测
                ArcFaceEntity arcFace = arcSoftFaceRecognition.getFaceDetailInfo(file);
                faceList.add(arcFace);
                // 上传并返回新文件名称
                SysFileLog sysFileLog = FileUploadUtils.upload(filePath, file);
                sysFileLog.setDescription("多人脸信息提取接口/feature/faces");
                String url = serverConfig.getUrl() + sysFileLog.getUri();
                sysFileLog.setUrl(url);
                sysFileLogService.insertSysFileLog(sysFileLog);
                fileList.add(sysFileLog);
                // 上传结果
                HashMap<String, String> result = new HashMap<>();
                result.put("url",url);
                urlList.add(result);
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("faceList", faceList);
            ajax.put("fileList", fileList);
            ajax.put("urlList", urlList);
            ajax.put("url", urlList.get(0).get("url"));
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }
}

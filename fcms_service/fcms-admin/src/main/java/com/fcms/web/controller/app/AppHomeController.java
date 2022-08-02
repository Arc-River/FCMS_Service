package com.fcms.web.controller.app;

/**
 * @author NYS
 * @date 2022/5/26
 */

import com.fcms.common.core.controller.BaseController;
import com.fcms.common.core.domain.AjaxResult;
import com.fcms.common.core.domain.entity.SysUser;
import com.fcms.common.core.page.TableDataInfo;
import com.fcms.common.utils.SecurityUtils;
import com.fcms.system.domain.FcmsClient;
import com.fcms.system.domain.FcmsRecognitionLog;
import com.fcms.system.domain.SysNotice;
import com.fcms.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.fcms.common.utils.PageUtils.startPage;

/**
 * @author NYS
 * @date 2022/5/26
 */
@RestController
@RequestMapping("/app/home")
public class AppHomeController extends BaseController {

    @Autowired
    private ISysNoticeService noticeService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IFcmsClientService fcmsClientService;

    @Autowired
    private IFcmsRecognitionLogService fcmsRecognitionLogService;


    @GetMapping("/data")
    public AjaxResult getInfo() {
        SysUser user = SecurityUtils.getLoginUser().getUser();

        SysNotice notice = new SysNotice();
        notice.setStatus("0");
        notice.setNoticeType(String.valueOf(2));
        List<SysNotice> billboardList = noticeService.selectNoticeList(notice);
        notice.setNoticeType(String.valueOf(1));
        notice.setUserId(user.getUserId());
        notice.setHits(0); // 未读
        List<SysNotice> noticeList = noticeService.selectNoticeList(notice);

        String documentUrl = configService.selectConfigByKey("sys.user.document");
        String androidDownloadUrl = configService.selectConfigByKey("app.download.android");
        String iosDownloadUrl = configService.selectConfigByKey("app.download.ios");

        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("documentUrl", documentUrl);
        ajax.put("androidDownloadUrl", androidDownloadUrl);
        ajax.put("iosDownloadUrl", iosDownloadUrl);
        ajax.put("noticeList", noticeList);
        ajax.put("billboardList", billboardList);
        return ajax;
    }

    /**
     * 获取全体通知列表
     */
    @GetMapping("/publicNoticeList")
    public TableDataInfo list(SysNotice notice) {

        notice.setUserId(null);
        notice.setStatus("0");
        notice.setNoticeType(String.valueOf(1));

        startPage();
        List<SysNotice> list = noticeService.selectPublicNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 获取统计数据
     */
    @GetMapping("/statistic")
    public AjaxResult statistic() {
        SysUser user = SecurityUtils.getLoginUser().getUser();

        SysUser sysUser = new SysUser();
        sysUser.setInviteUserId(user.getUserId());
        Integer inviteCount = userService.selectUserCount(sysUser);

        FcmsClient fcmsClient = new FcmsClient();
        fcmsClient.setUserId(user.getUserId());
        Integer clientCount = fcmsClientService.selectFcmsClientCount(fcmsClient);

        FcmsRecognitionLog recognitionLog = new FcmsRecognitionLog();
        recognitionLog.setUserId(user.getUserId());
        Integer recognitionLogCount = fcmsRecognitionLogService.selectFcmsRecognitionLogCount(recognitionLog);

        AjaxResult ajax = AjaxResult.success();
        ajax.put("inviteCount", inviteCount);
        ajax.put("clientCount", clientCount);
        ajax.put("recognitionLogCount", recognitionLogCount);
        return ajax;
    }
}

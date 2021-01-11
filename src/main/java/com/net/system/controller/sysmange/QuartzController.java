package com.net.system.controller.sysmange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.net.system.service.sysmange.QuartzService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/quartz")
@Api(tags = "QuartzController", description = "定时调度管理类")
public class QuartzController {

    @Autowired
    private QuartzService quartzService;

    /**
     * 新增任务
     */
    @GetMapping("/insert")
    public String insertTask(String jName, String jGroup, String tName, String tGroup, String cron) {
        quartzService.addJob(jName, jGroup, tName, tGroup, cron);
        return "添加成功！";
    }

    /**
     * 暂停任务
     */
    @GetMapping("/pause")
    public String pauseTask(String jName, String jGroup) {
        quartzService.pauseJob(jName, jGroup);
        return "暂停成功！";
    }

    /**
     * 继续任务
     */
    @GetMapping("/resume")
    public String resumeTask(String jName, String jGroup) {
        quartzService.resumeJob(jName, jGroup);
        return "继续成功！";
    }

    /**
     * 删除任务
     */
    @GetMapping("/delete")
    public String deleteTask(String jName, String jGroup) {
        quartzService.deleteJob(jName, jGroup);
        return "删除成功！";
    }

    /**
     * 修改任务
     */
    @GetMapping("/update")
    public String updateTask(String jName, String jGroup, String tName, String tGroup, String cron) {
    	//先把之前的任务删除
    	quartzService.deleteJob(jName, jGroup);
    	//再将新的任务添加进去
        quartzService.addJob(jName, jGroup, tName, tGroup, cron);
        return "修改成功！";
    }

    
}


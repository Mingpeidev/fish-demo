package com.mao.controller;

import com.mao.proj.Fish;
import com.mao.service.FishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Mingpeidev
 * @description 鱼
 */
@Controller
@RequestMapping("/user")
public class FishController {

    @Autowired
    private FishService fishService;

    @RequestMapping("updataSetting")
    @ResponseBody
    public void updateLogin(@RequestParam("id") int id,
                            @RequestParam("smart") int smart,
                            @RequestParam("water") int water,
                            @RequestParam("wendu") int wendu,
                            @RequestParam("o2") int o2) {
        fishService.updateSetting(id, smart, water, wendu, o2);
    }

    @RequestMapping("getSettingAll")
    @ResponseBody
    public SsmResult getSettingAll() {
        List<Fish> list = fishService.getSettingAll();
        return SsmResult.ok(list);
    }

}

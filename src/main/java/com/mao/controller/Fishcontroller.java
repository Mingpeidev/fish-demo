package com.mao.controller;

import com.mao.proj.Fish;
import com.mao.service.FishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@RequestMapping("/user")

//这里用了@SessionAttributes，可以直接把model中的user(也就key)放入其中,这样保证了session中存在user这个对象
@SessionAttributes("user")
public class Fishcontroller {

    @Autowired
    private FishService fishService;

    @RequestMapping("updataSetting")
    @ResponseBody
    public void updataLogin(@RequestParam("id") int id,
                            @RequestParam("smart") int smart,
                            @RequestParam("water") int water,
                            @RequestParam("wendu") int wendu,
                            @RequestParam("o2") int o2) {
        fishService.updataSetting(id, smart, water, wendu, o2);
    }

    @RequestMapping("getSettingAll")
    @ResponseBody
    public SsmResult getLogin() {
        System.out.println("fef");
        List<Fish> list = fishService.getSettingAll();
        return SsmResult.ok(list);
    }

}

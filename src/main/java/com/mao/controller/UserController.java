package com.mao.controller;

import com.mao.proj.User;
import com.mao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mingpeidev
 * 通过 @SessionAttributes 注解将Model中 attributeName 为 "user"、"age"、"name" 的值添加至 session 中，不是其中值不放入 session
 */
@Controller
@RequestMapping("/user")
@SessionAttributes(value = {"user", "age", "name"})
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 正常访问login页面
     *
     * @return
     */
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 测试超链接跳转到另一个页面是否可以取到session值
     *
     * @return
     */
    @RequestMapping("/anotherPage")
    public String anotherPage() {
        return "another_page";
    }

    /**
     * 注销session方法
     *
     * @param sessionStatus
     * @return
     */
    @RequestMapping("/outLogin")
    public String outLogin(SessionStatus sessionStatus) {
        //清除通过@SessionAttribute添加至 session 中的数据
        sessionStatus.setComplete();
        return "login";
    }

    /**
     * 表单提交过来的路径
     *
     * @param user
     * @param model
     * @return
     */
    @RequestMapping("/checkLogin")
    public String checkLogin(User user, Model model) {
        try {
            // 调用service方法
            user = userService.checkLogin(user.getUsername(), user.getPassword());
            // 若有user则添加到model里并且跳转到成功页面
            if (user != null) {
                model.addAttribute("user", user);
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception e) {
            return "fail";
        }
    }

    @RequestMapping("/modelSet")
    @ResponseBody
    public String modelSet(Model model) {
        try {
            User user = new User();
            user.setUsername("root");
            user.setPassword("123456");

            //在@SessionAttributes的value里面
            model.addAttribute("user", user);
            model.addAttribute("age", 13);
            //不在@SessionAttributes的value里面
            model.addAttribute("other", "other");
        } catch (Exception e) {
            return "fail" + e;
        }
        return "success";
    }

    /**
     * ModelAttribute 和 @RequestMapping 同时注释一个方法要配合 @SessionAttributes
     *
     * @return
     */
    @RequestMapping("/modelSet1")
    @ModelAttribute("name")
    public String modelSet1() {
        return "model set session!";
    }

    /**
     * 获取Session值
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "getSession", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getSession(HttpSession session) {
        Map<String, Object> requestMap = new HashMap<>();

        //在@SessionAttributes的value里面
        requestMap.put("user", session.getAttribute("user"));
        requestMap.put("age", session.getAttribute("age"));
        //ModelAttribute注解传递
        requestMap.put("name", session.getAttribute("name"));
        //不在@SessionAttributes的value里面
        requestMap.put("other", session.getAttribute("other"));

        //HttpSession设置
        requestMap.put("test", session.getAttribute("test"));

        session.setAttribute("test", "test");
        //注销当前通过HttpSession设置的session
        session.invalidate();
        return requestMap;
    }

    /**
     * 清除通过@SessionAttribute添加至 session 中的数据
     *
     * @param sessionStatus
     * @return 清除的只是该 Controller 通过 @SessionAttribute 添加至 session 的数据
     * （当然，如果不同 controller 的 @SessionAttribute 拥有相同的值，则也会清除）
     */
    @RequestMapping("/removeSessionAttributes")
    @ResponseBody
    public String removeSessionAttributes(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "result";
    }
}

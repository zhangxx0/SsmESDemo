package com.danke.web;

import com.danke.dto.DemoResult;
import com.danke.entity.UserDemo;
import com.danke.service.UserDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * demo Controller
 *
 * @date 2017年11月20日11:35:54
 */
@Component
@RequestMapping("//demo")//url:模块/资源/{}/细分
public class UserDemoController {

    @Autowired
    private UserDemoService userDemoService;

    /**
     * 获取用户列表
     * http://localhost:8088/demo/list
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        List<UserDemo> list = userDemoService.queryAll();
        model.addAttribute("users", list);
        return "list";
    }

    /**
     * 获取系统时间
     */
    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public DemoResult<Long> time() {
        Date date = new Date();
        return new DemoResult<Long>(true, date.getTime());
    }


}

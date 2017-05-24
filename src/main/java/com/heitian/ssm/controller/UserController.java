package com.heitian.ssm.controller;

import com.heitian.ssm.model.User;
import com.heitian.ssm.service.UserService;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhangxq on 2016/7/15.
 */

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger log = Logger.getLogger(UserController.class);
    @Resource
    private UserService userService;

    // /user/test?id=1
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(HttpServletRequest request, Model model) {
        long userId = Long.parseLong(request.getParameter("id"));
        System.out.println("userId:" + userId);
        User user = this.userService.getUserById(userId);

        log.debug(user.toString());
        model.addAttribute("user", user);
        return "getUserByPhone";
    }

    @RequestMapping(value = "/showUser", method = RequestMethod.GET)
    public String showUser(HttpServletRequest request, Model model) {
        log.info("查询所有用户信息");
        List<User> userList = this.userService.getAllUser();
        model.addAttribute("userList", userList);
        return "showUser";
    }

    @RequestMapping(value = "/showUserByUserPhone", method = RequestMethod.GET)
    public String showUserByUserPhone(@RequestParam("userPhone") String userPhone, Model model) {
        log.info("根据手机号查找用户信息 userPhone = " + userPhone);
        User user = this.userService.getUserByPhoneOrEmail(userPhone);
        model.addAttribute(user);
        return "getUserByPhone";
    }

    // /user/showUserByUserPhone2/{usePhone}
    @RequestMapping(value = "/showUserPathVariable/{userPhone}", method = RequestMethod.GET)
    public String showUserByUserPhone2(@PathVariable("userPhone") String userPhone, Map<String, Object> model) {
        System.out.println("userPhone = " + userPhone);
        User user = this.userService.getUserByPhoneOrEmail(userPhone);
        log.debug(user.toString());
        model.put("user", user);
        return "getUserByPhone";
    }

    @RequestMapping(value = "/{userPhone}", method = RequestMethod.POST)
    public @ResponseBody
    User getUserInJson(@PathVariable("userPhone") String userPhone, Map<String, Object> model) {
        System.out.println("userPhone = " + userPhone);
        User user = this.userService.getUserByPhoneOrEmail(userPhone);
        log.debug(user.toString());
        return user;
    }

    @RequestMapping(value = "/preUpload", method = RequestMethod.GET)
    public String showUploadPage() {
        log.info("pre upload start");
        return "fileUpload";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String doUploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                log.info("Process file:{}" + file.getOriginalFilename());
            }
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File("/Users/roc/workspace/web-ssm/upload", System.currentTimeMillis() + file.getOriginalFilename()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}

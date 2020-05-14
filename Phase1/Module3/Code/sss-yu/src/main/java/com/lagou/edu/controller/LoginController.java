package com.lagou.edu.controller;

import com.lagou.edu.pojo.Account;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/")
public class LoginController {

    @RequestMapping(value = "login")
    public ModelAndView showLoginPage() {

        ModelAndView modelAndView = new ModelAndView();
        Account account = new Account();
        modelAndView.addObject("account", account);

        modelAndView.setViewName("loginPage");

        return modelAndView;
    }

    @RequestMapping(value = "list")
    public String toResumeListPage(Account account, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("account", account);
        return "redirect:resume/queryAll";
    }
}

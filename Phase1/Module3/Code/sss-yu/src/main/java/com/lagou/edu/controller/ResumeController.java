package com.lagou.edu.controller;

import com.lagou.edu.pojo.Resume;
import com.lagou.edu.service.IResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private IResumeService resumeService;

    @RequestMapping("/queryAll")
    public ModelAndView queryAll() throws Exception {
        List<Resume> resumeList = resumeService.queryAllResumes();

        ModelAndView modelAndView = new ModelAndView();
        String result = resumeList.get(0).toString();
        modelAndView.addObject("myList", resumeList);

        modelAndView.setViewName("resumeList");

        return modelAndView;
    }
}

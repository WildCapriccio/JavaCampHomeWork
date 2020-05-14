package com.lagou.edu.controller;

import com.lagou.edu.pojo.Account;
import com.lagou.edu.pojo.Resume;
import com.lagou.edu.service.IResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private IResumeService resumeService;


    @RequestMapping("/edit")
    public ModelAndView showEditPage(Long id, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();

        Resume resume = new Resume();
        resume.setId(id);
        modelAndView.addObject("resume", resume);
        modelAndView.setViewName("resumeForm");

        return modelAndView;
    }

    @RequestMapping("/add")
    public String handleAdd(Resume resume) throws Exception {

        resumeService.addNewResume(resume);

        return "redirect:queryAll";
    }

    @RequestMapping("/update")
    public String handleUpdate(Resume resume) throws Exception {

        resumeService.updateResume(resume);

        return "redirect:queryAll";
    }

    @RequestMapping("/delete")
    public String handleDelete(@RequestParam("id") Long id) throws Exception {
        resumeService.deleteResumeById(id);

        return "redirect:queryAll";
    }

    @RequestMapping("/queryAll")
    public ModelAndView queryAll(Account account) throws Exception {
        System.out.println("从前端拿到的account是： " + account.toString());

        List<Resume> resumeList = resumeService.queryAllResumes();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("myList", resumeList);

        modelAndView.setViewName("resumeList");

        return modelAndView;
    }

}

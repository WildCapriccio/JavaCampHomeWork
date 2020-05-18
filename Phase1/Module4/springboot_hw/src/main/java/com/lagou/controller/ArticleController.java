package com.lagou.controller;

import com.lagou.pojo.Article;
import com.lagou.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping("/findAll")
    public String findAllArticles(Model model) {
        int page = 0;
        int size = 2;

        Page<Article> articles = articleService.findAllArticles(page, size);
        model.addAttribute("articles", articles);
        return "/client/index";
    }

    @RequestMapping("/findOne")
    public Article findArticleById(Integer id) {
        return articleService.findArticleById(id);
    }
}

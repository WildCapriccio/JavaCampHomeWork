package com.lagou.controller;

import com.lagou.pojo.Article;
import com.lagou.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping("/findAll")
    public List<Article> findAllArticles() {
        return articleService.findAllArticles();
    }

    @RequestMapping("/findOne")
    public Article findArticleById(Integer id) {
        return articleService.findArticleById(id);
    }
}

package com.lagou.controller;

import com.lagou.pojo.Article;
import com.lagou.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping("/findAll")
    public Page<Article> findAllArticles() {
        int page = 0;
        int size = 2;

        return articleService.findAllArticles(page, size);
    }

    @RequestMapping("/findOne")
    public Article findArticleById(Integer id) {
        return articleService.findArticleById(id);
    }
}

package com.lagou.service;

import com.lagou.pojo.Article;
import com.lagou.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public Page<Article> findAllArticles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return articleRepository.findAll(pageable);
    }

    public Article findArticleById(Integer id) {
        Optional<Article> optional = articleRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }
}

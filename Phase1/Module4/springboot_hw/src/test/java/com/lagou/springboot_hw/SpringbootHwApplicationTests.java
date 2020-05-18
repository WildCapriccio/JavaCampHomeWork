package com.lagou.springboot_hw;

import com.lagou.pojo.Article;
import com.lagou.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class SpringbootHwApplicationTests {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void testJpaFindArticleById() {
        Optional<Article> optional = articleRepository.findById(2);
        if (optional.isPresent()) {
            System.out.println(optional.get());
        }
    }

}

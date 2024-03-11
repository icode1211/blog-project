package com.estsoft.blogproject.controller;

import com.estsoft.blogproject.domain.AddArticleRequest;
import com.estsoft.blogproject.domain.Article;
import com.estsoft.blogproject.domain.ArticleResponse;
import com.estsoft.blogproject.domain.ModifyArticleRequest;
import com.estsoft.blogproject.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/api/articles")
    @ResponseBody
    public ResponseEntity<List<ArticleResponse>> showAll() {
        List<Article> list = articleService.findAll();
        List<ArticleResponse> articleResponses = list.stream().map(x -> new ArticleResponse(x.getId(), x.getTitle(), x.getContent())).toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(articleResponses);
    }

    @GetMapping("/api/articles/{id}")
    @ResponseBody
    public ResponseEntity<ArticleResponse> showOne(@PathVariable Long id) {
        Article one = articleService.findOne(id);
        return ResponseEntity.ok(one.from());
    }

    // 등록 API
    @PostMapping(value = "/api/articles")
    @ResponseBody
    public ResponseEntity<ArticleResponse> save(@RequestBody AddArticleRequest request) {
        Article article = request.mapper();
        int count = articleService.insertOneArticle(article);
        log.info("save count: {}", count);
        return ResponseEntity.status(HttpStatus.CREATED).body(article.from());
    }

    // 수정 API
    @PutMapping(value = "/api/articles/{id}")
    @ResponseBody
    public ResponseEntity<ArticleResponse> modify(@PathVariable Long id,
                                                  @RequestBody ModifyArticleRequest request) {
        Article article = articleService.update(id, request);
        return ResponseEntity.ok(article.from());
    }

    // 삭제 API
    @DeleteMapping(value = "/api/articles/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
         articleService.deleteOneArticle(id);
    }
}

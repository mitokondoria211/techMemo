package com.example.techMemo.url;

import com.example.techMemo.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UrlRepository extends JpaRepository<Url, Long> {
    // 特定記事に紐づくURL一覧
    List<Url> findByArticleOrderByUpdatedAt(Article article);
}

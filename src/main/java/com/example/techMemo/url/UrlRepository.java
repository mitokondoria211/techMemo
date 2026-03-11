package com.example.techMemo.url;

import com.example.techMemo.article.entity.Article;
import com.example.techMemo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UrlRepository extends JpaRepository<Url, Long> {
    // 自分のURL一覧（全件）
    List<Url> findByUserOrderBySortOrderAsc(User user);
    // 自分の単体ブックマーク一覧（記事未紐づけ
    List<Url> findByUserAndArticleIsNullOrderBySortOrderAsc(User user);
    // 特定記事に紐づくURL一覧
    List<Url> findByArticleOrderBySortOrderAsc(Article article);
    // 自分のURLか確認
    boolean existsByIdAndUser(Long id,User user);

    boolean existsByUrlAndUser(String url, User user);

    List<Url> findByArticleOrderBySortOrder(Article article);
}

package com.example.techMemo.like;

import com.example.techMemo.article.entity.Article;
import com.example.techMemo.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface LikeRepository extends JpaRepository<Like, Long> {

    //いいね済み確認
    boolean existsByUserAndArticle(User user, Article article);

    //いいね数
    long countByArticle(Article article);

    // いいね解除
    @Modifying
    void deleteByUserAndArticle(User user, Article article);

    //自分がいいねした一覧
    Page<Like> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}

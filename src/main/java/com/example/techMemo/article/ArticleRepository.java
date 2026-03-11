package com.example.techMemo.article;

import com.example.techMemo.article.entity.Article;
import com.example.techMemo.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByTitleLike(String title);

    List<Article> findByPublicFlag(boolean publicFlag);

    List<Article> findByUser(User user);

    Page<Article> findByUser(User user, Pageable pageable);

    Page<Article> findByPublicFlagTrue(Pageable pageable);

    // 検索（キーワード・タグ・カテゴリ）
    @Query(
        value = """
        SELECT DISTINCT a FROM Article a
        JOIN FETCH a.user
        LEFT JOIN FETCH a.category
        LEFT JOIN FETCH a.tags t
        WHERE a.publicFlag = true
        AND (:keyword IS NULL OR a.title LIKE %:keyword% OR a.content LIKE %:keyword%)
        AND (:tagId IS NULL OR t.id = :tagId)
        AND (:categoryId IS NULL OR a.category.id = :categoryId)
        """,
        countQuery = """
        SELECT COUNT(DISTINCT a) FROM Article a
        LEFT JOIN a.tags t
        WHERE a.publicFlag = true
        AND (:keyword IS NULL OR a.title LIKE %:keyword% OR a.content LIKE %:keyword%)
        AND (:tagId IS NULL OR t.id = :tagId)
        AND (:categoryId IS NULL OR a.category.id = :categoryId)
        """
    )
    Page<Article> search(
        @Param("keyword") String keyword,
        @Param("tagId") Long tagId,
        @Param("categoryId") Long categoryId,
        Pageable pageable
    );

    // ✅ 追加：userの絞り込みだけ加えたメソッド
    @Query(
        value = """
    SELECT DISTINCT a FROM Article a
    JOIN FETCH a.user
    LEFT JOIN FETCH a.category
    LEFT JOIN FETCH a.tags t
    WHERE a.user = :user
    AND (:keyword IS NULL OR a.title LIKE %:keyword% OR a.content LIKE %:keyword%)
    AND (:tagId IS NULL OR t.id = :tagId)
    AND (:categoryId IS NULL OR a.category.id = :categoryId)
    """,
        countQuery = """
    SELECT COUNT(DISTINCT a) FROM Article a
    LEFT JOIN a.tags t
    WHERE a.user = :user
    AND (:keyword IS NULL OR a.title LIKE %:keyword% OR a.content LIKE %:keyword%)
    AND (:tagId IS NULL OR t.id = :tagId)
    AND (:categoryId IS NULL OR a.category.id = :categoryId)
    """
    )
    Page<Article> searchByUser(
        @Param("keyword") String keyword,
        @Param("tagId") Long tagId,
        @Param("categoryId") Long categoryId,
        @Param("user") User user,
        Pageable pageable
    );

}

package com.example.techMemo.url;

import com.example.techMemo.article.entity.Article;
import com.example.techMemo.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
// ユーザー×URLでユニーク制約
@Table(
    name = "urls",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "url"})
)
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // NULLの場合は単体ブックマーク
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false) // ✅ 必ず記事に紐付く
    private Article article;


    @Column(nullable = false, length = 500)
    private String url;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false)
    private int sortOrder = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void update(String url, String title, int sortOrder) {
        this.url = url;
        this.title = title;
        this.sortOrder = sortOrder;
    }


    // 記事への紐づけ・解除
    public void attachToArticle(Article article) {
        this.article = article;
    }

    public void detachFromArticle() {
        this.article = null;
    }



}

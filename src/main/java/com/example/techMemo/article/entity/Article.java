package com.example.techMemo.article.entity;

import com.example.techMemo.category.Category;
import com.example.techMemo.like.Like;
import com.example.techMemo.tag.Tag;
import com.example.techMemo.url.Url;
import com.example.techMemo.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private boolean publicFlag = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(
        name = "article_tags",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private List<Tag> tags = new ArrayList<>();

    // URLとの関係（urlsテーブルのarticle_idで紐づく）
    // Article.java に追加
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Url> urls = new ArrayList<>();
// ✅ 記事削除時にURLも一緒に削除される
// ✅ orphanRemoval=trueで記事からURLを外すと自動削除


    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Like> likes = new ArrayList<>();


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void update(String title, String content,
                       Boolean publicFlag, Category category, List<Tag> tags) {
        this.title = title;
        this.content = content;
        this.publicFlag = publicFlag != null ? publicFlag : this.publicFlag;
        this.category = category;
        this.tags = tags;
    }

    public void updateUrls(List<Url> newUrls) {
        this.urls.clear();        // orphanRemoval=trueなので古いURLがDBから自動削除される
        this.urls.addAll(newUrls);
    }

    public void setPublicFlag(Boolean publicFlag) {
        this.publicFlag = publicFlag != null ? publicFlag : this.publicFlag;
    }


}

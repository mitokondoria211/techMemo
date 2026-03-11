package com.example.techMemo.like;

import com.example.techMemo.article.entity.Article;
import com.example.techMemo.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@Table(
    name = "likes",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"user_id", "article_id"}  // 同じユーザーの重複いいね防止
    )
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

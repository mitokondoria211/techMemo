package com.example.techMemo.like;

import com.example.techMemo.article.ArticleRepository;
import com.example.techMemo.article.dto.ArticleResponse;
import com.example.techMemo.article.entity.Article;
import com.example.techMemo.exception.ResourceNotFoundException;
import com.example.techMemo.mapper.ArticleMapper;
import com.example.techMemo.user.User;
import com.example.techMemo.user.UserRepository;
import com.example.techMemo.utils.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {
    private final LikeRepository repository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    @Transactional
    public void like(Long articleId) {
        User user = getUser();
        Article article = getArticleById(articleId);

        //公開記事のみいいね可能
        if (!article.isPublicFlag()) {
            throw new IllegalStateException("非公開記事にはいいねできません");
        }
        //重複いいねチェック
        if (repository.existsByUserAndArticle(user, article)) {
            throw new IllegalStateException("すでにいいねしています。");
        }

        //自分の記事にはいいねできません
        if (article.getUser().equals(user)) {
            throw new IllegalStateException("自分の記事にはいいねできません");
        }

        repository.save(Like.builder().user(user).article(article).build());

    }

    @Transactional
    public void unlike(Long articleId) {
        User user = getUser();
        Article article = getArticleById(articleId);

        // いいねしていない場合はエラー
        if (!repository.existsByUserAndArticle(user, article)) {
            throw new IllegalStateException("いいねしていません");
        }

        repository.deleteByUserAndArticle(user, article);
    }

    public boolean isLiked(Long articleId) {
        if (SecurityUtils.getCurrentUsernameOrNull() == null) return false;
        User user = getUser();
        Article article = getArticleById(articleId);
        return repository.existsByUserAndArticle(user, article);
    }

    public long getLikeCount(Long articleId) {
        Article article = getArticleById(articleId);
        return repository.countByArticle(article);
    }

    public Page<ArticleResponse> getLikedArticles(Pageable pageable) {
        User user = getUser();
        return repository.findByUserOrderByCreatedAtDesc(user, pageable)
                         .map(like -> articleMapper.toResponse(
                             like.getArticle(),
                             repository.countByArticle(like.getArticle()),
                             true));
    }

    // ユーザー取得
    private User getUser() {
        String email = SecurityUtils.getCurrentUsername();
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new ResourceNotFoundException("ユーザーが見つかりません"));
    }

    // 記事取得
    private Article getArticleById(Long id) {
        return articleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("記事が見つかりません"));
    }
}

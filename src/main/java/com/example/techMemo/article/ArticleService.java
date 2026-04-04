package com.example.techMemo.article;

import com.example.techMemo.article.dto.ArticleCreateRequest;
import com.example.techMemo.article.dto.ArticleDetailResponse;
import com.example.techMemo.article.dto.ArticleResponse;
import com.example.techMemo.article.dto.ArticleUpdateRequest;
import com.example.techMemo.article.entity.Article;
import com.example.techMemo.category.Category;
import com.example.techMemo.category.CategoryRepository;
import com.example.techMemo.exception.ForbiddenException;
import com.example.techMemo.exception.ResourceNotFoundException;
import com.example.techMemo.exception.UnauthorizedException;
import com.example.techMemo.like.LikeRepository;
import com.example.techMemo.mapper.ArticleMapper;
import com.example.techMemo.tag.Tag;
import com.example.techMemo.tag.TagService;
import com.example.techMemo.url.Url;
import com.example.techMemo.url.UrlService;
import com.example.techMemo.user.User;
import com.example.techMemo.user.UserRepository;
import com.example.techMemo.utils.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {
    private final UserRepository userRepository;
    private final ArticleRepository repository;
    private final CategoryRepository categoryRepository;
    private final LikeRepository likeRepository;
    private final UrlService urlService;
    private final ArticleMapper mapper;
    private final TagService tagService;

    //公開記事一覧
    public Page<ArticleResponse> getArticles(Pageable pageable) {
        return repository.findByPublicFlagTrue(pageable).map(
            article -> mapper.toResponse(article, likeRepository.countByArticle(article), false)
        );
    }

    //パラメータを追加して既存のsearchメソッドを活用
    public Page<ArticleResponse> getMyArticles(
        String keyword, Long tagId, Long categoryId, Pageable pageable
    ) {
        User user = getUser();
        return repository.searchByUser(keyword, tagId, categoryId, user, pageable)
                         .map(article -> mapper.toResponse(
                             article, likeRepository.countByArticle(article), false
                         ));
    }

    public List<ArticleResponse> getRecentMyArticles() {
        User user = getUser();
        return repository.findTop3ByUserOrderByUpdatedAtDesc(user)
                         .stream()
                         .map(mapper::toResponse)
                         .toList();
    }


    public Long getMyArticlesCount() {
        User user = getUser();
        return repository.countArticleByUser(user);
    }


    public Long getMyPrivateArticlesCount() {
        User user = getUser();
        return repository.countArticleByUserAndPublicFlagFalse(user);
    }

    //記事詳細
    public ArticleDetailResponse getArticle(Long id) {
        String currentUsername = SecurityUtils.getCurrentUsernameOrNull();
        User currentUser = currentUsername != null ? getUser() : null;
        Article article = getArticleById(id);

        if (!article.isPublicFlag()) {
            if (SecurityUtils.getCurrentUsernameOrNull() == null) {
                throw new UnauthorizedException("この記事は非公開です。");
            }

            if (!article.getUser().equals(currentUser)) {
                throw new UnauthorizedException("この記事は非公開です。");
            }
        }

        long likeCount = likeRepository.countByArticle(article);
        boolean likeByMe = SecurityUtils.getCurrentUsernameOrNull() != null && likeRepository.existsByUserAndArticle(
            getUser(), article);

        return mapper.toResponseDetail(article, likeCount, likeByMe);
    }

    //記事作成
    @Transactional
    public ArticleResponse create(ArticleCreateRequest request) {
        User user = getUser();
        List<Tag> tags = tagService.findOrCreateAll(request.tagNames());
        Category category = resolveCategory(request.categoryId());
        Article article = mapper.toEntity(request, user, category, tags);

        Article saved = repository.save(article);

        // ✅ UrlServiceに委譲
        if (request.urls() != null && !request.urls().isEmpty()) {
            List<Url> newUrls = urlService.buildUrls(request.urls(), article);
            urlService.createAll(request.urls(), saved);
        }
        return mapper.toResponse(saved, 0L, false);
    }

    //記事更新
    @Transactional
    public ArticleResponse update(Long id, ArticleUpdateRequest request) {
        User user = getUser();
        Article article = getArticleById(id);

        //本人チェック
        checkOwner(article, user);

        List<Tag> tags = tagService.findOrCreateAll(request.tagNames());
        Category category = resolveCategory(request.categoryId());

        article.update(request.title(), request.content(), request.publicFlag(), category, tags);

        return mapper.toResponse(article, likeRepository.countByArticle(article), false);
    }

    @Transactional
    public void delete(Long id) {
        User user = getUser();
        Article article = getArticleById(id);

        checkOwner(article, user);

        repository.delete(article);
    }

    public Page<ArticleResponse> search(String keyword, Long tagId, Long categoryId, Pageable pageable) {
        return repository.search(keyword, tagId, categoryId, pageable)
                         .map(article -> mapper.toResponse(article, likeRepository.countByArticle(article), false));
    }

    @Transactional
    public ArticleResponse updateVisibility(Long id, Boolean publicFlag) {
        User user = getUser();
        Article article = getArticleById(id);
        checkOwner(article, user);
        article.setPublicFlag(publicFlag);

        return mapper.toResponse(article);
    }

    // ユーザー取得
    private User getUser() {
        String email = SecurityUtils.getCurrentUsername();
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new ResourceNotFoundException("ユーザーが見つかりません"));
    }

    // 記事取得
    private Article getArticleById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("記事が見つかりません"));
    }

    // 本人チェック
    private void checkOwner(Article article, User user) {
        if (!user.getId().equals(article.getUser().getId())) {
            throw new ForbiddenException("この記事を操作する権限はありません");
        }
    }

    // カテゴリ取得（nullも許容）
    private Category resolveCategory(Long categoryId) {
        if (categoryId == null) return null;
        return categoryRepository.findById(categoryId)
                                 .orElseThrow(() -> new ResourceNotFoundException("カテゴリーが見つかりません"));
    }


}

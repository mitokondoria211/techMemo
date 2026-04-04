package com.example.techMemo.url;

import com.example.techMemo.article.ArticleRepository;
import com.example.techMemo.article.entity.Article;
import com.example.techMemo.exception.ResourceNotFoundException;
import com.example.techMemo.exception.UnauthorizedException;
import com.example.techMemo.mapper.UrlMapper;
import com.example.techMemo.user.User;
import com.example.techMemo.user.UserRepository;
import com.example.techMemo.utils.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UrlService {

    private final UrlRepository repository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final UrlMapper mapper;

    //自分のurl一覧
    public List<UrlResponse> getMyUrls() {
        User user = getUser();
        return repository.findByUserOrderBySortOrderAsc(user)
                         .stream()
                         .map(mapper::toResponse)
                         .toList();
    }

    @Transactional
    public UrlResponse create(UrlRequest request) {
        User user = getUser();

        if (repository.existsByUrlAndUser(request.url(), user)) {
            throw new IllegalStateException("同じURLが既に登録されています");
        }

        Url url = mapper.toEntity(request, user);
        Url saved = repository.save(url);

        return mapper.toResponse(saved);
    }

    // 記事のURL一覧取得

    public List<UrlResponse> getUrlsByArticle(Long articleId) {
        Article article = getArticleById(articleId);
        return repository.findByArticleOrderBySortOrder(article)
                         .stream()
                         .map(mapper::toResponse)
                         .toList();
    }

    // URL追加（記事作成時に一緒に使う）
    @Transactional
    public void createAll(List<UrlRequest> requests, User user, Article article) {
        requests.forEach(req -> {
            Url url = Url.builder()
                         .user(user)
                         .article(article)
                         .url(req.url())
                         .title(req.title())
                         .sortOrder(req.sortOrder())
                         .build();
            repository.save(url);
        });
    }

    @Transactional
    public UrlResponse update(Long urlId, UrlRequest request) {
        User user = getUser();
        Url url = getUrlById(urlId);
        checkOwner(url, user);
        url.update(request.url(), request.title(), request.sortOrder());
        return mapper.toResponse(url);
    }

    // URL削除
    @Transactional
    public void delete(Long urlId) {
        User user = getUser();
        Url url = getUrlById(urlId);
        checkOwner(url, user);
        repository.delete(url);
    }
    
    public UrlResponse attachToArticle(Long urlId, Long articleId) {
        User user = getUser();
        Url url = findMyUrl(urlId, user);

        Article article = articleRepository.findById(articleId)
                                           .orElseThrow(() -> new ResourceNotFoundException("記事が見つかりません"));

        url.attachToArticle(article);
        return mapper.toResponse(url);
    }

    public UrlResponse detachFromArticle(Long urlId) {
        User user = getUser();
        Url url = findMyUrl(urlId, user);

        if (url.getArticle() == null) {
            throw new IllegalStateException("このURLは記事に紐づいていません");
        }

        url.detachFromArticle();
        return mapper.toResponse(url);
    }

    private Url getUrlById(Long id) {
        return repository.findById(id)
                         .orElseThrow(() -> new ResourceNotFoundException("URLが見つかりません"));
    }


    private User getUser() {
        String email = SecurityUtils.getCurrentUsername();
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new ResourceNotFoundException("ユーザーが見つかりません"));
    }

    private Article getArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                                .orElseThrow(() -> new ResourceNotFoundException("記事が見つかりません"));
    }

    private void checkOwner(Url url, User user) {
        if (!url.getUser().equals(user)) {
            throw new UnauthorizedException("このURLを操作する権限がありません");
        }
    }

    private Url findMyUrl(Long id, User user) {
        Url url = repository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("URLが見つかりません"));

        if (!url.getUser().equals(user)) {
            throw new UnauthorizedException("このURLを操作する権限がありません");
        }

        return url;
    }
}
